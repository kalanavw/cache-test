package com.example.cachetest.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kalana.w on 8/13/2020.
 */
public class LFUCache<K extends Serializable, V extends Serializable> implements Storage<K, V> {
    private final int MAX_SIZE;
    private ConcurrentHashMap<K, V> storage;
    private HashMap<K, Long> keyFrequency;
    private TreeMap<Long, HashSet<K>> sortedFrequencies;


    public LFUCache(int maxSize) {
        this.MAX_SIZE = maxSize;
        this.storage = new ConcurrentHashMap<>(maxSize);
        this.keyFrequency = new HashMap<>();
        this.sortedFrequencies = new TreeMap<>();
    }

    private void eviction() {
        Long minFrequency = this.sortedFrequencies.firstKey();
        K evictionKey = this.sortedFrequencies.get(minFrequency).iterator().next();
        delFromSortedFrequencies(evictionKey, minFrequency);
        this.keyFrequency.remove(evictionKey);
        this.storage.remove(evictionKey);
    }

    private void frequencyIncrement(K key) {
        Long frequency = this.keyFrequency.compute(key, (k, f) -> f + 1L);
        delFromSortedFrequencies(key, frequency - 1);
        this.sortedFrequencies.computeIfAbsent(frequency, keys -> new HashSet<>()).add(key);
    }

    private void delFromSortedFrequencies(K key, Long frequency) {
        if (this.sortedFrequencies.get(frequency).size() > 1) {
            this.sortedFrequencies.get(frequency).remove(key);
        } else {
            this.sortedFrequencies.remove(frequency);
        }
    }

    @Override
    public V put(K key, V value) {
        if (this.storage.size() == this.MAX_SIZE) {
            eviction();
        }
        V oldValue = this.storage.put(key, value);
        Long frequency = this.keyFrequency.computeIfAbsent(key, f -> 1L);
        this.sortedFrequencies.computeIfAbsent(frequency, keys -> new HashSet<>()).add(key);
        return oldValue;
    }

    @Override
    public V get(K key) {
        V value = this.storage.get(key);
        if (value != null) {
            frequencyIncrement(key);
        }
        return value;
    }

    @Override
    public int size() {
        return this.storage.size();
    }

    @Override
    public void clearCache() {
        this.storage.clear();
    }

    @Override
    public String toString() {
        return this.storage.toString();
    }
}
