package edu.scu.coen317.common.message.replication.codec;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.message.MessageType;
import edu.scu.coen317.common.message.membership.MemSyncRequest;
import edu.scu.coen317.common.message.replication.ReplicationRequest;
import edu.scu.coen317.common.model.Node;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.ArrayList;
import java.util.List;

public class ReplicationRequestDecoder extends ReplayingDecoder<ReplicationRequest> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        MessageType type = MessageType.values()[buf.readInt()];
        if (type != MessageType.REPLICATION) {
            throw new DecoderException("MessageType is not expected");
        }

        int kLen = buf.readInt();
        String key = buf.readCharSequence(kLen, Configuration.CHARSET).toString();
        int vLen = buf.readInt();
        String val = buf.readCharSequence(vLen, Configuration.CHARSET).toString();

        ReplicationRequest req = new ReplicationRequest(key, val);
        list.add(req);
    }
}
