package edu.scu.coen317.common.message.replication.codec;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.message.replication.ReplicationRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ReplicationRequestEncoder extends MessageToByteEncoder<ReplicationRequest> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ReplicationRequest req, ByteBuf buf) throws Exception {
        buf.writeInt(req.TYPE.ordinal());
        buf.writeInt(req.getKey().length());
        buf.writeCharSequence(req.getKey(), Configuration.CHARSET);
        buf.writeInt(req.getVal().length());
        buf.writeCharSequence(req.getVal(), Configuration.CHARSET);
    }
}
