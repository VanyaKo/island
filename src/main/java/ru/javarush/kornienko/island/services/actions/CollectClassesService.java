package ru.javarush.kornienko.island.services.actions;

import ru.javarush.kornienko.island.entities.abstracts.Organism;
import ru.javarush.kornienko.island.entities.island.Cell;
import ru.javarush.kornienko.island.entities.island.Island;
import ru.javarush.kornienko.island.services.utils.MapWorker;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CollectClassesService {
    private final Island island;

    public CollectClassesService(Island island) {
        this.island = island;
    }

    public synchronized Map<Class<? extends Organism>, Long> getClassesToCountMap() {
        synchronized(island) {
            Map<Class<? extends Organism>, Long> organismClassesToCount = new HashMap<>();
            for(Map.Entry<Cell, Set<Organism>> cellToOrganismsEntry : island.getIslandMap().entrySet()) {
                for(Organism organism : cellToOrganismsEntry.getValue()) {
                    MapWorker.putDuplicateValueToCountEntry(organismClassesToCount, organism.getClass());
                }
            }
            return organismClassesToCount;
        }
    }
}
