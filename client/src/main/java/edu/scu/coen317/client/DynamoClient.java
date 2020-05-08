package edu.scu.coen317.client;

import edu.scu.coen317.common.message.MessageType;
import edu.scu.coen317.common.message.client.ClientRequest;
import edu.scu.coen317.common.message.client.codec.ClientRequestEncoder;
import edu.scu.coen317.common.message.client.codec.ClientResponseDecoder;
import edu.scu.coen317.common.message.membership.MemSyncRequest;
import edu.scu.coen317.common.message.membership.codec.MemSyncRequestEncoder;
import edu.scu.coen317.common.message.membership.codec.MemSyncResponseDecoder;
import edu.scu.coen317.common.model.Node;
import edu.scu.coen317.common.util.NodeGlobalView;
import edu.scu.coen317.common.util.NodeLocator;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class DynamoClient {

    public static void main(String[] args) throws Exception {
        ConcurrentSkipListSet<Node> nodes = NodeGlobalView.readAll();

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
             .channel(NioSocketChannel.class)
             .handler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 protected void initChannel(SocketChannel sc) throws Exception {
                     ClientRequest req = new ClientRequest(MessageType.PUT, "key", "val");
                     ChannelPipeline cp = sc.pipeline();
                     cp.addLast(new ClientResponseDecoder());
                     cp.addLast(new ClientRequestEncoder());
                     cp.addLast(new RequestHandler(req));
                 }
             });

            Set<Node> targets = NodeLocator.getNodes(nodes, "key", "");
            for (Node node : targets) {
                ChannelFuture future = b.connect(node.getIp(), node.getPort()).sync();
                future.channel().closeFuture().sync();
                if (future.isSuccess()) {
                    break;
                }
            }
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
