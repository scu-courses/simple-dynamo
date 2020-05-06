package edu.scu.coen317.server.membership;

import edu.scu.coen317.common.message.membership.MemSyncRequest;
import edu.scu.coen317.common.message.membership.codec.MemSyncRequestEncoder;
import edu.scu.coen317.common.message.membership.codec.MemSyncResponseDecoder;
import edu.scu.coen317.common.model.Node;
import edu.scu.coen317.server.DynamoNode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MemSyncTask implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(MemSyncTask.class);

    private DynamoNode dNode;
    public MemSyncTask(DynamoNode node) {
        this.dNode = node;
    }

    @Override
    public void run() {
        LOG.info("Membership sync started...");

        List<Node> nodes = getAllAliveNodes(false);
        if (nodes.size() == 0) {
            LOG.info("no other nodes to sync with, stop and wait for next cycle");
            return;
        }

        // if node is seed node, refresh membership list from source of truth
        if (dNode.getConf().isSeed()) {
            dNode.refreshMemListFromSeedFile();
        }

        // quick and dirty solution for getting the node reference in DynamoNode members list
        int random = new Random().nextInt(nodes.size());
        Node fakeTarget = nodes.get(random);
        Node target = dNode.getMembers().ceiling(fakeTarget);
        performMemSync(target, nodes);

        LOG.info("Total number of nodes: {}", dNode.getMembers().size());
        for (Node node : dNode.getMembers()) {
            LOG.info(node.toString());
        }

        LOG.info("Membership sync finished...");
    }

    private void performMemSync(Node target, List<Node> nodes) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
             .channel(NioSocketChannel.class)
             .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel sc) throws Exception {
                    MemSyncRequest req = new MemSyncRequest(nodes);
                    ChannelPipeline cp = sc.pipeline();
                    cp.addLast(new MemSyncResponseDecoder());
                    cp.addLast(new MemSyncRequestEncoder());
                    cp.addLast(new MemSyncClientHandler(req));
                }
             });

            ChannelFuture future = b.connect(target.getIp(), target.getPort()).await();
            future.channel().closeFuture().sync();
            if (!future.isSuccess()) {
                LOG.info("Target node {}:{}:{} not responding, setting is as failed...",
                        target.getIp(), target.getPort(), target.getHash());
                target.setAlive(false);
            } else {
                LOG.info("MEM_SYNC with {}:{}:{} succeeded...",
                        target.getIp(), target.getPort(), target.getHash());
                target.setAlive(true);
            }
        } catch (Exception e) {
            LOG.warn("MEM_SYNC request got interrupted...");
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    private List<Node> getAllAliveNodes(boolean exceptSelf) {
        List<Node> nodes = new ArrayList<>();
        for (Node n : dNode.getMembers()) {
            if ( !n.isAlive() || (exceptSelf && isSelf(n)) ) {
                continue;
            }
            Node node = new Node(n);
            nodes.add(node);
        }
        return nodes;
    }

    private boolean isSelf(Node node) {
        return node.getHash().equals(dNode.getConf().getHash());
    }
}
