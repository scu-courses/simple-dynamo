package edu.scu.coen317.common.message.server.codec;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.message.server.QuorumGetRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class QuorumGetRequestEncoder extends MessageToByteEncoder<QuorumGetRequest> {
    @Override
    protected void encode(ChannelHandlerContext ctx, QuorumGetRequest req, ByteBuf buf) throws Exception {
        buf.writeInt(req.TYPE.ordinal());
        buf.writeInt(req.getKey().length());
        buf.writeCharSequence(req.getKey(), Configuration.CHARSET);
    }
}
