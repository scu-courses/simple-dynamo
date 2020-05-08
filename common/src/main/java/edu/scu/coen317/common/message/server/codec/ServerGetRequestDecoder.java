package edu.scu.coen317.common.message.server.codec;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.message.MessageType;
import edu.scu.coen317.common.message.server.ServerGetRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class ServerGetRequestDecoder extends ReplayingDecoder<ServerGetRequest> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        MessageType type = MessageType.values()[buf.readInt()];
        if (type != MessageType.QUORUM_GET) {
            throw new DecoderException("MessageType is not expected");
        }

        int len = buf.readInt();
        String key = buf.readCharSequence(len, Configuration.CHARSET).toString();
        ServerGetRequest req = new ServerGetRequest(key);
        list.add(req);
    }
}
