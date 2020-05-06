package edu.scu.coen317.common.message.membership.codec;

import edu.scu.coen317.common.message.MessageType;
import edu.scu.coen317.common.message.membership.MemSyncResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MemSyncResponseDecoder extends ReplayingDecoder<MemSyncResponse> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        if (buf.readInt() != MessageType.MEM_REPLY.ordinal()) {
            throw new IllegalArgumentException("This is not a MEM_REPLY message");
        }
        MemSyncResponse resp = new MemSyncResponse();
        list.add(resp);
    }
}
