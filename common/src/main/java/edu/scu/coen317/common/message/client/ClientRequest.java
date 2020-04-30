package edu.scu.coen317.common.message.client;

import edu.scu.coen317.common.message.MessageType;

public class ClientRequest {
    private MessageType type;
    private String key, val;

    public ClientRequest(MessageType type, String key) {
        this.type = type;
        this.key = key;
    }

    public ClientRequest(MessageType type, String key, String val) {
        this.type = type;
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
