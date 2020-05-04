package edu.scu.coen317.common.message.client.codec;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.message.MessageType;
import edu.scu.coen317.common.message.client.ClientRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ClientRequestEncoder extends MessageToByteEncoder<ClientRequest> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ClientRequest clientRequest, ByteBuf byteBuf) throws Exception {
        System.out.println("Request Encoder");
        byteBuf.writeInt(clientRequest.getType().ordinal());
        byteBuf.writeInt(clientRequest.getKey().length());
        byteBuf.writeCharSequence(clientRequest.getKey(), Configuration.CHARSET);
        if (clientRequest.getType() == MessageType.PUT) {
            byteBuf.writeInt(clientRequest.getVal().length());
            byteBuf.writeCharSequence(clientRequest.getVal(), Configuration.CHARSET);
        }
    }
}
