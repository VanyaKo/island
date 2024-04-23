package ru.javarush.kornienko.island.services.impls;

import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ReproduceService {
    /**
     * Each king of animals can reproduce only one child per move (за один ход игры)
     * @param islandMap
     * @param maxAnimalsPerCell
     */
    public void reproduceAnimalsOnIsland(Map<Cell, List<Organism>> islandMap, int maxAnimalsPerCell) {
        for(Map.Entry<Cell, List<Organism>> islandEntry : islandMap.entrySet()) {
            List<Animal> animalsOnCell = getAnimalsOnCell(islandEntry.getValue());
            int animalsOnCellNumber = animalsOnCell.size();
            if(animalsOnCellNumber < maxAnimalsPerCell) {
                Map<Class<? extends Animal>, Animal> classAnimals = getMapFromList(animalsOnCell);
                reproduceAnimalsOnCell(classAnimals, animalsOnCellNumber, maxAnimalsPerCell);
            }
        }
    }

    private void reproduceAnimalsOnCell(Map<Class<? extends Animal>, Animal> classAnimals, int animalsOnCellNumber, int maxAnimalsPerCell) {
        for(Map.Entry<Class<? extends Animal>, Animal> classAnimalEntry : classAnimals.entrySet()) {
            Organism reproduce = classAnimalEntry.getValue().reproduce(); // TODO
            if(++animalsOnCellNumber >= maxAnimalsPerCell) {
                return;
            }
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
            if(classAnimalsEntry.getValue().size() < 2) {
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

    private List<Animal> getAnimalsOnCell(List<Organism> organismsOnCell) {
        return organismsOnCell.stream()
                .filter(organism -> organism instanceof Animal)
                .map(organism -> (Animal) organism)
                .toList();
    }
}
