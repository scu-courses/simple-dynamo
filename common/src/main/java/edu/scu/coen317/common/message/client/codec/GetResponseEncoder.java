package edu.scu.coen317.common.message.client.codec;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.message.client.GetResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class GetResponseEncoder extends MessageToByteEncoder<GetResponse> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, GetResponse getResponse, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(getResponse.TYPE.intValue());
        byteBuf.writeInt(getResponse.getVal().length());
        byteBuf.writeCharSequence(getResponse.getVal(), Configuration.CHARSET);
    }
}
