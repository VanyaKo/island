package ru.javarush.kornienko.island.services.impls;

import ru.javarush.kornienko.island.configs.EatProbabilityPair;
import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.services.EatService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class EatServiceImpl implements EatService {
    private final Island island;
    private final EatProbabilityPair[] eatProbabilityPairs;
    private Map<Class<? extends Organism>, Long> eatenOrganismClassCount;

    public EatServiceImpl(Island island, EatProbabilityPair[] eatProbabilityPairs) {
        this.island = island;
        this.eatProbabilityPairs = eatProbabilityPairs;
    }

    /**
     * @return map of eaten organisms to remove
     */
    @Override
    public Map<Class<? extends Organism>, Long> eatIslandOrganisms() {
        eatenOrganismClassCount = new HashMap<>();
        for(Map.Entry<Cell, List<Organism>> cellOrganismsEntry : island.getIslandMap().entrySet()) {
            eatCellAnimals(cellOrganismsEntry);
        }
        return eatenOrganismClassCount;
    }

    private void eatCellAnimals(Map.Entry<Cell, List<Organism>> cellOrganismsEntry) {
        List<Animal> eaters = new ArrayList<>();
        List<Organism> eatenOrganisms = new ArrayList<>();
        List<Organism> organisms = cellOrganismsEntry.getValue();
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
                    putDuplicateValueCount(eatenOrganismClassCount, randomEatableOrganism.getClass());
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
                .orElseThrow(() -> new RuntimeException("Cannot find probability of being eaten for " + organism + " organism."));
    }

    private Map<Class<?>, Byte> getEatablesByEaterClass(Class<? extends Animal> eaterClass) {
        for(EatProbabilityPair eatProbabilityPair : eatProbabilityPairs) {
            if(eatProbabilityPair.getEater() == eaterClass) {
                return eatProbabilityPair.getEatables();
            }
        }
        throw new RuntimeException(eaterClass + "is not eater!");
    }
}
