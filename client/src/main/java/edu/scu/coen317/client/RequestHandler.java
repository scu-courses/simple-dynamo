package edu.scu.coen317.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.scu.coen317.common.message.client.ClientRequest;
import edu.scu.coen317.common.message.client.ClientResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RequestHandler extends ChannelInboundHandlerAdapter {

    private ClientRequest req;
    private ClientResponse response;
    private static final Logger LOG = LoggerFactory.getLogger(RequestHandler.class);

    public RequestHandler(ClientRequest req) {
        this.req = req;
    }

    public ClientResponse getResponse() {
		return response;
	}

	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(req);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        response = (ClientResponse) msg;
        switch (response.getType()) {
        	case PUT_REPLY:
        		LOG.info("Received PUT_REPLY");
        		break;
        	case GET_REPLY:
        		LOG.info("Received GET_REPLY, Value: {}", response.getVal());
        		break;
        	default:
        		break;
        }
    }
}
