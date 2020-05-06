package edu.scu.coen317.server.membership;

import edu.scu.coen317.common.message.membership.MemSyncRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class MemSyncClientHandler extends ChannelInboundHandlerAdapter {
    private MemSyncRequest req;
    public MemSyncClientHandler(MemSyncRequest req) {
        this.req = req;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(req);
    }
}
