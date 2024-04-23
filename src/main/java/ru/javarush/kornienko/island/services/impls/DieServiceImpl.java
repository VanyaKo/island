package ru.javarush.kornienko.island.services.impls;

import ru.javarush.kornienko.island.consts.Consts;
import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.services.DieService;
import ru.javarush.kornienko.island.services.OrganismService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DieServiceImpl implements DieService {
    private final Island island;

    public Map<Class<? extends Organism>, Long> getDiedOrganismClassToCount() {
        return new HashMap<>(diedOrganismClassToCount);
    }

    private Map<Class<? extends Organism>, Long> diedOrganismClassToCount;

    public DieServiceImpl(Island island) {
        this.island = island;
    }

    @Override
    public long killHungryIslandAnimals() {
        diedOrganismClassToCount = new HashMap<>();
        long diedFromHungerAnimalCount = 0;
        for(Map.Entry<Cell, List<Organism>> cellOrganismsEntry : island.getIslandMap().entrySet()) {
            for(Organism organism : cellOrganismsEntry.getValue()) {
                if(organism instanceof Animal animal) {
                    if(animal.decreaseHealth(Consts.PERCENT_TO_DECREASE_FROM_ANIMAL_STARVATION) <= 0) {
                        island.removeOrganismFromCell(animal, cellOrganismsEntry.getKey());
                        putDuplicateClassCount(diedOrganismClassToCount, animal.getClass());
                        diedFromHungerAnimalCount++;
                    }
                }
            }
        }
        return diedFromHungerAnimalCount;
    }
}
