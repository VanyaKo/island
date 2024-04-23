package ru.javarush.kornienko.island.services.impls;

import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.services.MoveService;
import ru.javarush.kornienko.island.services.OrganismService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class MoveServiceImpl implements MoveService {
    private final Island island;
    private Map<Class<? extends Organism>, Long> movedOrganismClassToCount;

    public MoveServiceImpl(Island island) {
        this.island = island;
    }

    public Map<Class<? extends Organism>, Long> getMovedOrganismClassToCount() {
        return new HashMap<>(movedOrganismClassToCount);
    }

    @Override
    public void moveIslandAnimals() {
        movedOrganismClassToCount = new HashMap<>();
        Map<Animal, Cell> animalsToMove = getAnimalsToMove();
        for(Map.Entry<Animal, Cell> animalCellEntry : animalsToMove.entrySet()) {
            Animal animal = animalCellEntry.getKey();
            Cell startCell = animalCellEntry.getValue();
            moveAnimal(animal, startCell);
        }
    }

    private Map<Animal, Cell> getAnimalsToMove() {
        Map<Animal, Cell> animalsToMove = new HashMap<>();
        for(Map.Entry<Cell, List<Organism>> cellOrganismsEntry : island.getIslandMap().entrySet()) {
            Cell currentCell = cellOrganismsEntry.getKey();
            island.getAnimalListFromOrganisms(cellOrganismsEntry.getValue())
                    .forEach(animal -> animalsToMove.put(animal, currentCell));
        }
        return animalsToMove;
    }

    private void moveAnimal(Animal animal, Cell startCell) {
        for(int i = 0; i <= ThreadLocalRandom.current().nextInt(animal.getMaxSpeed() + 1); i++) {
            Map<Cell, List<Organism>> neighborCells = getNeighborCells(startCell);
            Set<Cell> availableCells = getAvailableCells(neighborCells);
            if(!availableCells.isEmpty()) {
                Cell destinationCell = animal.move(availableCells.toArray(new Cell[0]));
                putDuplicateClassCount(movedOrganismClassToCount, animal.getClass());
                island.addAnimalToCell(animal, destinationCell);
                island.removeOrganismFromCell(animal, startCell);
                startCell = destinationCell;
            }
        }
    }

    private Map<Cell, List<Organism>> getNeighborCells(Cell currentCell) {
        Map<Cell, List<Organism>> neighborCells = new HashMap<>();
        for(Map.Entry<Cell, List<Organism>> cellOrganismsEntry : island.getIslandMap().entrySet()) {
            Cell neighborCell = cellOrganismsEntry.getKey();
            if(isDifferentCell(neighborCell, currentCell) && isNeighborNonDiagonalCell(neighborCell, currentCell)) {
                neighborCells.put(neighborCell, cellOrganismsEntry.getValue());
            }
        }
        return neighborCells;
    }

    /**
     *
     * @return cells with free spaces for animals
     */
    private Set<Cell> getAvailableCells(Map<Cell, List<Organism>> neighborCells) {
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


    private boolean isDifferentCell(Cell neighborCell, Cell currentCell) {
        return !(neighborCell.getX() == currentCell.getX() && neighborCell.getY() == currentCell.getY());
    }

    private boolean isNeighborNonDiagonalCell(Cell neighborCell, Cell currentCell) {
        return Math.abs(neighborCell.getX() - currentCell.getX()) == 1 && Math.abs(neighborCell.getY() - currentCell.getY()) == 0
               || Math.abs(neighborCell.getX() - currentCell.getX()) == 0 && Math.abs(neighborCell.getY() - currentCell.getY()) == 1;
    }
}
