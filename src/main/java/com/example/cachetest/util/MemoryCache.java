package com.example.cachetest.util;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kalana.w on 8/14/2020.
 */
public class MemoryCache<K extends Serializable, V extends Serializable> implements Storage<K, V> {

    private final Map<K, V> storage;
    private final int storageSize;

    MemoryCache(int storageSize) {
        this.storageSize = storageSize;
        this.storage = new ConcurrentHashMap<>(storageSize);
    }

    @Override
    public V get(K key) {
        return this.storage.get(key);
    }

    @Override
    public V put(K key, V value) {
        return this.storage.put(key, value);
    }

    @Override
    public int size() {
        return this.storage.size();
    }

    @Override
    public void clearCache() {
        this.storage.clear();
    }
    public boolean isExist(K key) {
        return this.storage.containsKey(key);
    }

    public boolean hasFreeSpace() {
        return size() < storageSize;
    }
}
