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

    public Node(String str) {
        String[] tokens = str.split(",");
        if (tokens.length != 3)
            throw new IllegalArgumentException();
        this.ip = tokens[1];
        this.port = Integer.parseInt(tokens[2]);
        this.hash = tokens[0];
        this.alive = true;
    }

    public Node(Node n) {
        this.ip = n.ip;
        this.port = n.port;
        this.hash = n.hash;
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

    @Override
    public int hashCode() {
        int h = 17;
        h = 31 * h + ip.hashCode();
        h = 31 * h + port;
        h = 31 * h + hash.hashCode();
        return h;
    }

    @Override
    public boolean equals(Object obj) {
        Node that = (Node) obj;
        return  this.hashCode() == that.hashCode() &&
                this.ip.equals(that.ip) &&
                this.port == that.port &&
                this.hash.equals(that.hash);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(hash);
        sb.append(",");
        sb.append(ip);
        sb.append(",");
        sb.append(port);
        return sb.toString();
    }
}
