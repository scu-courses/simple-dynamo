package edu.scu.coen317.common;

import java.nio.charset.Charset;

public class Configuration {
    public static final String NODE_LIST = "/tmp/simple-dynamo/nodes";
    public static final Charset CHARSET = Charset.forName("UTF-8");
    public static final int MEM_SYNC_INTERVAL = 5;
    public static final int MEM_SYNC_INIT_DELAY = 3;
}
