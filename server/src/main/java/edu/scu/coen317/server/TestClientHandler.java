package edu.scu.coen317.server;

import edu.scu.coen317.common.message.MessageType;
import edu.scu.coen317.common.message.client.ClientRequest;
import edu.scu.coen317.common.message.client.ClientResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TestClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Request sent");
        ClientRequest req = new ClientRequest(MessageType.PUT, "test", "val");
        ctx.writeAndFlush(req);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Response received");
        ClientResponse resp = (ClientResponse) msg;
        System.out.println(resp.getType().ordinal());
        System.out.println(resp.getVal());
        ctx.close();
    }

//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);
//    }
}
