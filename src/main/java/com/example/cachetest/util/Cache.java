package com.example.cachetest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by kalana.w on 8/13/2020.
 */
@Component
public class Cache<K extends Serializable, V extends Serializable>{
    private static final Logger LOG = LoggerFactory.getLogger(Cache.class);
    public static String MAX_SIZE_ERROR = "maxSize should be more than 0";
    public static String ALGORITHM_ERROR = "caching algorithm was not found";

    private Storage<K, V> storage;

    public void init(Constants.Algorithm algorithm, int maxSize){
        if(maxSize <= 0) {
            LOG.error(this.MAX_SIZE_ERROR);
            throw new IllegalArgumentException(this.MAX_SIZE_ERROR);
        }
        switch (algorithm) {
            case LFU:
                this.storage = new LFUCache<>(maxSize);
                break;
            case LRU:
                this.storage = new LRUCache<>(maxSize);
                break;
            default:
                LOG.error(this.ALGORITHM_ERROR);
                throw new IllegalArgumentException(this.ALGORITHM_ERROR);
        }
    }


    public V get(K key) {
        return this.storage.get(key);
    }

    public V put(K key, V value) {
        return this.storage.put(key, value);
    }

    public int size() {
        return this.storage.size();
    }

}
