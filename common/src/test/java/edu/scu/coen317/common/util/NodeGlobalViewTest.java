package edu.scu.coen317.common.util;

import edu.scu.coen317.common.Configuration;
import edu.scu.coen317.common.model.Node;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentSkipListSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class NodeGlobalViewTest {

    @Test
    public void testAddNode() throws IOException {
        Node node = new Node("localhost", 8080, HashFunctions.randomMD5(), 0);
        NodeGlobalView.addNode(node);

        ConcurrentSkipListSet<Node> nodes = NodeGlobalView.readAll();
        assertEquals(1, nodes.size());
        assertEquals(node, nodes.first());

        Node node2 = new Node("localhst", 8081, HashFunctions.randomMD5(), 0);
        NodeGlobalView.addNode(node2);

        nodes = NodeGlobalView.readAll();
        assertEquals(2, nodes.size());
        assertTrue(nodes.first().equals(node) || nodes.first().equals(node2));
        assertTrue(nodes.last().equals(node) || nodes.last().equals(node2));
    }

    @AfterClass
    public static void teardown() {
        File file = new File(Configuration.NODE_LIST);
        if (file.exists()) {
            file.delete();
        }
    }
}
