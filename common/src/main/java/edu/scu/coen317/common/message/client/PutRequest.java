package edu.scu.coen317.common.message.client;

import edu.scu.coen317.common.message.MessageType;

public class PutRequest {
    private static final MessageType type = MessageType.PUT;
    String key, val;

    public PutRequest(String key, String val) {
        this.key = key;
        this.val = val;
    }

    public MessageType getType() {
        return type;
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
