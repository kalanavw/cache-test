package com.example.cachetest.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by kalana.w on 8/13/2020.
 */
class LRUCacheTest {
    private int maxSize;
    private Storage<Integer, Integer> storage;

    @BeforeEach
    void init() {
        maxSize = 4;
        storage = new LRUCache<>(maxSize);
        for (int i = 0; i < maxSize; i++) {
            storage.put(i, i);
        }
    }

    @Test
    void put() {
        Integer key = 1;
        Integer value = 111;
        Integer oldValue = storage.put(key, value);
        assertEquals(value, storage.get(key));
        assertEquals(oldValue, Integer.valueOf(1));
    }

    @Test
    void get() {
        Integer key = 1;
        assertEquals(Integer.valueOf(key), key);
    }

    @Test
    void size() {
        assertEquals(maxSize, storage.size());
        storage.put(maxSize + 1, maxSize + 1);
        assertEquals(maxSize, storage.size());
    }

    @Test
    void lru() {
        Integer key = maxSize / 2;
        storage.get(key);
        for (int i = 0; i < maxSize; i++) {
            if (i != key) {
                storage.get(i);
            }
        }
        storage.put(maxSize + 1, maxSize + 1);
        assertNull(storage.get(key));
    }
}