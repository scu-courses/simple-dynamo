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
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
        ses = Executors.newScheduledThreadPool(1);
        init();
    }

    private void init() {
        // update membership list
        if (conf.isSeed()) {
            initMemListFromSeedFile();
        }
        Node self = new Node(conf.getIp(), conf.getPort(), conf.getHash());
        members.add(self);
    }

    private void initMemListFromSeedFile() {
        try {
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
                    .childHandler(new DynamoNodeChannelInitializer());

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

    public static void main(String[] args) throws Exception {
        NodeConf conf = new NodeConf("127.0.0.1", 8080, "", HashFunctions.randomMD5(),true);
        new DynamoNode(conf).run();
    }
}
