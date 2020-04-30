package edu.scu.coen317.common.message.client.codec;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.message.client.GetRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class GetRequestDecoder extends ReplayingDecoder<GetRequest> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf buf, List<Object> list) throws Exception {
        int strLen = buf.readInt();
        String key = buf.readCharSequence(strLen, Configuration.CHARSET).toString();
        GetRequest req = new GetRequest(key);
        list.add(req);
    }
}
