package edu.scu.coen317.common.message.client.codec;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.message.MessageType;
import edu.scu.coen317.common.message.client.ClientResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ClientResponseEncoder extends MessageToByteEncoder<ClientResponse> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ClientResponse clientResponse, ByteBuf byteBuf) throws Exception {
        byteBuf.writeInt(clientResponse.getType().ordinal());
        if (clientResponse.getType() == MessageType.GET_REPLY) {
            byteBuf.writeInt(clientResponse.getVal().length());
            byteBuf.writeCharSequence(clientResponse.getVal(), Configuration.CHARSET);
        }
    }
}
