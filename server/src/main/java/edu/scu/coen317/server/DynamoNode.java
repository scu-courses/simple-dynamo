package edu.scu.coen317.server;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.model.Node;
import edu.scu.coen317.common.util.HashFunctions;
import edu.scu.coen317.common.util.NodeGlobalView;
import edu.scu.coen317.server.conf.NodeConf;
import edu.scu.coen317.server.membership.MemSyncTask;
import edu.scu.coen317.server.persist.InMemoryKVStore;
import edu.scu.coen317.server.persist.KVStore;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

public class DynamoNode {
    private static final Logger LOG = LoggerFactory.getLogger(DynamoNode.class);

    public static final KVStore KVSTORE = new InMemoryKVStore();

    private NodeConf conf;
    private ConcurrentSkipListSet<Node> members = new ConcurrentSkipListSet<>();
    private ScheduledExecutorService ses;

    public NodeConf getConf() {
        return conf;
    }

    public ConcurrentSkipListSet<Node> getMembers() {
        return members;
    }

    public DynamoNode(NodeConf conf) {
        this.conf = conf;
        init();

        ses = Executors.newScheduledThreadPool(1);
    }

    private void init() {
        // update membership list
        if (conf.isSeed()) {
            refreshMemListFromSeedFile();
        }
        Node self = new Node(conf.getIp(), conf.getPort(), conf.getHash());
        members.add(self);
    }

    public void refreshMemListFromSeedFile() {
        try {
            LOG.info("Refreshing membership list from source of truth since node is seed node...");
            ConcurrentSkipListSet<Node> allNodes = NodeGlobalView.readAll();
            members.addAll(allNodes);
        } catch (IOException ioe) {
            LOG.error("Having trouble reading node list file, init as empty...");
        }
    }

    public void run() throws Exception {
        startServerLoop();
    }

    private ScheduledFuture<?> startMemSyncLoop() {
        ScheduledFuture<?> future = ses.scheduleAtFixedRate(
                new MemSyncTask(this),
                Configuration.MEM_SYNC_INIT_DELAY,
                Configuration.MEM_SYNC_INTERVAL,
                TimeUnit.SECONDS);
        return future;
    }

    private void startServerLoop() throws Exception {
        // Create multi-threaded event loop for the node
        EventLoopGroup connGroup = new NioEventLoopGroup();
        EventLoopGroup execGroup = new NioEventLoopGroup();

        // First start membership sync task to run for every 1 second
        ScheduledFuture<?> future = startMemSyncLoop();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(connGroup, execGroup)
                    // using java NIO to handle incoming conn
                    .channel(NioServerSocketChannel.class)
                    // log every connection
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // handle incoming requests
                    .childHandler(new DynamoNodeChannelInitializer(this));

            ChannelFuture cf = bootstrap.bind(conf.getPort()).sync();
            cf.channel().closeFuture().sync();
        } finally {
            // cancel membership sync
            future.cancel(true);
            ses.shutdown();

            // shutdown server loops
            execGroup.shutdownGracefully();
            connGroup.shutdownGracefully();
        }
    }

    // return replication nodes except for itself
    public Set<Node> getNodes(String key) {
        Set<Node> nodes = new HashSet<>();
        Node tmp = new Node("", 0, HashFunctions.md5Hash(key));
        Node start = members.ceiling(tmp);
        Set<Node> tailSet = members.tailSet(start, true);

        // get N replicas from members list
        if (tailSet.size() >= Configuration.N) {
            int cnt = 0;
            for (Node n : tailSet) {
                if (cnt++ == Configuration.N) {
                    break;
                }
                nodes.add(n.clone());
            }
        } else {
            nodes.addAll(tailSet);
            int size = Configuration.N - nodes.size();
            for (Node n : members) {
                if (size-- == 0) {
                    break;
                }
                nodes.add(n.clone());
            }
        }

        // remove node itself from list
        nodes.removeIf(n -> n.getHash().equals(conf.getHash()));

        return nodes;
    }

    public static void main(String[] args) throws Exception {
        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        String datadir = args[2];
        String hash = args[3];
        boolean isSeed = Boolean.parseBoolean(args[4]);
        NodeConf conf = new NodeConf(ip, port, datadir, hash, isSeed);

        DynamoNode node = new DynamoNode(conf);
        node.run();
    }
}
