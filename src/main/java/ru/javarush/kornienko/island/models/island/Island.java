package ru.javarush.kornienko.island.models.island;

import ru.javarush.kornienko.island.configs.IslandConfig;
import ru.javarush.kornienko.island.configs.PrototypeFactory;
import ru.javarush.kornienko.island.exceptions.AppException;
import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.plants.Plant;
import ru.javarush.kornienko.island.services.utils.MapWorker;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Island {
    private static final int DEFAULT_PROPERTY_VALUE = -1;
    private final IslandConfig islandConfig;
    private final PrototypeFactory prototypeFactory;
    private ConcurrentMap<Cell, Set<Organism>> islandMap;
    private int width = DEFAULT_PROPERTY_VALUE;
    private int height = DEFAULT_PROPERTY_VALUE;
    private int maxPlantsPerCell = DEFAULT_PROPERTY_VALUE;
    private int maxAnimalsPerCell = DEFAULT_PROPERTY_VALUE;
    private int cycleDuration = DEFAULT_PROPERTY_VALUE;
    private Map<Class<? extends Organism>, Long> grownPlantClassToCount;

    public Island(IslandConfig islandConfig, PrototypeFactory prototypeFactory) {
        this.islandConfig = islandConfig;
        this.prototypeFactory = prototypeFactory;
    }

    public int getCellCount() {
        return width * height;
    }

    public int getMaxAnimalsPerCell() {
        return maxAnimalsPerCell;
    }

    /**
     * Get copy of a map.
     */
    public synchronized Map<Cell, Set<Organism>> getIslandMap() {
        return new HashMap<>(islandMap);
    }

    public synchronized void setIslandMap(ConcurrentMap<Cell, Set<Organism>> islandMap) {
        this.islandMap = islandMap;
    }

    public void initEmptyIsland() {
        extractProperties();
        initEmptyMap();
        resetGrownOrganismsMap();
    }

    private void extractProperties() {
        width = islandConfig.getProperty("width");
        height = islandConfig.getProperty("height");
        maxPlantsPerCell = islandConfig.getProperty("maxPlantsPerCell");
        maxAnimalsPerCell = islandConfig.getProperty("maxAnimalsPerCell");
        cycleDuration = islandConfig.getProperty("cycleDuration");
    }

    private void initEmptyMap() {
        islandMap = new ConcurrentHashMap<>();
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                Cell cell = new Cell(i, j);
                islandMap.put(cell, new HashSet<>());
            }
        }
    }

    public Map<Class<? extends Organism>, Long> getGrownPlantClassToCount() {
        return new HashMap<>(grownPlantClassToCount);
    }

    public void resetGrownOrganismsMap() {
        grownPlantClassToCount = new ConcurrentHashMap<>();
    }

    public synchronized void growPlants() {
        for(Set<Organism> cellOrganisms : islandMap.values()) {
            long currentOrganisms = countClassesByFilter(cellOrganisms, Plant.class);
            for(Organism prototype : prototypeFactory.getPrototypes()) {
                if(Plant.class.isAssignableFrom(prototype.getClass())) {
                    int prototypesToAdd = getPrototypesToAdd(Collections.emptyMap(), prototype);
                    if(currentOrganisms + prototypesToAdd > maxPlantsPerCell) {
                        break;
                    }
                    fillCellWithOrganisms(cellOrganisms, prototype, prototypesToAdd, Plant.class);
                    currentOrganisms += prototypesToAdd;
                }
            }
        }
    }

    public void initAnimals(Map<Class<?>, Integer> startAnimalNumConfig) {
        for(Set<Organism> cellOrganisms : islandMap.values()) {
            long currentOrganisms = countClassesByFilter(cellOrganisms, Animal.class);
            for(Organism prototype : prototypeFactory.getPrototypes()) {
                if(Animal.class.isAssignableFrom(prototype.getClass())) {
                    int prototypesToAdd = getPrototypesToAdd(startAnimalNumConfig, prototype);
                    if(currentOrganisms + prototypesToAdd > maxAnimalsPerCell) {
                        break;
                    }
                    fillCellWithOrganisms(cellOrganisms, prototype, prototypesToAdd, Animal.class);
                    currentOrganisms += prototypesToAdd;
                }
            }
        }
    }

    private synchronized int getPrototypesToAdd(Map<Class<?>, Integer> startAnimalNumConfig, Organism prototype) {
        if(startAnimalNumConfig.isEmpty()) {
            return ThreadLocalRandom.current().nextInt(prototype.getMaxCountOnCell());
        } else {
            return startAnimalNumConfig.get(prototype.getClass());
        }
    }

    private synchronized long countClassesByFilter(Set<Organism> cellOrganisms, Class<? extends Organism> targetClass) {
        return cellOrganisms.stream()
                .filter(organism -> targetClass.isAssignableFrom(organism.getClass()))
                .count();
    }

    private synchronized void fillCellWithOrganisms(Set<Organism> cellOrganisms, Organism prototype, int prototypesPerCell, Class<? extends Organism> clazz) {
        for(int i = 0; i < prototypesPerCell; i++) {
            try {
                cellOrganisms.add((Organism) prototype.clone());
                if(clazz == Plant.class) {
                    MapWorker.putDuplicateValueCount(grownPlantClassToCount, prototype.getClass());
                }
            } catch(CloneNotSupportedException e) {
                throw new AppException(e);
            }
        }
    }

    public synchronized void addAnimalToCell(Animal animal, Cell cell) {
        islandMap.get(cell).add(animal);
    }

    public synchronized void removeOrganismFromCell(Organism organism, Cell cell) {
        islandMap.get(cell).remove(organism);
    }

    public synchronized List<Animal> getAnimalListFromOrganisms(Collection<Organism> organisms) {
        return organisms.stream()
                .filter(Animal.class::isInstance)
                .map(Animal.class::cast)
                .toList();
    }

    public int getCycleDuration() {
        return cycleDuration;
    }

    public synchronized long countPlants() {
        return countClassesByFilter(islandMap.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet()), Plant.class);
    }
}
