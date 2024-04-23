package ru.javarush.kornienko.island.services.impls;

import ru.javarush.kornienko.island.consts.Consts;
import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.services.DieService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DieServiceImpl implements DieService {
    private final Island island;
    private Map<Class<? extends Organism>, Long> diedOrganismClassToCount;

    public DieServiceImpl(Island island) {
        this.island = island;
    }

    public Map<Class<? extends Organism>, Long> getDiedOrganismClassToCount() {
        return new HashMap<>(diedOrganismClassToCount);
    }

    @Override
    public long killHungryIslandAnimals() {
        diedOrganismClassToCount = new HashMap<>();
        Map<Cell, List<Organism>> cellToSurvivedAnimals = new HashMap<>();
        long diedFromHungerAnimalCount = 0;
        for(Map.Entry<Cell, List<Organism>> cellToOrganismsEntry : island.getIslandMap().entrySet()) {
            List<Organism> survivedOrganisms = new ArrayList<>();
            for(Organism organism : cellToOrganismsEntry.getValue()) {
                if(organism instanceof Animal animal && (animal.decreaseHealth(Consts.PERCENT_TO_DECREASE_FROM_ANIMAL_STARVATION) <= 0)) {
                    putDuplicateValueCount(diedOrganismClassToCount, animal.getClass());
                    diedFromHungerAnimalCount++;
                } else {
                    survivedOrganisms.add(organism);
                }
            }
            cellToSurvivedAnimals.put(cellToOrganismsEntry.getKey(), survivedOrganisms);
        }
        island.setIslandMap(cellToSurvivedAnimals);
        return diedFromHungerAnimalCount;
    }
}
