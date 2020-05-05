package edu.scu.coen317.server.persist;

public interface KVStore {
    String get(String key);
    String put(String key, String val);
}
