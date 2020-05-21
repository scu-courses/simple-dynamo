package edu.scu.coen317.server.request;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.message.MessageType;
import edu.scu.coen317.common.message.client.ClientRequest;
import edu.scu.coen317.common.message.client.ClientResponse;
import edu.scu.coen317.common.message.replication.ReplicationRequest;
import edu.scu.coen317.common.message.replication.codec.ReplicationRequestDecoder;
import edu.scu.coen317.common.message.replication.codec.ReplicationRequestEncoder;
import edu.scu.coen317.common.message.server.QuorumGetRequest;
import edu.scu.coen317.common.message.server.codec.QuorumGetRequestEncoder;
import edu.scu.coen317.common.message.server.codec.QuorumGetResponseDecoder;
import edu.scu.coen317.common.model.Node;
import edu.scu.coen317.common.util.NodeLocator;
import edu.scu.coen317.server.DynamoNode;
import edu.scu.coen317.server.replication.ReplicationClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClientRequestHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOG = LoggerFactory.getLogger(ClientRequestHandler.class);

    private DynamoNode dNode;

    public ClientRequestHandler(DynamoNode dNode) {
        this.dNode = dNode;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ClientRequest req = (ClientRequest) msg;
        ClientResponse resp = null;
        switch (req.getType()) {
            case GET:
                handleGetRequest(ctx, req);
                break;
            case PUT:
                handlePutRequest(ctx, req);
                break;
            default:
                break;
        }
    }

    private void handleGetRequest(ChannelHandlerContext ctx, ClientRequest req) {
        String localVal = DynamoNode.KVSTORE.get(req.getKey());
        String result = retrieveReplica(req.getKey(), localVal);
        if (result == null) {
            ctx.close();
        } else {
            ClientResponse resp = new ClientResponse(MessageType.GET_REPLY, result);
            ChannelFuture future = ctx.writeAndFlush(resp);
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private String retrieveReplica(String key, String localVal) {
        ConcurrentHashMap<String, Integer> quorum = new ConcurrentHashMap<>();
        quorum.put(localVal, 1);

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        ChannelPipeline cp = sc.pipeline();
                        cp.addLast(new QuorumGetResponseDecoder());
                        cp.addLast(new QuorumGetRequestEncoder());
                        cp.addLast(new QuorumGetRequestClientHandler(new QuorumGetRequest(key), quorum));
                    }
                });

            Set<Node> nodes = NodeLocator.getNodes(dNode.getMembers(), key, dNode.getConf().getHash());
            for (Node node : nodes) {
                ChannelFuture future = b.connect(node.getIp(), node.getPort()).await();
                future.channel().closeFuture().sync();
                if (!future.isSuccess()) {
                    LOG.debug("{}:{}:{} not responding to QUORUM_GET, setting it as failed...",
                            node.getIp(), node.getPort(), node.getHash());
                    dNode.getMembers().floor(node).setAlive(false);
                    boolean removed = dNode.getMembers().remove(node);
                    LOG.info("Target successfully removed from memberlist: {}", removed);
                } else {
                    LOG.debug("QUORUM_GET with {}:{}:{} succeeded...",
                            node.getIp(), node.getPort(), node.getHash());
                    dNode.getMembers().floor(node).setAlive(true);
                }
            }
        } catch (Exception e) {
            LOG.warn("QUORUM_GET request got interrupted...");
        } finally {
            workerGroup.shutdownGracefully();
        }

        for (Map.Entry<String, Integer> entry : quorum.entrySet()) {
            int occur = entry.getValue();
            if (occur >= Configuration.R) {
                return entry.getKey();
            }
        }
        return null;
    }

    private void handlePutRequest(ChannelHandlerContext ctx, ClientRequest req) {
        String val = DynamoNode.KVSTORE.put(req.getKey(), req.getVal());
        int success = performReplication(req.getKey(), req.getVal());
        if (success + 1 >= Configuration.W) {
            ClientResponse resp = new ClientResponse(MessageType.PUT_REPLY, val);
            ChannelFuture future = ctx.writeAndFlush(resp);
            future.addListener(ChannelFutureListener.CLOSE);
        } else {
            ctx.close();
        }
    }

    private int performReplication(String key, String val) {
        int success = 0;

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            ReplicationRequest req = new ReplicationRequest(key, val);
                            ChannelPipeline cp = sc.pipeline();
                            cp.addLast(new ReplicationRequestDecoder());
                            cp.addLast(new ReplicationRequestEncoder());
                            cp.addLast(new ReplicationClientHandler(req));
                        }
                    });

            Set<Node> nodes = NodeLocator.getNodes(dNode.getMembers(), key, dNode.getConf().getHash());
            for (Node node : nodes) {
                ChannelFuture future = b.connect(node.getIp(), node.getPort()).await();
                future.channel().closeFuture().sync();
                if (!future.isSuccess()) {
                    LOG.info("{}:{}:{} not responding to REPLICATION, setting it as failed...",
                            node.getIp(), node.getPort(), node.getHash());
                    dNode.getMembers().floor(node).setAlive(false);
                    boolean removed = dNode.getMembers().remove(node);
                    LOG.info("Target successfully removed from memberlist: {}", removed);
                } else {
                    LOG.info("REPLICATION with {}:{}:{} succeeded...",
                            node.getIp(), node.getPort(), node.getHash());
                    dNode.getMembers().floor(node).setAlive(true);
                    success++;
                }
            }
        } catch (Exception e) {
            LOG.warn("REPLICATION request got interrupted...");
        } finally {
            workerGroup.shutdownGracefully();
        }

        return success;
    }
}
