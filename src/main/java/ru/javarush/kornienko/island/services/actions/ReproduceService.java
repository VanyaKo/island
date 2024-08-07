package ru.javarush.kornienko.island.services.actions;

import ru.javarush.kornienko.island.configs.animals.ReproduceConfig;
import ru.javarush.kornienko.island.consts.Consts;
import ru.javarush.kornienko.island.entities.abstracts.Animal;
import ru.javarush.kornienko.island.entities.abstracts.Organism;
import ru.javarush.kornienko.island.entities.island.Cell;
import ru.javarush.kornienko.island.entities.island.Island;
import ru.javarush.kornienko.island.exceptions.AppException;
import ru.javarush.kornienko.island.services.utils.MapWorker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class ReproduceService extends ActionService {
    private final Island island;
    private final ReproduceConfig[] reproduceProbabilityEntries;

    public ReproduceService(Island island, ReproduceConfig[] reproduceProbabilityEntries) {
        this.island = island;
        this.reproduceProbabilityEntries = reproduceProbabilityEntries;
    }

    public synchronized void reproduceCellOrganisms(Map.Entry<Cell, Set<Organism>> cellOrganismsEntry) {
        synchronized(island) {
            List<Animal> animals = island.getAnimalListFromOrganisms(cellOrganismsEntry.getValue());
            int currentAnimalsCount = animals.size();
            if(currentAnimalsCount < island.getMaxAnimalsPerCell()) {
                Map<Class<? extends Animal>, Animal> classAnimals = getReproducibleAnimals(animals);
                reproduceAnimals(classAnimals, cellOrganismsEntry.getKey(), currentAnimalsCount);
            }
        }
    }

    /**
     * Get map with animal class as a key and animal as a value.
     */
    private synchronized Map<Class<? extends Animal>, Animal> getReproducibleAnimals(List<Animal> animals) {
        Map<Class<? extends Animal>, List<Animal>> classAnimals = getClassAnimals(animals);
        Map<Class<? extends Animal>, Animal> reproducibleClassAnimals = new HashMap<>();
        for(Map.Entry<Class<? extends Animal>, List<Animal>> classAnimalsEntry : classAnimals.entrySet()) {
            if(pairExists(classAnimalsEntry)) {
                int randomAnimalIndex = ThreadLocalRandom.current().nextInt(classAnimalsEntry.getValue().size());
                reproducibleClassAnimals.put(classAnimalsEntry.getKey(), classAnimalsEntry.getValue().get(randomAnimalIndex));
            }
        }
        return reproducibleClassAnimals;
    }

    private boolean pairExists(Map.Entry<Class<? extends Animal>, List<Animal>> classAnimalsEntry) {
        return classAnimalsEntry.getValue().size() >= Consts.MIN_NUMBER_OF_ANIMALS_TO_REPRODUCE;
    }

    /**
     * Get map with animal class as a key and list of animals as a value
     */
    private synchronized Map<Class<? extends Animal>, List<Animal>> getClassAnimals(List<Animal> animals) {
        Map<Class<? extends Animal>, List<Animal>> classAnimals = new HashMap<>();
        for(Animal animal : animals) {
            Class<? extends Animal> animalClass = animal.getClass();
            classAnimals.putIfAbsent(animalClass, new ArrayList<>());
            classAnimals.get(animalClass).add(animal);
        }
        return classAnimals;
    }

    private synchronized void reproduceAnimals(Map<Class<? extends Animal>, Animal> classAnimals, Cell cell, int currentAnimalCount) {
        for(Map.Entry<Class<? extends Animal>, Animal> classAnimalEntry : classAnimals.entrySet()) {
            ReproduceConfig reproduceConfig = getEntryByClass(classAnimalEntry.getKey());
            if(isSuccessProbabilityToReproduce(reproduceConfig)) {
                Set<Animal> newborns = classAnimalEntry.getValue().reproduce(reproduceConfig.maxCubs());
                for(Animal newborn : newborns) {
                    island.addAnimalToCell(newborn, cell);
                    MapWorker.putDuplicateValueToCountEntry(organismClassCountMap, newborn.getClass());
                    if(++currentAnimalCount >= island.getMaxAnimalsPerCell()) {
                        return;
                    }
                }
            }
        }
    }

    private synchronized boolean isSuccessProbabilityToReproduce(ReproduceConfig reproduceConfig) {
        int currentCouplingProbability = ThreadLocalRandom.current().nextInt(Consts.HUNDRED_PERCENT + 1);
        int currentBirthProbability = ThreadLocalRandom.current().nextInt(Consts.HUNDRED_PERCENT + 1);
        return currentCouplingProbability <= reproduceConfig.couplingProbability()
               && currentBirthProbability <= reproduceConfig.birthProbability();
    }

    private synchronized ReproduceConfig getEntryByClass(Class<? extends Animal> clazz) {
        return Arrays.stream(reproduceProbabilityEntries)
                .filter(entry -> entry.reproducer() == clazz)
                .findAny()
                .orElseThrow(() -> new AppException("No reproducer for " + clazz + " class"));
    }
}
