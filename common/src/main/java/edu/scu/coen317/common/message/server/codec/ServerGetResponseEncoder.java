package edu.scu.coen317.common.message.server.codec;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.message.server.ServerGetResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ServerGetResponseEncoder extends MessageToByteEncoder<ServerGetResponse> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ServerGetResponse resp, ByteBuf buf) throws Exception {
        buf.writeInt(resp.TYPE.ordinal());
        buf.writeInt(resp.getVal().length());
        buf.writeCharSequence(resp.getVal(), Configuration.CHARSET);
    }
}
