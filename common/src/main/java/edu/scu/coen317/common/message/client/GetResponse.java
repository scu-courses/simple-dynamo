package edu.scu.coen317.common.message.client;

import edu.scu.coen317.common.message.MessageType;

public class GetResponse {
    private static final MessageType type = MessageType.GET_REPLY;
    String val;

    public GetResponse(String val) {
        this.val = val;
    }

    public static MessageType getType() {
        return type;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
