package edu.scu.coen317.common.message;

public enum MessageType {
    // Client Request
    PUT(101),
    PUT_REPLY(102),
    GET(103),
    GET_REPLY(104),

    // Membership notification
    MEM_SYNC(201),
    MEM_REPLY(202),

    // Replication protocol
    REPLICATION(301),
    REPLICATION_REPLY(302);

    private final int type;

    MessageType(int type) {
        this.type = type;
    }

    public int intValue() {
        return this.type;
    }
}
