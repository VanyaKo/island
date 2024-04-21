package ru.javarush.kornienko.island.models.island;

import ru.javarush.kornienko.island.configs.IslandConfig;
import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.plants.Plant;
import ru.javarush.kornienko.island.models.plants.Plants;
import ru.javarush.kornienko.island.services.IslandAction;
import ru.javarush.kornienko.island.services.PrototypeFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Island implements IslandAction {
    private static final int DEFAULT_PROPERTY_VALUE = -1;
    private IslandConfig islandConfig;
    private PrototypeFactory prototypeFactory;
    private Map<Cell, List<Organism>> islandMap;
    private int width = DEFAULT_PROPERTY_VALUE;
    private int height = DEFAULT_PROPERTY_VALUE;
    private int maxPlantsPerCell = DEFAULT_PROPERTY_VALUE;
    private int maxAnimalsPerCell = DEFAULT_PROPERTY_VALUE;

    public int getMaxPlantsPerCell() {
        return maxPlantsPerCell;
    }

    public int getMaxAnimalsPerCell() {
        return maxAnimalsPerCell;
    }

    public Island(IslandConfig islandConfig, PrototypeFactory prototypeFactory) {
        this.islandConfig = islandConfig;
        this.prototypeFactory = prototypeFactory;
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

    public Map<Cell, List<Organism>> getIslandMap() {
        return new HashMap<>(islandMap);
    }

    public void initEmptyIsland() {
        width = islandConfig.getProperty("width");
        height = islandConfig.getProperty("height");
        maxPlantsPerCell = islandConfig.getProperty("maxPlantsPerCell");
        maxAnimalsPerCell = islandConfig.getProperty("maxAnimalsPerCell");
        Map<Cell, List<Organism>> islandMap = new HashMap<>();
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                Cell cell = new Cell(i, j);
                islandMap.put(cell, new ArrayList<>());
            }
        }
    }

    public void placeOrganisms() {
        for(Map.Entry<Cell, List<Organism>> entry : islandMap.entrySet()) {
            List<Organism> cellOrganisms = entry.getValue();
            placePlantsOnCell(cellOrganisms);
            placeAnimalsPerCell(cellOrganisms);
        }
    }

    private void placePlantsOnCell(List<Organism> cellOrganisms) {
        for(Organism prototype : prototypeFactory.getPrototypes()) {
            if(prototype instanceof Plant) {
                int prototypesPerCell = ThreadLocalRandom.current().nextInt(prototype.getMaxCountOnCell());
                if(cellOrganisms.size() + prototypesPerCell > maxPlantsPerCell) {
                    break;
                }
                fillCellWithOrganisms(cellOrganisms, prototype, prototypesPerCell);
            }
        }
    }

    private void placeAnimalsPerCell(List<Organism> cellOrganisms) {
        for(Organism prototype : prototypeFactory.getPrototypes()) {
            if(prototype instanceof Animal) {
                int prototypesPerCell = ThreadLocalRandom.current().nextInt(prototype.getMaxCountOnCell());
                if(cellOrganisms.size() + prototypesPerCell > maxAnimalsPerCell) {
                    break;
                }
                fillCellWithOrganisms(cellOrganisms, prototype, prototypesPerCell);
            }
        }
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


    private int getMaxOrganismsPerCell(Organism prototype) {
        if(prototype instanceof Plants) {
            return maxPlantsPerCell;
        } else if(prototype instanceof Animal) {
            return maxAnimalsPerCell;
        } else {
            throw new RuntimeException("UNKNOWN_ORGANISM");
        }
    }

    public void refillPlants(int maxCountPerCell) {

    }

    public void removeDeathAnimal() {

    }
}
