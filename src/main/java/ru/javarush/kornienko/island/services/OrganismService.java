package ru.javarush.kornienko.island.services;

import ru.javarush.kornienko.island.models.abstracts.Organism;

import java.util.Map;

public interface OrganismService {
    default void putDuplicateClassCount(Map<Class<? extends Organism>, Long> organismClassCount, Class<? extends Organism> clazz) {
        organismClassCount.putIfAbsent(clazz, 0L);
        organismClassCount.put(clazz, organismClassCount.get(clazz) + 1);
    }
}
