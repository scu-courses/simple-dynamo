package edu.scu.coen317.common.message.server.codec;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.message.server.QuorumGetResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class QuorumGetResponseEncoder extends MessageToByteEncoder<QuorumGetResponse> {
    @Override
    protected void encode(ChannelHandlerContext ctx, QuorumGetResponse resp, ByteBuf buf) throws Exception {
        buf.writeInt(resp.TYPE.ordinal());
        buf.writeInt(resp.getVal().length());
        buf.writeCharSequence(resp.getVal(), Configuration.CHARSET);
    }
}
