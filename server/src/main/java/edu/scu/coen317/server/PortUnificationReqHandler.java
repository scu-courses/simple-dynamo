package edu.scu.coen317.server;

import edu.scu.coen317.common.message.MessageType;
import edu.scu.coen317.common.message.client.codec.ClientRequestDecoder;
import edu.scu.coen317.common.message.client.codec.ClientResponseEncoder;
import edu.scu.coen317.server.request.ClientRequestHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PortUnificationReqHandler extends ByteToMessageDecoder {

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
            default:
                throw new RuntimeException("Unrecognized message type");
        }
    }

    private void processClientRequest(ChannelHandlerContext ctx) {
        ChannelPipeline cp = ctx.pipeline();
        cp.addLast(new ClientRequestDecoder());
        cp.addLast(new ClientResponseEncoder());
        cp.addLast(new ClientRequestHandler());
        cp.remove(this);
    }
}
