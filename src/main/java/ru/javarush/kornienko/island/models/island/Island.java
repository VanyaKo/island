package ru.javarush.kornienko.island.models.island;

import ru.javarush.kornienko.island.configs.IslandConfig;
import ru.javarush.kornienko.island.configs.PrototypeFactory;
import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.plants.Plant;
import ru.javarush.kornienko.island.services.IslandAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class Island implements IslandAction {
    private static final int DEFAULT_PROPERTY_VALUE = -1;
    private final IslandConfig islandConfig;
    private final PrototypeFactory prototypeFactory;
    private Map<Cell, List<Organism>> islandMap;
    private int width = DEFAULT_PROPERTY_VALUE;
    private int height = DEFAULT_PROPERTY_VALUE;
    private int maxPlantsPerCell = DEFAULT_PROPERTY_VALUE;
    private int maxAnimalsPerCell = DEFAULT_PROPERTY_VALUE;

    public Island(IslandConfig islandConfig, PrototypeFactory prototypeFactory) {
        this.islandConfig = islandConfig;
        this.prototypeFactory = prototypeFactory;
    }

    public int getMaxPlantsPerCell() {
        return maxPlantsPerCell;
    }

    public int getMaxAnimalsPerCell() {
        return maxAnimalsPerCell;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Get copy of a map.
     */
    public Map<Cell, List<Organism>> getIslandMap() {
        return new HashMap<>(islandMap);
    }

    public void setIslandMap(Map<Cell, List<Organism>> islandMap) {
        this.islandMap = islandMap;
    }

    public void initEmptyIsland() {
        extractProperties();
        initEmptyMap();
    }

    private void extractProperties() {
        width = islandConfig.getProperty("width");
        height = islandConfig.getProperty("height");
        maxPlantsPerCell = islandConfig.getProperty("maxPlantsPerCell");
        maxAnimalsPerCell = islandConfig.getProperty("maxAnimalsPerCell");
    }

    private void initEmptyMap() {
        islandMap = new HashMap<>();
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                Cell cell = new Cell(i, j);
                islandMap.put(cell, new ArrayList<>());
            }
        }
    }

    public void placePlants() {
        for(List<Organism> cellOrganisms : islandMap.values()) {
            placeOrganismsOnCell(cellOrganisms, Plant.class, maxPlantsPerCell);
        }
    }

    public void placeAnimals() {
        for(List<Organism> cellOrganisms : islandMap.values()) {
            placeOrganismsOnCell(cellOrganisms, Animal.class, maxAnimalsPerCell);
        }
    }

    private void placeOrganismsOnCell(List<Organism> cellOrganisms, Class<? extends Organism> clazz, int maxOrganismsPerCell) {
        int currentOrganisms = getCurrentOrganismsOnCell(cellOrganisms, clazz);
        for(Organism prototype : prototypeFactory.getPrototypes()) {
            if(clazz.isAssignableFrom(prototype.getClass())) {
                int prototypesToAdd = ThreadLocalRandom.current().nextInt(prototype.getMaxCountOnCell());
                if(currentOrganisms + prototypesToAdd > maxOrganismsPerCell) {
                    break;
                }
                fillCellWithOrganisms(cellOrganisms, prototype, prototypesToAdd);
                currentOrganisms += prototypesToAdd;
            }
        }
    }

    private int getCurrentOrganismsOnCell(List<Organism> cellOrganisms, Class<? extends Organism> targetClass) {
        return (int) cellOrganisms.stream()
                .filter(organism -> organism.getClass() == targetClass).count();
    }

    private void fillCellWithOrganisms(List<Organism> cellOrganisms, Organism prototype, int prototypesPerCell) {
        for(int i = 0; i < prototypesPerCell; i++) {
            try {
                cellOrganisms.add((Organism) prototype.clone());
            } catch(CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addAnimalToCell(Animal animal, Cell cell) {
        islandMap.get(cell).add(animal);
    }

    public void removeOrganismFromCell(Organism organism, Cell cell) {
        islandMap.get(cell).remove(organism);
    }

    public List<Animal> getAnimalListFromOrganisms(Collection<Organism> organisms) {
        return organisms.stream()
                .filter(Animal.class::isInstance)
                .map(Animal.class::cast)
                .toList();
    }
}
