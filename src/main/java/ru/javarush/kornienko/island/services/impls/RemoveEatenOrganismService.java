package ru.javarush.kornienko.island.services.impls;

import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;

import java.util.List;
import java.util.Map;

@Deprecated
public class RemoveEatenOrganismService {
    public void removeEatenOrganisms(Map<Cell, List<Organism>> islandMap, Map<Cell, List<Organism>> organismsToRemove) {
        for(Map.Entry<Cell, List<Organism>> islandEntry : islandMap.entrySet()) {
            List<Organism> organismsToRemoveOnCell = organismsToRemove.get(islandEntry.getKey());
            islandEntry.getValue().removeAll(organismsToRemoveOnCell);
        }
    }
}
