package edu.scu.coen317.server.request;

import edu.scu.coen317.common.message.server.ServerGetRequest;
import edu.scu.coen317.common.message.server.ServerGetResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ConcurrentHashMap;

public class ServerGetRequestHandler extends ChannelInboundHandlerAdapter {

    private ServerGetRequest req;
    private ConcurrentHashMap<String, Integer> quorum;

    public ServerGetRequestHandler(ServerGetRequest req, ConcurrentHashMap<String, Integer> quorum) {
        this.req = req;
        this.quorum = quorum;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(req);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ServerGetResponse resp = (ServerGetResponse) msg;
        quorum.put(resp.getVal(), quorum.getOrDefault(resp.getVal(), 0) + 1);
    }
}
