package edu.scu.coen317.server;

import edu.scu.coen317.server.conf.NodeConf;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class DynamoNode {
    private NodeConf conf;

    public DynamoNode(NodeConf conf) {
        this.conf = conf;
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
}
