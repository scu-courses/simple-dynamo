package edu.scu.coen317.server;

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


    }
}
