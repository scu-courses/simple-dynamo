package edu.scu.coen317.server.request;

import edu.scu.coen317.common.message.server.QuorumGetRequest;
import edu.scu.coen317.common.message.server.QuorumGetResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ConcurrentHashMap;

public class QuorumGetRequestClientHandler extends ChannelInboundHandlerAdapter {

    private QuorumGetRequest req;
    private ConcurrentHashMap<String, Integer> quorum;

    public QuorumGetRequestClientHandler(QuorumGetRequest req, ConcurrentHashMap<String, Integer> quorum) {
        this.req = req;
        this.quorum = quorum;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(req);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        QuorumGetResponse resp = (QuorumGetResponse) msg;
        quorum.put(resp.getVal(), quorum.getOrDefault(resp.getVal(), 0) + 1);
    }
}
