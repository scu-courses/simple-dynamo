package edu.scu.coen317.common.message.replication.codec;

import edu.scu.coen317.common.message.replication.ReplicationResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ReplicationResponseEncoder extends MessageToByteEncoder<ReplicationResponse> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ReplicationResponse resp, ByteBuf buf) throws Exception {
        buf.writeInt(resp.TYPE.ordinal());
    }
}
