package ru.javarush.kornienko.island.services.impls;

import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.enums.Direction;
import ru.javarush.kornienko.island.models.island.Cell;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.services.MoveService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MoveServiceImpl implements MoveService {
    @Override
    public void moveIslandAnimals(Island island) {
        Map<Cell, List<Organism>> islandMap = island.getIslandMap();
        for(Map.Entry<Cell, List<Organism>> cellOrganismsEntry : islandMap.entrySet()) {
            moveCellAnimals(island, cellOrganismsEntry, islandMap);
        }
    }

    private void moveCellAnimals(Island island, Map.Entry<Cell, List<Organism>> cellOrganismsEntry, Map<Cell, List<Organism>> islandMap) {
        List<Organism> organismsToRemove = new ArrayList<>();
        Map<Cell, List<Organism>> neighborCells = getNeighborCells(cellOrganismsEntry.getKey(), islandMap);
        for(Organism organism : cellOrganismsEntry.getValue()) {
            if(organism instanceof Animal animal) {
                Set<Cell> availableCells = getAvailableCells(island, neighborCells);
                Cell destinationCell = animal.move(availableCells.toArray(new Cell[0]));
                islandMap.get(destinationCell).add(organism);
                organismsToRemove.add(organism);
            }
        }
        cellOrganismsEntry.getValue().removeAll(organismsToRemove);
    }

    /**
     *
     * @param neighborCells
     * @return cells with free spaces for animals
     */
    private Set<Cell> getAvailableCells(Island island, Map<Cell, List<Organism>> neighborCells) {
        return neighborCells.entrySet().stream()
                .filter(entry -> getAnimalCount(entry.getValue()) < island.getMaxAnimalsPerCell())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                .keySet();
    }

    private int getAnimalCount(List<Organism> organisms) {
        return (int) organisms.stream()
                .filter(Animal.class::isInstance)
                .count();
    }


    private Map<Cell, List<Organism>> getNeighborCells(Cell currentCell, Map<Cell, List<Organism>> islandMap) {
        Map<Cell, List<Organism>> neighborCells = new HashMap<>();
        for(Map.Entry<Cell, List<Organism>> cellOrganismsEntry : islandMap.entrySet()) {
            Cell neighborCell = cellOrganismsEntry.getKey();
            if(isDifferentCell(neighborCell, currentCell) && isNeighborNonDiagonalCell(neighborCell, currentCell)) {
                neighborCells.put(neighborCell, cellOrganismsEntry.getValue());
            }
        }
        return neighborCells;
    }

    private boolean isDifferentCell(Cell neighborCell, Cell currentCell) {
        return !(neighborCell.getX() == currentCell.getX() && neighborCell.getY() == currentCell.getY());
    }

    private boolean isNeighborNonDiagonalCell(Cell neighborCell, Cell currentCell) {
        return Math.abs(neighborCell.getX() - currentCell.getX()) == 1 && Math.abs(neighborCell.getY() - currentCell.getY()) == 0
               || Math.abs(neighborCell.getX() - currentCell.getX()) == 0 && Math.abs(neighborCell.getY() - currentCell.getY()) == 1;
    }

    private void moveAnimal(Animal animal, Cell from, Direction direction, byte maxSpeed) {

    }
}
