package edu.scu.coen317.server.membership;

import edu.scu.coen317.common.message.membership.MemSyncRequest;
import edu.scu.coen317.common.message.membership.codec.MemSyncRequestEncoder;
import edu.scu.coen317.common.message.membership.codec.MemSyncResponseDecoder;
import edu.scu.coen317.common.model.Node;
import edu.scu.coen317.common.util.HashFunctions;
import edu.scu.coen317.server.DynamoNode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class MemSyncTask implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(MemSyncTask.class);

    private DynamoNode dNode;
    public MemSyncTask(DynamoNode node) {
        this.dNode = node;
    }

    @Override
    public void run() {
        LOG.debug("Membership sync started...");

        if (dNode.getMembers().size() == 1) {
            LOG.info("no other nodes to sync with, stop and wait for next cycle");
            return;
        }

        // if node is seed node, refresh membership list from source of truth
        if (dNode.getConf().isSeed()) {
            dNode.refreshMemListFromSeedFile();
        }

        Node target = getRandomNode();
        performMemSync(target, cloneMemberList());

        LOG.debug("Total number of nodes: {}", dNode.getMembers().size());
        for (Node node : dNode.getMembers()) {
            LOG.debug(node.toString());
        }

        LOG.debug("Membership sync finished...");
    }

    private List<Node> cloneMemberList() {
        List<Node> cpy = new ArrayList<>();
        for (Node node : dNode.getMembers()) {
            cpy.add(node.clone());
        }
        return cpy;
    }

    private Node getRandomNode() {
        // quick and dirty way to generate random node for getting real node
        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

        Node random = new Node("localhost", 8080, HashFunctions.randomMD5(), Integer.valueOf(pid));
        Node target = dNode.getMembers().floor(random);
        if (target == null) {
            target = dNode.getMembers().first();
        }
        return target;
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
                LOG.info("{}:{}:{} not responding to MEM_SYNC, setting as failed...",
                        target.getIp(), target.getPort(), target.getHash());
                target.setAlive(false);
                boolean removed = dNode.getMembers().remove(target);
                LOG.info("Target successfully removed from memberlist: {}", removed);
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
            if ( (exceptSelf && isSelf(n)) ) {
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
