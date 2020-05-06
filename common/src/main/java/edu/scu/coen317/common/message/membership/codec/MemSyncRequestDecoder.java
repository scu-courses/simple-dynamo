package edu.scu.coen317.common.message.membership.codec;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.message.MessageType;
import edu.scu.coen317.common.message.client.ClientRequest;
import edu.scu.coen317.common.message.membership.MemSyncRequest;
import edu.scu.coen317.common.model.Node;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.ArrayList;
import java.util.List;

public class MemSyncRequestDecoder extends ReplayingDecoder<MemSyncRequest> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        MessageType type = MessageType.values()[buf.readInt()];
        if (type != MessageType.MEM_SYNC) {
            throw new DecoderException("MessageType is not expected...");
        }
        
        List<Node> nodes = new ArrayList<>();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            int len = buf.readInt();
            String str = buf.readCharSequence(len, Configuration.CHARSET).toString();
            nodes.add(new Node(str));
        }
        MemSyncRequest req = new MemSyncRequest(nodes);
        list.add(req);
    }
}
