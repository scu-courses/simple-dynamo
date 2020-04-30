package edu.scu.coen317.common.message.client.codec;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.message.MessageType;
import edu.scu.coen317.common.message.client.ClientRequest;
import edu.scu.coen317.common.message.client.ClientResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class ClientResponseDecoder extends ReplayingDecoder<ClientResponse> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        MessageType type = MessageType.values()[byteBuf.readInt()];
        ClientResponse resp = new ClientResponse(type);
        if (type == MessageType.PUT) {
            int valLen = byteBuf.readInt();
            String val = byteBuf.readCharSequence(valLen, Configuration.CHARSET).toString();
            resp.setVal(val);
        }
        list.add(resp);
    }
}
