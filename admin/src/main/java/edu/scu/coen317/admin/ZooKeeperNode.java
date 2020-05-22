//package edu.scu.coen317.admin;
//
//
//import edu.scu.coen317.common.model.Node;
//
//import java.util.concurrent.ConcurrentSkipListSet;
//
///**
// * Every DynamoNode should be on a single server (physical, or virtual)
// * On the same host, we should have a ZooKeeperNode
// */
//public class ZooKeeperNode {
//
//    /**
//     * Store the members list
//     */
//    private ConcurrentSkipListMap<int[], DynamoNode> members;
//
//    public ConcurrentSkipListSet<int[] DynamoNode> getAllMembers() {
//    }
//
//    public DynamoNode getDynamoNode(int hashValue) {
//
//    }
//
//    public void update(String event, int nodeNumber) {
//        // calculate new consistent hashing table
//        // [0-15] node_1
//        // [16-20] node_2
//
//
//
//        //  update the other zookeeper node by calling zookeeper client API
//    }
//
//
//}
