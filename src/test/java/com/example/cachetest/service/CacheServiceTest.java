package com.example.cachetest.service;

import com.example.cachetest.util.Cache;
import com.example.cachetest.util.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Created by kalana.w on 8/13/2020.
 */
@SpringBootTest
class CacheServiceTest {

    @Autowired
    private CacheService cacheService;
    @Mock
    private Cache<Integer, Integer> cache;

    @BeforeEach
    void setUp() {
        this.cache.init(Constants.Algorithm.LRU, 10);
    }

    @Test
    void addNewToCache_test() {
        String add = this.cacheService.addNewToCache(1, 10000);
        Assertions.assertEquals(add, "New Item added to the Cache");
    }

    @Test
    void getByKey_test() {
        this.cacheService.addNewToCache(1, 10000);
        Integer value = this.cacheService.getByKey(1);
        Assertions.assertEquals(10000, value);
        Assertions.assertNotEquals(1, value);
    }
}