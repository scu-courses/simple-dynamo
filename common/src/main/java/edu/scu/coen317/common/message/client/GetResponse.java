package edu.scu.coen317.common.message.client;

import edu.scu.coen317.common.message.MessageType;

public class GetResponse {
    public static final MessageType TYPE = MessageType.GET_REPLY;
    String val;

    public GetResponse(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
