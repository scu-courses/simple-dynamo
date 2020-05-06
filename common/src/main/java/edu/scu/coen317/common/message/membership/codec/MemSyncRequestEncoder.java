package edu.scu.coen317.common.message.membership.codec;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.message.membership.MemSyncRequest;
import edu.scu.coen317.common.model.Node;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

public class MemSyncRequestEncoder extends MessageToByteEncoder<MemSyncRequest> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MemSyncRequest req, ByteBuf buf) throws Exception {
        buf.writeInt(req.TYPE.ordinal());
        List<Node> nodes = req.getNodes();
        buf.writeInt(nodes.size());
        for (Node node : nodes) {
            String s = node.toString();
            buf.writeInt(s.length());
            buf.writeCharSequence(s, Configuration.CHARSET);
        }
    }
}
