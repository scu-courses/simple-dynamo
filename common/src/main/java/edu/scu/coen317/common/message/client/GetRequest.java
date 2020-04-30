package edu.scu.coen317.common.message.client;

import edu.scu.coen317.common.message.MessageType;

public class GetRequest {
    private static final MessageType type = MessageType.GET;
    String key;

    public GetRequest(String key) {
        this.key = key;
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
}
