package edu.scu.coen317.common.message.client.codec;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.message.client.GetRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class GetRequestEncoder extends MessageToByteEncoder<GetRequest> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, GetRequest getRequest, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(getRequest.TYPE.intValue());
        byteBuf.writeInt(getRequest.getKey().length());
        byteBuf.writeCharSequence(getRequest.getKey(), Configuration.CHARSET);
    }
}
