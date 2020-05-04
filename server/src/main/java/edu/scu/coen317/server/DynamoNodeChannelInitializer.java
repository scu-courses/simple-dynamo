package edu.scu.coen317.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class DynamoNodeChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        System.out.println("DynamoNodeChannelInitializer");
        sc.pipeline().addLast(new PortUnificationReqHandler());
    }
}
