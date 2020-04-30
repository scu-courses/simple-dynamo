package edu.scu.coen317.common.message.client.codec;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.message.client.GetResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class GetResponseDecoder extends ReplayingDecoder<GetResponse> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int strLen = byteBuf.readInt();
        String val = byteBuf.readCharSequence(strLen, Configuration.CHARSET).toString();
        GetResponse resp = new GetResponse(val);
        list.add(resp);
    }
}
