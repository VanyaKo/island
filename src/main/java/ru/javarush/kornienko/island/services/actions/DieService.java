package ru.javarush.kornienko.island.services.actions;

import ru.javarush.kornienko.island.consts.Consts;
import ru.javarush.kornienko.island.entities.abstracts.Animal;
import ru.javarush.kornienko.island.entities.abstracts.Organism;
import ru.javarush.kornienko.island.entities.island.Cell;
import ru.javarush.kornienko.island.entities.island.Island;
import ru.javarush.kornienko.island.services.utils.MapWorker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DieService {
    private final Island island;
    private Map<Class<? extends Organism>, Long> diedOrganismClassToCountMap;
    private ConcurrentMap<Cell, Set<Organism>> cellToSurvivedAnimalsMap;

    public DieService(Island island) {
        this.island = island;
    }

    public void resetDiedAndSurvivedOrganisms() {
        diedOrganismClassToCountMap = new HashMap<>();
        cellToSurvivedAnimalsMap = new ConcurrentHashMap<>();
    }

    /**
     * Remove died animals from island and return them.
     */
    public Map<Class<? extends Organism>, Long> getDiedAnimals() {
        island.setIslandMap(cellToSurvivedAnimalsMap);
        return diedOrganismClassToCountMap;
    }

    public synchronized void killCellHungryAnimals(Map.Entry<Cell, Set<Organism>> cellToOrganismsEntry) {
        synchronized(island) {
            Set<Organism> survivedOrganisms = new HashSet<>();
            for(Organism organism : cellToOrganismsEntry.getValue()) {
                if(organism instanceof Animal animal && hasNoHealth(animal)) {
                    MapWorker.putDuplicateValueCount(diedOrganismClassToCountMap, animal.getClass());
                } else {
                    survivedOrganisms.add(organism);
                }
            }
            cellToSurvivedAnimalsMap.put(cellToOrganismsEntry.getKey(), survivedOrganisms);
        }
    }

    private synchronized boolean hasNoHealth(Animal animal) {
        return animal.decreaseHealth(Consts.PERCENT_TO_DECREASE_FROM_ANIMAL_STARVATION) <= 0;
    }
}
