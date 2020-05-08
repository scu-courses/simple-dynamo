package edu.scu.coen317.server.conf;

public class NodeConf {
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
