package edu.scu.coen317.common.message.client;

import edu.scu.coen317.common.message.MessageType;

public class ClientResponse {
    private MessageType type;
    private String val;

    public ClientResponse(MessageType type) {
        this.type = type;
    }

    public ClientResponse(MessageType type, String val) {
        this.type = type;
        this.val = val;
    }

    public MessageType getType() {
        return type;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
