package com.example.cachetest.util;

/**
 * Created by kalana.w on 8/13/2020.
 */
public interface Storage<K, V> {
    V get(K key);

    V put(K key, V value);

    int size();

    void clearCache();
}
