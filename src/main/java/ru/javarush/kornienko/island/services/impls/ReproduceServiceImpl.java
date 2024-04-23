package ru.javarush.kornienko.island.services.impls;

import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.services.ReproduceService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ReproduceServiceImpl implements ReproduceService {
    private static final int MIN_NUMBER_OF_ANIMALS_TO_REPRODUCE = 2;
    private final Island island;

    public ReproduceServiceImpl(Island island) {
        this.island = island;
    }

    public void reproduceIslandAnimals() {
        for(Map.Entry<Cell, List<Organism>> cellOrganismsEntry : island.getIslandMap().entrySet()) {
            processCell(cellOrganismsEntry);
        }
    }

    private void processCell(Map.Entry<Cell, List<Organism>> cellOrganismsEntry) {
        List<Animal> animals = island.getAnimalListFromOrganisms(cellOrganismsEntry.getValue());
        int currentAnimalsCount = animals.size();
        if(currentAnimalsCount < island.getMaxAnimalsPerCell()) {
            Map<Class<? extends Animal>, Animal> classAnimals = getMapFromList(animals);
            reproduceAnimals(classAnimals, cellOrganismsEntry.getKey(), currentAnimalsCount);
        }
    }

    /**
     * Get map with animal class as a key and animal as a value.
     * @param animals
     * @return
     */
    private Map<Class<? extends Animal>, Animal> getMapFromList(List<Animal> animals) {
        Map<Class<? extends Animal>, List<Animal>> classAnimals = getClassAnimals(animals);
        Map<Class<? extends Animal>, Animal> reproducibleClassAnimals = new HashMap<>();
        for(Map.Entry<Class<? extends Animal>, List<Animal>> classAnimalsEntry : classAnimals.entrySet()) {
            if(classAnimalsEntry.getValue().size() >= MIN_NUMBER_OF_ANIMALS_TO_REPRODUCE) {
                int randomAnimalIndex = ThreadLocalRandom.current().nextInt(classAnimalsEntry.getValue().size());
                reproducibleClassAnimals.put(classAnimalsEntry.getKey(), classAnimalsEntry.getValue().get(randomAnimalIndex));
            }
        }
        return reproducibleClassAnimals;
    }

    /**
     * Get map with animal class as a key and list of animals as a value
     * @param animals
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
            Animal newborn = classAnimalEntry.getValue().reproduce();
            island.addAnimalToCell(newborn, cell);
            if(++currentAnimalCount >= island.getMaxAnimalsPerCell()) {
                return;
            }
        }
    }
}
