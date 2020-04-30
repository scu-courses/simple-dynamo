package edu.scu.coen317.server.request;

import edu.scu.coen317.common.message.client.GetRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientRequestHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof GetRequest) {
            GetRequest
        } else { // msg is PutRequest

        }
    }
}
