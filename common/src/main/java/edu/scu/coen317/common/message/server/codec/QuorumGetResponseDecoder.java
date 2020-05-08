package edu.scu.coen317.common.message.server.codec;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.message.MessageType;
import edu.scu.coen317.common.message.server.QuorumGetResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class QuorumGetResponseDecoder extends ReplayingDecoder<QuorumGetResponse> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        MessageType type = MessageType.values()[buf.readInt()];
        if (type != MessageType.QUORUM_GET_REPLY) {
            throw new DecoderException("MessageType is not expected");
        }

        int len = buf.readInt();
        String val = buf.readCharSequence(len, Configuration.CHARSET).toString();
        QuorumGetResponse req = new QuorumGetResponse(val);
        list.add(req);
    }
}
