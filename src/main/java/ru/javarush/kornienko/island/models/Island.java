package ru.javarush.kornienko.island.models;

import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Island {
    private final Map<Cell, List<Organism>> islandMap;

    public Island(Map<Cell, List<Organism>> islandMap) {
        this.islandMap = islandMap;
    }

    public Map<Cell, List<Organism>> getIslandMap() {
        return new HashMap<>(islandMap);
    }

}
