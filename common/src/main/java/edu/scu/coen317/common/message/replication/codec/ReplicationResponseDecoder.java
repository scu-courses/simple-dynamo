package edu.scu.coen317.common.message.replication.codec;

import edu.scu.coen317.common.message.MessageType;
import edu.scu.coen317.common.message.replication.ReplicationResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class ReplicationResponseDecoder extends ReplayingDecoder<ReplicationResponse> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        if (buf.readInt() != MessageType.REPLICATION_REPLY.ordinal()) {
            throw new IllegalArgumentException("This is not a REPLICATION_REPLY message");
        }
        ReplicationResponse resp = new ReplicationResponse();
        list.add(resp);
    }
}
