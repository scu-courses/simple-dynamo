package edu.scu.coen317.server.membership;

import edu.scu.coen317.common.message.membership.MemSyncRequest;
import edu.scu.coen317.common.message.membership.MemSyncResponse;
import edu.scu.coen317.server.DynamoNode;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MemSyncServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(MemSyncServerHandler.class);

    private DynamoNode dNode;
    public MemSyncServerHandler(DynamoNode node) {
        this.dNode = node;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // deal with request
        MemSyncRequest req = (MemSyncRequest) msg;
        dNode.getMembers().addAll(req.getNodes());

        // send response
        MemSyncResponse resp = new MemSyncResponse();
        ChannelFuture future = ctx.writeAndFlush(resp);
        future.addListener(ChannelFutureListener.CLOSE);
    }
}
