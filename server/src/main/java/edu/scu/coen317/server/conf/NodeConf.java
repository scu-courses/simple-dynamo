package edu.scu.coen317.server.conf;

public class NodeConf {
    // quorum
    public static final int N = 3;
    public static final int R = 2;
    public static final int W = 2;

    // server initialization params
    private int port;
    private String datadir;
    private boolean seed;

    public NodeConf(int port, String datadir, boolean seed) {
        this.port = port;
        this.datadir = datadir;
        this.seed = seed;
    }

    public int getPort() {
        return port;
    }

    public String getDatadir() {
        return datadir;
    }

    public boolean isSeed() {
        return seed;
    }
}
