package com.praveendocoding.lowleveldesign.patterns.singleton;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

public enum CacheManager {
    INSTANCE;

    private record CacheEntry(String value, Instant expiresAt){
        boolean isExpired() {
            return expiresAt != null && expiresAt.isBefore(Instant.now());
        }
    }

    // create cache map
    final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

    // put with ttl
    public void put(String key, String value, long ttlSeconds) {
        Instant expiresAt = ttlSeconds > 0 ? Instant.now().plusSeconds(ttlSeconds) : null;
        cache.put(key, new CacheEntry(value, expiresAt));
    }

    // put without ttl
    public void put(String key, String value) {
        put(key, value, 0);
    }

    // get
    public String get(String key) {
        CacheEntry cacheEntry = cache.get(key);
        if(cacheEntry == null) { return null; }
        if(cacheEntry.isExpired()) {
            cache.remove(key);
            return null;
        }
        return cacheEntry.value;
    }

    // remove
    public void remove(String key) {
        cache.remove(key);
    }

    // size
    public int size() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
        return cache.size();
    }
}


class Main{
    public static void main(String[] args){
        CacheManager cacheManager1 = CacheManager.INSTANCE;
        CacheManager cacheManager2 = CacheManager.INSTANCE;

        System.out.println("cacheManager1 == cacheManager2: " + (cacheManager1 == cacheManager2));

        cacheManager1.put("smartest.employee", "Praveen", 300);
        cacheManager2.put("most.read.book", "System Design by Alex XU");

        System.out.println("Value of Key {most.read.book} is: " + cacheManager1.get("most.read.book"));
        System.out.println("Value of Key {smartest.employee} is: " + cacheManager2.get("smartest.employee"));

        System.out.println("Cache Size: " + cacheManager1.size());
    }
}
