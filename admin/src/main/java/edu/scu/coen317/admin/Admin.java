package edu.scu.coen317.admin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;

import edu.scu.coen317.client.DynamoClient;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Admin {
    private static ZooKeeper zk;
    private static ZooKeeperConnection conn;
    private static final Logger logger = LoggerFactory.getLogger(DynamoClient.class);

    public static void create(String path, String data) throws KeeperException, InterruptedException {
        byte[] bytes = data.getBytes();
        zk.create(path, bytes, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        logger.debug("create zookeeper node ({} => {})", path, data);
    }

    public static boolean node_exists(String path) throws KeeperException, InterruptedException {
        if (zk.exists(path, true) == null) {
            return false;
        }
        return true;
    }

    public static String getData(String path) throws KeeperException, InterruptedException, UnsupportedEncodingException {
        final CountDownLatch connectedSignal = new CountDownLatch(1);
        if (node_exists(path)) {
            byte[] b = zk.getData(path, new Watcher() {

                public void process(WatchedEvent we) {

                    if (we.getType() == Event.EventType.None) {
                        switch(we.getState()) {
                            case Expired:
                                connectedSignal.countDown();
                                break;
                        }

                    } else {
                        //String path = "/MyFirstZnode";

                        try {
                            byte[] bn = zk.getData(path, false, null);
                            String data = new String(bn, "UTF-8");
                            System.out.println(data);
                            connectedSignal.countDown();

                        } catch(Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }
            }, null);

            String data = new String(b, "UTF-8");
            System.out.println(data);
            connectedSignal.await();
            return data;

        } else {
            System.out.println("Node does not exists");
        }
        return null;
    }

    public static void update(String path, String data) throws KeeperException,InterruptedException {
        byte[] bytes = data.getBytes();
        zk.setData(path, bytes, zk.exists(path,true).getVersion());
    }

    public static void delete(String path) throws KeeperException,InterruptedException {
        zk.delete(path,zk.exists(path,true).getVersion());
    }


}
