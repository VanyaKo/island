package ru.javarush.kornienko.island.services.impls;

import ru.javarush.kornienko.island.configs.action.ReproduceProbabilityEntry;
import ru.javarush.kornienko.island.consts.Consts;
import ru.javarush.kornienko.island.exceptions.AppException;
import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.services.ReproduceService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ReproduceServiceImpl implements ReproduceService {
    private final Island island;
    private final ReproduceProbabilityEntry[] reproduceProbabilityEntries;
    private Map<Class<? extends Organism>, Long> newbornAnimalClassCount;

    public ReproduceServiceImpl(Island island, ReproduceProbabilityEntry[] reproduceProbabilityEntries) {
        this.island = island;
        this.reproduceProbabilityEntries = reproduceProbabilityEntries;
    }

    @Override
    public Map<Class<? extends Organism>, Long> reproduceIslandAnimals() {
        newbornAnimalClassCount = new HashMap<>();
        for(Map.Entry<Cell, Set<Organism>> cellOrganismsEntry : island.getIslandMap().entrySet()) {
            processCell(cellOrganismsEntry);
        }
        return newbornAnimalClassCount;
    }

    private void processCell(Map.Entry<Cell, Set<Organism>> cellOrganismsEntry) {
        List<Animal> animals = island.getAnimalListFromOrganisms(cellOrganismsEntry.getValue());
        int currentAnimalsCount = animals.size();
        if(currentAnimalsCount < island.getMaxAnimalsPerCell()) {
            Map<Class<? extends Animal>, Animal> classAnimals = getMapFromList(animals);
            reproduceAnimals(classAnimals, cellOrganismsEntry.getKey(), currentAnimalsCount);
        }
    }

    /**
     * Get map with animal class as a key and animal as a value.
     */
    private Map<Class<? extends Animal>, Animal> getMapFromList(List<Animal> animals) {
        Map<Class<? extends Animal>, List<Animal>> classAnimals = getClassAnimals(animals);
        Map<Class<? extends Animal>, Animal> reproducibleClassAnimals = new HashMap<>();
        for(Map.Entry<Class<? extends Animal>, List<Animal>> classAnimalsEntry : classAnimals.entrySet()) {
            if(classAnimalsEntry.getValue().size() >= Consts.MIN_NUMBER_OF_ANIMALS_TO_REPRODUCE) {
                int randomAnimalIndex = ThreadLocalRandom.current().nextInt(classAnimalsEntry.getValue().size());
                reproducibleClassAnimals.put(classAnimalsEntry.getKey(), classAnimalsEntry.getValue().get(randomAnimalIndex));
            }
        }
        return reproducibleClassAnimals;
    }

    /**
     * Get map with animal class as a key and list of animals as a value
     */
    private Map<Class<? extends Animal>, List<Animal>> getClassAnimals(List<Animal> animals) {
        Map<Class<? extends Animal>, List<Animal>> classAnimals = new HashMap<>();
        for(Animal animal : animals) {
            Class<? extends Animal> animalClass = animal.getClass();
            classAnimals.putIfAbsent(animalClass, new ArrayList<>());
            List<Animal> currentClassAnimals = classAnimals.get(animalClass);
            currentClassAnimals.add(animal);
            classAnimals.replace(animalClass, currentClassAnimals);
        }
        return classAnimals;
    }

    private void reproduceAnimals(Map<Class<? extends Animal>, Animal> classAnimals, Cell cell, int currentAnimalCount) {
        for(Map.Entry<Class<? extends Animal>, Animal> classAnimalEntry : classAnimals.entrySet()) {
            ReproduceProbabilityEntry reproduceProbabilityEntry = getEntryByClass(classAnimalEntry.getKey());
            if(isSuccessProbabilityToReproduce(reproduceProbabilityEntry)) {
                Set<Animal> newborns = classAnimalEntry.getValue().reproduce(reproduceProbabilityEntry.getMaxCubs());
                for(Animal newborn : newborns) {
                    island.addAnimalToCell(newborn, cell);
                    putDuplicateValueCount(newbornAnimalClassCount, newborn.getClass());
                }
                if(++currentAnimalCount >= island.getMaxAnimalsPerCell()) {
                    return;
                }
            }
        }
    }

    private boolean isSuccessProbabilityToReproduce(ReproduceProbabilityEntry reproduceProbabilityEntry) {
        int currentCouplingProbability = ThreadLocalRandom.current().nextInt(Consts.HUNDRED_PERCENT + 1);
        int currentBirthProbability = ThreadLocalRandom.current().nextInt(Consts.HUNDRED_PERCENT + 1);
        return currentCouplingProbability <= reproduceProbabilityEntry.getCouplingProbability()
               && currentBirthProbability <= reproduceProbabilityEntry.getBirthProbability();
    }

    private ReproduceProbabilityEntry getEntryByClass(Class<? extends Animal> clazz) {
        return Arrays.stream(reproduceProbabilityEntries)
                .filter(entry -> entry.getReproducer() == clazz)
                .findAny()
                .orElseThrow(() -> new AppException("No reproducer for " + clazz + " class"));
    }
}
