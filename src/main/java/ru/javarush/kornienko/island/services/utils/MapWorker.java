package ru.javarush.kornienko.island.services.utils;

import java.util.Map;

public class MapWorker {
    private MapWorker() {

    }

    public static synchronized <K> void putDuplicateValueToCountEntry(Map<K, Long> map, K key) {
        map.merge(key, 1L, Long::sum);
    }
}
