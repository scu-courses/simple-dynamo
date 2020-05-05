package edu.scu.coen317.common.model;

public class Node implements Comparable<Node> {
    private String ip;
    private int port;
    private String hash;
    private boolean alive;

    public Node(String ip, int port, String hash) {
        this.ip = ip;
        this.port = port;
        this.hash = hash;
        this.alive = true;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public int compareTo(Node that) {
        return this.hash.compareTo(that.hash);
    }
}
