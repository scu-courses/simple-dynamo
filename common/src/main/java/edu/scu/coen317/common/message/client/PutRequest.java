package edu.scu.coen317.common.message.client;

import edu.scu.coen317.common.message.MessageType;

public class PutRequest {
    public static final MessageType TYPE = MessageType.PUT;
    String key, val;

    public PutRequest(String key, String val) {
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
