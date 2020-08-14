package com.example.cachetest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.String.format;

/**
 * Created by kalana.w on 8/14/2020.
 */
public class FileSystemCache<K extends Serializable, V extends Serializable> implements Storage<K, V> {
    private static final Logger LOG = LoggerFactory.getLogger(FileSystemCache.class);

    private final Map<K, String> storage;
    private Path tempFolder;
    private int storageSize;

    public FileSystemCache(int storageSize) {
        try {
            this.tempFolder = Files.createTempDirectory("cache");
            this.tempFolder.toFile().deleteOnExit();
        } catch (IOException e) {
            this.LOG.error("Can not create a temp directory", e);
            e.printStackTrace();
        }
        this.storageSize = storageSize;
        this.storage = new ConcurrentHashMap<>(this.storageSize);
    }

    @Override
    public V get(K key) {
        if (this.storage.containsKey(key)) {
            String fileName = this.storage.get(key);
            try (InputStream fileInputStream = new FileInputStream(new File(this.tempFolder + File.separator + fileName));
                 ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                V objectFromCache = (V) objectInputStream.readObject();
                this.LOG.debug("Get an object with key '%s' from file system cache", key);
                return objectFromCache;
            } catch (ClassNotFoundException | IOException e) {
                LOG.error("Can not read a file", fileName, e);
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        File tmpFile = null;
        try {
            tmpFile = Files.createTempFile(tempFolder, "file_", ".cache").toFile();
        } catch (IOException e) {
            this.LOG.error("Can not create a temp fil", e);
        }

        if (tmpFile != null) {
            try (OutputStream fileOutputStream = new FileOutputStream(tmpFile);
                 ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {
                outputStream.writeObject(value);
                outputStream.flush();
                this.storage.put(key, tmpFile.getName());
                this.LOG.debug("Put an object with key '%s' into file system cache", key);
            } catch (IOException e) {
                this.LOG.error("Can not write an object to a file '%s': %s", tmpFile.getName(), e);
            }
        }
        return value;
    }

    @Override
    public int size() {
        return this.storage.size();
    }

    @Override
    public void clearCache() {
        try {
            Files.walk(this.tempFolder)
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .forEach(file -> {
                        if (file.delete()) {
                            this.LOG.debug("Delete an object '%s' from file system cache", file);
                        } else {
                            this.LOG.error("Can not delete a file", file);
                        }
                    });
        } catch (IOException e) {
            this.LOG.error("Can not access the cache directory. %s", e);
        }
        this.storage.clear();
    }
    public boolean isExist(K key) {
        return this.storage.containsKey(key);
    }

    public boolean hasFreeSpace() {
        return size() < storageSize;
    }
}
