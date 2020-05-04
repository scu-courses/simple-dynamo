package edu.scu.coen317.common.message.client.codec;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.message.MessageType;
import edu.scu.coen317.common.message.client.ClientRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class ClientRequestDecoder extends ReplayingDecoder<ClientRequest> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("Request Decoder");
        MessageType type = MessageType.values()[byteBuf.readInt()];
        int keyLen = byteBuf.readInt();
        String key = byteBuf.readCharSequence(keyLen, Configuration.CHARSET).toString();
        ClientRequest req = new ClientRequest(type, key);
        if (type == MessageType.PUT) {
            int valLen = byteBuf.readInt();
            String val = byteBuf.readCharSequence(valLen, Configuration.CHARSET).toString();
            req.setVal(val);
        }
        list.add(req);
    }
}
