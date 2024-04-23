package ru.javarush.kornienko.island.services.impls;

import ru.javarush.kornienko.island.configs.ProbabilityPair;
import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.services.EatService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class EatServiceImpl implements EatService {
    private final Island island;

    public EatServiceImpl(Island island) {
        this.island = island;
    }

    /**
     * @param probabilityPairs
     * @return map of eaten organisms to remove
     */
    public void eat(ProbabilityPair[] probabilityPairs) {
        Map<Cell, List<Organism>> eatenIslandOrganisms = Collections.emptyMap();
        for(Map.Entry<Cell, List<Organism>> cellOrganismListEntry : island.getIslandMap().entrySet()) {
            List<Organism> eatenOrganismPerCell = Collections.emptyList();
            for(Organism organism : cellOrganismListEntry.getValue()) {
                if(organism instanceof Animal animal) {
                    Map<Class<?>, Byte> eatableClasses = getEatableClassesByEater(probabilityPairs, animal.getClass());
                    List<Organism> eatableOrganisms = cellOrganismListEntry.getValue().stream()
                            .filter(eatableOrganism -> eatableClasses.containsKey(eatableOrganism.getClass()))
                            .toList();

                    Organism randomEatableOrganism = getRandomEatableOrganism(eatableOrganisms, eatableClasses);
                    byte eatingProbability = getEatingProbabilityByOrganism(randomEatableOrganism, eatableClasses);
                    if(animal.eat(randomEatableOrganism, eatingProbability)) {
                        eatenOrganismPerCell.add(randomEatableOrganism);
                    }
                }
            }
            eatenIslandOrganisms.put(cellOrganismListEntry.getKey(), eatenOrganismPerCell); // TODO
        }
    }

    private  byte getEatingProbabilityByOrganism(Organism randomEatableOrganism, Map<Class<?>, Byte> eatableClasses) {
        for(Map.Entry<Class<?>, Byte> classByteEntry : eatableClasses.entrySet()) {
            if(classByteEntry.getKey() == randomEatableOrganism.getClass()) {
                return classByteEntry.getValue();
            }
        }
        throw new RuntimeException("KEK");
    }

    private  Organism getRandomEatableOrganism(List<Organism> eatableOrganisms, Map<Class<?>, Byte> eatableClasses) {
        Class<?> randomEatableClass = getRandomEatableClass(eatableClasses.keySet());
        return getRandomEatableEntryByClass(randomEatableClass, eatableOrganisms);
    }

    private  Organism getRandomEatableEntryByClass(Class<?> randomEatableClass, List<Organism> eatableOrganisms) {
        for(Organism eatableOrganism : eatableOrganisms) {
            if(eatableOrganism.getClass() == randomEatableClass) {
                return eatableOrganism;
            }
        }
        throw new RuntimeException("No eatable organisms with " + randomEatableClass + " class");
    }

    private  Class<?> getRandomEatableClass(Set<Class<?>> eatableClasses) {
        return (Class<?>) eatableClasses.toArray()[ThreadLocalRandom.current().nextInt(eatableClasses.size())];
    }

    private  Map<Class<?>, Byte> getEatableClassesByEater(ProbabilityPair[] probabilityPairs, Class<? extends Animal> eaterClass) {
        for(ProbabilityPair probabilityPair : probabilityPairs) {
            if(probabilityPair.getEater() == eaterClass) {
                return probabilityPair.getEatables();
            }
        }
        throw new RuntimeException(eaterClass + "is not eater!");
    }
}
