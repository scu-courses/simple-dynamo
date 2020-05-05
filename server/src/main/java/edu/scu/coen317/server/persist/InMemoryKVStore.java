package edu.scu.coen317.server.persist;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryKVStore implements KVStore {
    private ConcurrentHashMap<String, String> map;

    public InMemoryKVStore() {
        map = new ConcurrentHashMap<>();
    }

    @Override
    public String get(String key) {
        return map.get(key);
    }

    @Override
    public String put(String key, String val) {
        return map.put(key, val);
    }
}
