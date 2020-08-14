package com.example.cachetest.service;

import com.example.cachetest.util.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * Created by kalana.w on 8/13/2020.
 */
@Service
public class CacheService {
    @Autowired
    private Cache<Integer, Integer> cache;
    @Autowired
    private Environment ENV;

    public String addNewToCache(Integer key, Integer value) {
        this.cache.put(key, value);
        return "New Item added to the Cache";
    }
    public Integer getByKey(Integer key) {
        Integer value = this.cache.get(key);
        return value;
    }
}
