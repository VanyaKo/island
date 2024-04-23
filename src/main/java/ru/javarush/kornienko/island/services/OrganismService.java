package ru.javarush.kornienko.island.services;

import java.util.Map;

public interface OrganismService {
    default <K> void putDuplicateValueCount(Map<K, Long> organismClassCount, K key) {
        organismClassCount.putIfAbsent(key, 0L);
        organismClassCount.put(key, organismClassCount.get(key) + 1);
    }
}
