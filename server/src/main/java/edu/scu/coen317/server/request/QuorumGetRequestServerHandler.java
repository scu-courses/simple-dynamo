package edu.scu.coen317.server.request;

import edu.scu.coen317.common.message.server.QuorumGetRequest;
import edu.scu.coen317.common.message.server.QuorumGetResponse;
import edu.scu.coen317.server.DynamoNode;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class QuorumGetRequestServerHandler extends ChannelInboundHandlerAdapter {

    private DynamoNode dNode;

    public QuorumGetRequestServerHandler(DynamoNode dNode) {
        this.dNode = dNode;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        QuorumGetRequest req = (QuorumGetRequest) msg;
        String key = req.getKey();
        QuorumGetResponse resp = new QuorumGetResponse(dNode.KVSTORE.get(key));
        ChannelFuture future = ctx.writeAndFlush(resp);
        future.addListener(ChannelFutureListener.CLOSE);
    }
}
