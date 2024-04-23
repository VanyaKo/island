package ru.javarush.kornienko.island.services.impls;

import ru.javarush.kornienko.island.configs.ProbabilityPair;
import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.services.EatService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class EatServiceImpl implements EatService {
    private final Island island;
    private final ProbabilityPair[] probabilityPairs;

    public EatServiceImpl(Island island, ProbabilityPair[] probabilityPairs) {
        this.island = island;
        this.probabilityPairs = probabilityPairs;
    }

    /**
     *
     * @return map of eaten organisms to remove
     */
    public long eatIslandOrganisms() {
        long eatenOrganismCount = 0;
        Map<Cell, List<Organism>> eatenIslandOrganisms = Collections.emptyMap();
        for(Map.Entry<Cell, List<Organism>> cellOrganismsEntry : island.getIslandMap().entrySet()) {
            eatenOrganismCount += eatCellAnimals(cellOrganismsEntry);
        }
        return eatenOrganismCount;
    }

    private long eatCellAnimals(Map.Entry<Cell, List<Organism>> cellOrganismsEntry) {
        long eatenOrganismCount = 0;
        List<Class<? extends Animal>> eaterClasses = new ArrayList<>();
        List<Organism> eatenOrganisms = new ArrayList<>();
        List<Organism> organisms = cellOrganismsEntry.getValue();
        for(Organism organism : organisms) {
            if(organism instanceof Animal animal && !eaterClasses.contains(animal.getClass()) && !eatenOrganisms.contains(animal)) {
                Map<Class<?>, Byte> eatableClasses = getEatablesByEaterClass(animal.getClass());
                List<Organism> eatableOrganisms = organisms.stream()
                        .filter(cellOrganism -> eatableClasses.containsKey(cellOrganism.getClass()))
                        .toList();
                if(eatableOrganisms.isEmpty()) {
                    continue;
                }
                Organism randomEatableOrganism = getRandomEatableOrganism(eatableOrganisms);
                byte eatingProbability = getEatingProbabilityByOrganism(randomEatableOrganism, eatableClasses);
                if(animal.eat(randomEatableOrganism, eatingProbability)) {
                    eaterClasses.add(animal.getClass());
                    eatenOrganisms.add(randomEatableOrganism);
                    eatenOrganismCount++;
                }
            }
        }
        eatenOrganisms.forEach(organism -> island.removeOrganismFromCell(organism, cellOrganismsEntry.getKey()));
        return eatenOrganismCount;
    }

    private Organism getRandomEatableOrganism(List<Organism> eatableOrganisms) {
        return eatableOrganisms.get(ThreadLocalRandom.current().nextInt(eatableOrganisms.size()));
    }

    private Class<?> getRandomEatableClass(Set<Class<?>> eatableClasses) {
        return (Class<?>) eatableClasses.toArray()[ThreadLocalRandom.current().nextInt(eatableClasses.size())];
    }

    private Organism getRandomEatableOrganismByClass(Class<?> clazz, List<Organism> organisms) {
        return organisms.stream()
                .filter(organism -> organism.getClass() == clazz)
                .findAny()
                .orElse(null);
//                .orElseThrow(() -> new RuntimeException("No eatable organisms with " + clazz + " class"));
    }

    private byte getEatingProbabilityByOrganism(Organism organism, Map<Class<?>, Byte> eatableClasses) {
        return eatableClasses.entrySet().stream()
                .filter(classByteEntry -> classByteEntry.getKey() == organism.getClass())
                .findAny()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new RuntimeException("Cannot find probability of being eaten for " + organism + " organism."));
    }

    private Map<Class<?>, Byte> getEatablesByEaterClass(Class<? extends Animal> eaterClass) {
        for(ProbabilityPair probabilityPair : probabilityPairs) {
            if(probabilityPair.getEater() == eaterClass) {
                return probabilityPair.getEatables();
            }
        }
        throw new RuntimeException(eaterClass + "is not eater!");
    }
}
