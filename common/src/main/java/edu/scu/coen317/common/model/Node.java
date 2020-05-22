package edu.scu.coen317.common.model;

import edu.scu.coen317.common.util.NodeGlobalView;

public class Node implements Comparable<Node> {
    private String ip;
    private int port;
    private String hash;
    private boolean alive;
    private int processId;

    public Node(String ip, int port, String hash, int processId) {
        this.ip = ip;
        this.port = port;
        this.hash = hash;
        this.alive = true;
        this.processId = processId;
    }

    public Node(String str) {
        String[] tokens = str.split(",");
        if (tokens.length != 4)
            throw new IllegalArgumentException();
        this.ip = tokens[1];
        this.port = Integer.parseInt(tokens[2]);
        this.hash = tokens[0];
        this.alive = true;
        this.processId = Integer.parseInt((tokens[3]));
    }

    public Node(Node n) {
        this.ip = n.ip;
        this.port = n.port;
        this.hash = n.hash;
        this.alive = true;
        this.processId = n.processId;
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

    public int getProcessId() { return processId; }

    public void setProcessId() { this.processId = processId; }

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
        sb.append(",");
        sb.append(processId);
        return sb.toString();
    }

    public Node clone() {
        return new Node(ip, port, hash, processId);
    }
}
