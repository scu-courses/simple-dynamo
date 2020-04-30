package edu.scu.coen317.common.message.client;

import edu.scu.coen317.common.message.MessageType;

public class GetRequest {
    public static final MessageType TYPE = MessageType.GET;
    String key;

    public GetRequest() {}

    public GetRequest(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
