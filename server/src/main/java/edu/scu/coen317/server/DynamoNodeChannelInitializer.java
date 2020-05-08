package edu.scu.coen317.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class DynamoNodeChannelInitializer extends ChannelInitializer<SocketChannel> {
    private DynamoNode dNode;

    public DynamoNodeChannelInitializer(DynamoNode dNode) {
        this.dNode = dNode;
    }

    @Override
    protected void initChannel(SocketChannel sc) throws Exception {
        sc.pipeline().addLast(new PortUnificationReqHandler(dNode));
    }
}
