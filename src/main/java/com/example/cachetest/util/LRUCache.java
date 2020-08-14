package com.example.cachetest.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by kalana.w on 8/13/2020.
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> implements Storage<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final boolean ACCESS_ORDER = true;
    private final int MAX_SIZE;

    public LRUCache(int maxSize) {
        super(maxSize, LOAD_FACTOR, ACCESS_ORDER);
        MAX_SIZE = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > MAX_SIZE;
    }

    @Override
    public void clearCache() {
        this.clear();
    }
}
