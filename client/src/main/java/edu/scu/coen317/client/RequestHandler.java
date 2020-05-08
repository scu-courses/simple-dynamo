package edu.scu.coen317.client;

import edu.scu.coen317.common.message.MessageType;
import edu.scu.coen317.common.message.client.ClientRequest;
import edu.scu.coen317.common.message.client.ClientResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RequestHandler extends ChannelInboundHandlerAdapter {

    private ClientRequest req;

    public RequestHandler(ClientRequest req) {
        this.req = req;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(req);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ClientResponse resp = (ClientResponse) msg;
        System.out.println(resp.getType() + " " + resp.getVal());
    }
}
