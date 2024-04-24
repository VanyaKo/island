package ru.javarush.kornienko.island.services.impls;

import ru.javarush.kornienko.island.consts.Consts;
import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.services.DieService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DieServiceImpl implements DieService {
    private final Island island;

    public DieServiceImpl(Island island) {
        this.island = island;
    }

    @Override
    public Map<Class<? extends Organism>, Long> killHungryIslandAnimals() {
        Map<Class<? extends Organism>, Long> diedOrganismClassToCount = new HashMap<>();
        Map<Cell, Set<Organism>> cellToSurvivedAnimals = new HashMap<>();
        for(Map.Entry<Cell, Set<Organism>> cellToOrganismsEntry : island.getIslandMap().entrySet()) {
            Set<Organism> survivedOrganisms = new HashSet<>();
            for(Organism organism : cellToOrganismsEntry.getValue()) {
                if(organism instanceof Animal animal && (animal.decreaseHealth(Consts.PERCENT_TO_DECREASE_FROM_ANIMAL_STARVATION) <= 0)) {
                    putDuplicateValueCount(diedOrganismClassToCount, animal.getClass());
                } else {
                    survivedOrganisms.add(organism);
                }
            }
            cellToSurvivedAnimals.put(cellToOrganismsEntry.getKey(), survivedOrganisms);
        }
        island.setIslandMap(cellToSurvivedAnimals);
        return diedOrganismClassToCount;
    }
}
