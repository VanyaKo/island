package ru.javarush.kornienko.island.services.actions;

import ru.javarush.kornienko.island.entities.abstracts.Organism;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class ActionService {
    protected ConcurrentMap<Class<? extends Organism>, Long> organismClassCountMap;

    public void resetOrganismClassToCountMap() {
        this.organismClassCountMap = new ConcurrentHashMap<>();
    }

    public Map<Class<? extends Organism>, Long> getOrganismClassCountMap() {
        return Collections.unmodifiableMap(organismClassCountMap);
    }
}
