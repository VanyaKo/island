package ru.javarush.kornienko.island.services;

import ru.javarush.kornienko.island.configs.animals.EatConfig;
import ru.javarush.kornienko.island.exceptions.AppException;
import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.services.utils.MapWorker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class EatService {
    private final Island island;
    private final EatConfig[] eatConfigs;
    private Map<Class<? extends Organism>, Long> eatenOrganismClassCount;

    public EatService(Island island, EatConfig[] eatConfigs) {
        this.island = island;
        this.eatConfigs = eatConfigs;
    }

    /**
     * @return map of eaten organisms to remove
     */
    public Map<Class<? extends Organism>, Long> eatIslandOrganisms() {
        eatenOrganismClassCount = new HashMap<>();
        for(Map.Entry<Cell, Set<Organism>> cellOrganismsEntry : island.getIslandMap().entrySet()) {
            eatCellAnimals(cellOrganismsEntry);
        }
        return eatenOrganismClassCount;
    }

    private void eatCellAnimals(Map.Entry<Cell, Set<Organism>> cellOrganismsEntry) {
        Set<Animal> eaters = new HashSet<>();
        Set<Organism> eatenOrganisms = new HashSet<>();
        Set<Organism> organisms = cellOrganismsEntry.getValue();
        for(Organism organism : organisms) {
            if(organism instanceof Animal animal && !eaters.contains(animal) && !eatenOrganisms.contains(animal)) {
                Map<Class<?>, Byte> eatableClasses = getEatablesByEaterClass(animal.getClass());
                List<Organism> eatableOrganisms = organisms.stream()
                        .filter(cellOrganism -> eatableClasses.containsKey(cellOrganism.getClass()))
                        .filter(cellOrganism -> !eatenOrganisms.contains(cellOrganism))
                        .toList();
                if(eatableOrganisms.isEmpty()) {
                    continue;
                }
                Organism randomEatableOrganism = getRandomEatableOrganism(eatableOrganisms);
                byte eatingProbability = getEatingProbabilityByOrganism(randomEatableOrganism, eatableClasses);
                if(animal.eat(randomEatableOrganism, eatingProbability)) {
                    eaters.add(animal);
                    eatenOrganisms.add(randomEatableOrganism);
                    MapWorker.putDuplicateValueCount(eatenOrganismClassCount, randomEatableOrganism.getClass());
                }
            }
        }
        eatenOrganisms.forEach(organism -> island.removeOrganismFromCell(organism, cellOrganismsEntry.getKey()));
    }

    private Organism getRandomEatableOrganism(List<Organism> eatableOrganisms) {
        return eatableOrganisms.get(ThreadLocalRandom.current().nextInt(eatableOrganisms.size()));
    }

    private byte getEatingProbabilityByOrganism(Organism organism, Map<Class<?>, Byte> eatableClasses) {
        return eatableClasses.entrySet().stream()
                .filter(classByteEntry -> classByteEntry.getKey() == organism.getClass())
                .findAny()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new AppException("Cannot find probability of being eaten for " + organism + " organism."));
    }

    private Map<Class<?>, Byte> getEatablesByEaterClass(Class<? extends Animal> eaterClass) {
        for(EatConfig eatConfig : eatConfigs) {
            if(eatConfig.eater() == eaterClass) {
                return eatConfig.eatables();
            }
        }
        throw new AppException(eaterClass + "is not eater");
    }
}
