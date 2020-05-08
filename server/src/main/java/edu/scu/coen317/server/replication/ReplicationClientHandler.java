package edu.scu.coen317.server.replication;

import edu.scu.coen317.common.message.replication.ReplicationRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ReplicationClientHandler extends ChannelInboundHandlerAdapter {

    private ReplicationRequest req;
    public ReplicationClientHandler(ReplicationRequest req) {
        this.req = req;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(req);
    }


}
