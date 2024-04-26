package ru.javarush.kornienko.island.services;

import ru.javarush.kornienko.island.consts.Consts;
import ru.javarush.kornienko.island.entities.abstracts.Animal;
import ru.javarush.kornienko.island.entities.abstracts.Organism;
import ru.javarush.kornienko.island.entities.island.Cell;
import ru.javarush.kornienko.island.entities.island.Island;
import ru.javarush.kornienko.island.services.utils.MapWorker;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class MoveService {
    private final Island island;
    private final Map<Class<?>, Integer> classToMoveProbability;
    private ConcurrentMap<Class<? extends Organism>, Long> movedOrganismClassToCount;

    public MoveService(Island island, Map<Class<?>, Integer> classToMoveProbability) {
        this.island = island;
        this.classToMoveProbability = classToMoveProbability;
    }

    public Map<Class<? extends Organism>, Long> getMovedOrganismClassToCount() {
        return new HashMap<>(movedOrganismClassToCount);
    }

    public void resetMovedOrganismsMap() {
        movedOrganismClassToCount = new ConcurrentHashMap<>();
    }

    public Map<Animal, Cell> getAnimalsToMove() {
        Map<Animal, Cell> animalsToMove = new HashMap<>();
        for(Map.Entry<Cell, Set<Organism>> cellOrganismsEntry : island.getIslandMap().entrySet()) {
            Cell currentCell = cellOrganismsEntry.getKey();
            island.getAnimalListFromOrganisms(cellOrganismsEntry.getValue())
                    .forEach(animal -> animalsToMove.put(animal, currentCell));
        }
        return animalsToMove;
    }

    public synchronized void moveCellAnimals(Map.Entry<Animal, Cell> animalToCellEntry) {
        Animal animal = animalToCellEntry.getKey();
        Cell startCell = animalToCellEntry.getValue();
        int maxMoveProbability = classToMoveProbability.get(animal.getClass());
        moveAnimal(animal, startCell, maxMoveProbability);
    }

    private synchronized void moveAnimal(Animal animal, Cell startCell, int maxMoveProbability) {
        boolean isMoved = false;
        for(int i = 0; i <= ThreadLocalRandom.current().nextInt(animal.getMaxSpeed() + 1); i++) {

            Map<Cell, Set<Organism>> neighborCells = getNeighborCells(startCell);
            Set<Cell> availableCells = getAvailableCells(neighborCells);
            if(!availableCells.isEmpty() && isSuccessMoveProbability(maxMoveProbability)) {
                synchronized(island) {
                    Cell destinationCell = animal.move(availableCells.toArray(new Cell[0]));
                    island.addAnimalToCell(animal, destinationCell);
                    island.removeOrganismFromCell(animal, startCell);
                    startCell = destinationCell;
                    isMoved = true;
                }
            }
        }

        if(isMoved) {
            MapWorker.putDuplicateValueCount(movedOrganismClassToCount, animal.getClass());
        }
    }

    private boolean isSuccessMoveProbability(int maxMoveProbability) {
        return ThreadLocalRandom.current().nextInt(Consts.HUNDRED_PERCENT + 1) <= maxMoveProbability;
    }

    private synchronized Map<Cell, Set<Organism>> getNeighborCells(Cell currentCell) {
        synchronized(island) {
            Map<Cell, Set<Organism>> neighborCells = new HashMap<>();
            for(Map.Entry<Cell, Set<Organism>> cellOrganismsEntry : island.getIslandMap().entrySet()) {
                Cell neighborCell = cellOrganismsEntry.getKey();
                if(isDifferentCell(neighborCell, currentCell) && isNeighborNonDiagonalCell(neighborCell, currentCell)) {
                    neighborCells.put(neighborCell, cellOrganismsEntry.getValue());
                }
            }
            return neighborCells;
        }
    }

    /**
     *
     * @return cells with free spaces for animals
     */
    private synchronized Set<Cell> getAvailableCells(Map<Cell, Set<Organism>> neighborCells) {
        synchronized(island) {
            return neighborCells.entrySet().stream()
                    .filter(entry -> getAnimalCount(entry.getValue()) < island.getMaxAnimalsPerCell())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
                    .keySet();
        }
    }

    private int getAnimalCount(Collection<Organism> organisms) {
        return (int) organisms.stream()
                .filter(Animal.class::isInstance)
                .count();
    }


    private boolean isDifferentCell(Cell neighborCell, Cell currentCell) {
        return !(neighborCell.x() == currentCell.x() && neighborCell.y() == currentCell.y());
    }

    private boolean isNeighborNonDiagonalCell(Cell neighborCell, Cell currentCell) {
        return Math.abs(neighborCell.x() - currentCell.x()) == 1 && Math.abs(neighborCell.y() - currentCell.y()) == 0
               || Math.abs(neighborCell.x() - currentCell.x()) == 0 && Math.abs(neighborCell.y() - currentCell.y()) == 1;
    }
}
