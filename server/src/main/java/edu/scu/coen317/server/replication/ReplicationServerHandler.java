package edu.scu.coen317.server.replication;

import edu.scu.coen317.common.message.replication.ReplicationRequest;
import edu.scu.coen317.common.message.replication.ReplicationResponse;
import edu.scu.coen317.server.DynamoNode;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ReplicationServerHandler extends ChannelInboundHandlerAdapter {

    private DynamoNode dNode;

    public ReplicationServerHandler(DynamoNode dNode) {
        this.dNode = dNode;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ReplicationRequest req = (ReplicationRequest) msg;
        String key = req.getKey();
        String val = req.getVal();
        dNode.KVSTORE.put(key, val);
        ReplicationResponse resp = new ReplicationResponse();
        ChannelFuture future = ctx.writeAndFlush(resp);
        future.addListener(ChannelFutureListener.CLOSE);
    }
}
