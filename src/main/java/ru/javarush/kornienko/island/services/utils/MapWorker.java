package ru.javarush.kornienko.island.services.utils;

import java.util.Map;

public class MapWorker {
    private MapWorker() {

    }

    public static <K> void putDuplicateValueCount(Map<K, Long> organismClassCount, K key) {
        organismClassCount.putIfAbsent(key, 0L);
        organismClassCount.put(key, organismClassCount.get(key) + 1);
    }
}
