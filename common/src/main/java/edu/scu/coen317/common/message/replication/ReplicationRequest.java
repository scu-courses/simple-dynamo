package edu.scu.coen317.common.message.replication;

import edu.scu.coen317.common.message.MessageType;

public class ReplicationRequest {
    public static final MessageType TYPE = MessageType.REPLICATION;

    private String key;
    private String val;

    public ReplicationRequest(String key, String val) {
        this.key = key;
        this.val = val;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
