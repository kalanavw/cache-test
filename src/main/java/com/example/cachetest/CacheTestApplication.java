package com.example.cachetest;

import com.example.cachetest.util.Cache;
import com.example.cachetest.util.Constants;
import com.example.cachetest.util.TwoLevelCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class CacheTestApplication implements CommandLineRunner {

    @Autowired
    private Cache<Integer, Integer> cache;
    @Autowired
    private TwoLevelCache<Integer, Integer> twoLevelCache;
    @Autowired
    private Environment ENV;

    public static void main(String[] args) {
        SpringApplication.run(CacheTestApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String maxCacheSize = this.ENV.getProperty("lfu.cache.max.size");
        if (maxCacheSize == null || maxCacheSize.trim().isEmpty()) {
            maxCacheSize = "10";
        }
        int maxSize = Integer.parseInt(maxCacheSize);
        this.cache.init(Constants.Algorithm.LRU, maxSize);

        String level1Size = this.ENV.getProperty("level1.cache.max.size");
        if (level1Size == null || level1Size.trim().isEmpty()) {
            level1Size = "10";
        }
        int memorySize = Integer.parseInt(level1Size);

        String level2Size = this.ENV.getProperty("level2.cache.max.size");
        if (level2Size == null || level2Size.trim().isEmpty()) {
            level2Size = "10";
        }
        int fileSystemSize = Integer.parseInt(level2Size);
        this.twoLevelCache.init(memorySize, fileSystemSize);


        this.twoLevelCache.put(1, 11);
        this.twoLevelCache.put(2, 12);
        this.twoLevelCache.put(3, 3423);
        this.twoLevelCache.put(4, 423);
        this.twoLevelCache.put(5, 453);
        this.twoLevelCache.put(6, 345);
        this.twoLevelCache.put(7, 567);
        this.twoLevelCache.put(8, 678);
        this.twoLevelCache.put(9, 67);
        this.twoLevelCache.put(10, 61231);
        this.twoLevelCache.put(11, 61567567);

        System.out.println(this.twoLevelCache.get(9));
        System.out.println(this.twoLevelCache.size());
        System.out.println(this.twoLevelCache.get(11));
    }
}
