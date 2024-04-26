package ru.javarush.kornienko.island.services;

import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.services.utils.MapWorker;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CollectClassesService {
    private final Island island;

    public CollectClassesService(Island island) {
        this.island = island;
    }

    public Map<Class<? extends Organism>, Long> getClassesToCountMap() {
        synchronized(island) {
            Map<Class<? extends Organism>, Long> organismClassesToCount = new HashMap<>();
            for(Map.Entry<Cell, Set<Organism>> cellToOrganismsEntry : island.getIslandMap().entrySet()) {
                for(Organism organism : cellToOrganismsEntry.getValue()) {
                    MapWorker.putDuplicateValueCount(organismClassesToCount, organism.getClass());
                }
            }
            return organismClassesToCount;
        }
    }
}
