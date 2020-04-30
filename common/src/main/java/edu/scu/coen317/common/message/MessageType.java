package edu.scu.coen317.common.message;

public enum MessageType {
    // Client Request
    PUT,
    PUT_REPLY,
    GET,
    GET_REPLY,

    // Membership notification
    MEM_SYNC,
    MEM_REPLY,

    // Replication protocol
    REPLICATION,
    REPLICATION_REPLY,
}
