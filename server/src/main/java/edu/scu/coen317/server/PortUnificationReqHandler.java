package edu.scu.coen317.server;

import edu.scu.coen317.common.message.MessageType;
import edu.scu.coen317.common.message.client.codec.ClientRequestDecoder;
import edu.scu.coen317.common.message.client.codec.ClientResponseEncoder;
import edu.scu.coen317.common.message.membership.codec.MemSyncRequestDecoder;
import edu.scu.coen317.common.message.membership.codec.MemSyncResponseEncoder;
import edu.scu.coen317.common.message.replication.codec.ReplicationRequestDecoder;
import edu.scu.coen317.common.message.replication.codec.ReplicationResponseEncoder;
import edu.scu.coen317.server.membership.MemSyncServerHandler;
import edu.scu.coen317.server.replication.ReplicationServerHandler;
import edu.scu.coen317.server.request.ClientRequestHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PortUnificationReqHandler extends ByteToMessageDecoder {
    private static final Logger LOG = LoggerFactory.getLogger(PortUnificationReqHandler.class);

    private DynamoNode dNode;

    public PortUnificationReqHandler(DynamoNode node) {
        this.dNode = node;
    }

    protected PortUnificationReqHandler() {}

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        // we use the first 4 byte to represent an enum which indicates the message type
        if (buf.readableBytes() < 4) {
            return;
        }

        MessageType type = MessageType.values()[buf.readInt()];
        buf.resetReaderIndex();
        switch (type) {
            case GET:
            case PUT:
                processClientRequest(ctx);
                break;
            case MEM_SYNC:
                processMemSync(ctx);
                break;
            case REPLICATION:
                processReplication(ctx);
                break;
            default:
                throw new RuntimeException("Unrecognized message type");
        }
    }

    private void processClientRequest(ChannelHandlerContext ctx) {
        ChannelPipeline cp = ctx.pipeline();
        cp.addLast(new ClientRequestDecoder());
        cp.addLast(new ClientResponseEncoder());
        cp.addLast(new ClientRequestHandler(dNode));
        cp.remove(this);
    }

    private void processMemSync(ChannelHandlerContext ctx) {
        ChannelPipeline cp = ctx.pipeline();
        cp.addLast(new MemSyncRequestDecoder());
        cp.addLast(new MemSyncResponseEncoder());
        cp.addLast(new MemSyncServerHandler(dNode));
        cp.remove(this);
    }

    private void processReplication(ChannelHandlerContext ctx) {
        ChannelPipeline cp = ctx.pipeline();
        cp.addLast(new ReplicationRequestDecoder());
        cp.addLast(new ReplicationResponseEncoder());
        cp.addLast(new ReplicationServerHandler(dNode));
        cp.remove(this);
    }
}
