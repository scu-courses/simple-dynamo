package edu.scu.coen317.server.request;

import edu.scu.coen317.common.message.MessageType;
import edu.scu.coen317.common.message.client.ClientRequest;
import edu.scu.coen317.common.message.client.ClientResponse;
import edu.scu.coen317.server.DynamoNode;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientRequestHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ClientRequest req = (ClientRequest) msg;
        ClientResponse resp = null;
        switch (req.getType()) {
            case GET:
                resp = handleGetRequest(req);
                break;
            case PUT:
                resp = handlePutRequest(req);
                break;
            default:
                break;
        }
        ChannelFuture future = ctx.writeAndFlush(resp);
        future.addListener(ChannelFutureListener.CLOSE);
    }

    private ClientResponse handleGetRequest(ClientRequest request) {
        String val = DynamoNode.KVSTORE.get(request.getKey());
        return new ClientResponse(MessageType.GET_REPLY, val);
    }

    private ClientResponse handlePutRequest(ClientRequest request) {
        String val = DynamoNode.KVSTORE.put(request.getKey(), request.getVal());
        return new ClientResponse(MessageType.PUT_REPLY, val);
    }
}
