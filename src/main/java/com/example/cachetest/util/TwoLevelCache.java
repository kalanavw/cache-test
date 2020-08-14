package com.example.cachetest.util;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by kalana.w on 8/14/2020.
 */
@Component
public class TwoLevelCache<K extends Serializable, V extends Serializable> implements Storage<K, V> {

    private MemoryCache<K, V> firstLevelCache;
    private FileSystemCache<K, V> secondLevelCache;

    public void init(final int memorySize, final int fileSystemSize) {
        this.firstLevelCache = new MemoryCache<>(memorySize);
        this.secondLevelCache = new FileSystemCache<>(fileSystemSize);
    }

    @Override
    public V get(K key) {
        if (this.firstLevelCache.isExist(key)) {
            return this.firstLevelCache.get(key);
        } else if (this.secondLevelCache.isExist(key)) {
            return this.secondLevelCache.get(key);
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if (this.firstLevelCache.isExist(key) || this.firstLevelCache.hasFreeSpace()) {
            this.firstLevelCache.put(key, value);
        } else if (this.secondLevelCache.isExist(key) || this.secondLevelCache.hasFreeSpace()) {
            this.secondLevelCache.put(key, value);
        }
        return null;
    }

    @Override
    public int size() {
        return (this.firstLevelCache.size() + this.secondLevelCache.size());
    }

    @Override
    public void clearCache() {
        this.firstLevelCache.clearCache();
        this.secondLevelCache.clearCache();
    }
}
