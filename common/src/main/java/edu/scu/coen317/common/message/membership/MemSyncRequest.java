package edu.scu.coen317.common.message.membership;

import edu.scu.coen317.common.message.MessageType;
import edu.scu.coen317.common.model.Node;

import java.util.List;

public class MemSyncRequest {
    public static final MessageType TYPE = MessageType.MEM_SYNC;

    public MemSyncRequest(List<Node> nodes) {
        this.nodes = nodes;
    }

    private List<Node> nodes;

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
}
