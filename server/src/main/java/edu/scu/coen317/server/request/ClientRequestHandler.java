package edu.scu.coen317.server.request;

import edu.scu.coen317.common.message.MessageType;
import edu.scu.coen317.common.message.client.ClientRequest;
import edu.scu.coen317.common.message.client.ClientResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientRequestHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ClientRequest req = (ClientRequest) msg;
        System.out.println(req.getKey());
        System.out.println(req.getVal());
        ClientResponse resp = new ClientResponse(MessageType.GET_REPLY, "get_val");
        ChannelFuture future = ctx.writeAndFlush(resp);
    }
}
