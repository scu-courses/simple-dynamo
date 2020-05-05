package edu.scu.coen317.server.conf;

public class NodeConf {
    // quorum
    public static final int N = 3;
    public static final int R = 2;
    public static final int W = 2;

    // server initialization params
    private String ip;
    private int port;
    private String datadir;
    private String hash;
    private boolean seed;

    public NodeConf(String ip, int port, String datadir, String hash, boolean seed) {
        this.ip = ip;
        this.port = port;
        this.datadir = datadir;
        this.hash = hash;
        this.seed = seed;
    }

    public String getIp() {
        return ip;
    }

    public String getHash() {
        return hash;
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
