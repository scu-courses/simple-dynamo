package edu.scu.coen317.server;

import edu.scu.coen317.common.model.Node;
import edu.scu.coen317.common.util.NodeGlobalView;
import edu.scu.coen317.server.conf.NodeConf;
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
import java.util.concurrent.ConcurrentSkipListSet;

public class DynamoNode {
    private static final Logger LOG = LoggerFactory.getLogger(DynamoNode.class);

    public static final KVStore KVSTORE = new InMemoryKVStore();
    public static final ConcurrentSkipListSet<Node> MEMBERS = new ConcurrentSkipListSet<>();

    private NodeConf conf;

    public DynamoNode(NodeConf conf) {
        this.conf = conf;
        init();
    }

    private void init() {
        // update membership list
        if (conf.isSeed()) {
            initMemListFromSeedFile();
        } else {
            Node self = new Node(conf.getIp(), conf.getPort(), conf.getHash());
            MEMBERS.add(self);
        }
    }

    private void initMemListFromSeedFile() {
        try {
            ConcurrentSkipListSet<Node> allNodes = NodeGlobalView.readAll();
            MEMBERS.addAll(allNodes);
        } catch (IOException ioe) {
            LOG.error("Having trouble reading node list file, init as empty...");
        }
    }

    public void run() throws Exception {
        // Create multi-threaded event loop for the node
        EventLoopGroup connGroup = new NioEventLoopGroup();
        EventLoopGroup execGroup = new NioEventLoopGroup();

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
            execGroup.shutdownGracefully();
            connGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        NodeConf conf = new NodeConf("127.0.0.1", 8080, "", "",true);
        new DynamoNode(conf).run();
    }
}
