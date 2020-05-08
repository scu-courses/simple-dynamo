package edu.scu.coen317.common;

import java.nio.charset.Charset;

public class Configuration {
    // quorum
    public static final int N = 3;
    public static final int R = 2;
    public static final int W = 2;

    public static final String NODE_LIST = "/tmp/simple-dynamo/nodes";
    public static final Charset CHARSET = Charset.forName("UTF-8");
    public static final int MEM_SYNC_INTERVAL = 10;
    public static final int MEM_SYNC_INIT_DELAY = 3;
}
