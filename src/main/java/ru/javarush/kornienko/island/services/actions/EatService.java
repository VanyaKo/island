package ru.javarush.kornienko.island.services.actions;

import ru.javarush.kornienko.island.configs.animals.EatConfig;
import ru.javarush.kornienko.island.entities.abstracts.Animal;
import ru.javarush.kornienko.island.entities.abstracts.Organism;
import ru.javarush.kornienko.island.entities.island.Cell;
import ru.javarush.kornienko.island.entities.island.Island;
import ru.javarush.kornienko.island.exceptions.AppException;
import ru.javarush.kornienko.island.services.utils.MapWorker;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;

public class EatService {
    private final Island island;
    private final EatConfig[] eatConfigs;
    private ConcurrentMap<Class<? extends Organism>, Long> eatenOrganismClassCount;

    public EatService(Island island, EatConfig[] eatConfigs) {
        this.island = island;
        this.eatConfigs = eatConfigs;
    }

    public ConcurrentMap<Class<? extends Organism>, Long> getEatenOrganismClassCount() {
        return new ConcurrentHashMap<>(eatenOrganismClassCount);
    }

    public void resetEatenOrganismsMap() {
        eatenOrganismClassCount = new ConcurrentHashMap<>();
    }

    public synchronized void eatCellAnimals(Map.Entry<Cell, Set<Organism>> cellToOrganismsEntry) {
        synchronized(island) {
            Set<Animal> eaters = new HashSet<>();
            Set<Organism> eatenOrganisms = new HashSet<>();
            Set<Organism> organisms = cellToOrganismsEntry.getValue();
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
            eatenOrganisms.forEach(organism -> island.removeOrganismFromCell(organism, cellToOrganismsEntry.getKey()));
        }
    }

    private synchronized Organism getRandomEatableOrganism(List<Organism> eatableOrganisms) {
        return eatableOrganisms.get(ThreadLocalRandom.current().nextInt(eatableOrganisms.size()));
    }

    private synchronized byte getEatingProbabilityByOrganism(Organism organism, Map<Class<?>, Byte> eatableClasses) {
        return eatableClasses.entrySet().stream()
                .filter(classByteEntry -> classByteEntry.getKey() == organism.getClass())
                .findAny()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new AppException("Cannot find probability of being eaten for " + organism + " organism."));
    }

    private synchronized Map<Class<?>, Byte> getEatablesByEaterClass(Class<? extends Animal> eaterClass) {
        for(EatConfig eatConfig : eatConfigs) {
            if(eatConfig.eater() == eaterClass) {
                return eatConfig.eatables();
            }
        }
        throw new AppException(eaterClass + "is not eater");
    }
}
