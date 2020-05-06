package edu.scu.coen317.common.message.membership.codec;

import edu.scu.coen317.common.message.membership.MemSyncResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MemSyncResponseEncoder extends MessageToByteEncoder<MemSyncResponse> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MemSyncResponse resp, ByteBuf buf) throws Exception {
        buf.writeInt(resp.TYPE.ordinal());
    }
}
