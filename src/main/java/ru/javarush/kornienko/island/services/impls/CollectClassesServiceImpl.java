package ru.javarush.kornienko.island.services.impls;

import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.services.CollectClassesService;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CollectClassesServiceImpl implements CollectClassesService {
    private final Island island;

    public CollectClassesServiceImpl(Island island) {
        this.island = island;
    }

    @Override
    public Map<Class<? extends Organism>, Long> getClassesToCountMap() {
        Map<Class<? extends Organism>, Long> organismClassesToCount = new HashMap<>();
        for(Map.Entry<Cell, Set<Organism>> cellToOrganismsEntry : island.getIslandMap().entrySet()) {
            for(Organism organism : cellToOrganismsEntry.getValue()) {
                putDuplicateValueCount(organismClassesToCount, organism.getClass());
            }
        }
        return organismClassesToCount;
    }
}
