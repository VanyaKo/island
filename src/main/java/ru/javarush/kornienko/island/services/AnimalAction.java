package ru.javarush.kornienko.island.services;

import ru.javarush.kornienko.island.entities.abstracts.Animal;
import ru.javarush.kornienko.island.entities.abstracts.Organism;
import ru.javarush.kornienko.island.entities.island.Cell;

import java.util.Set;

public interface AnimalAction {
    /**
     * Try eating once and return if it was successful.
     */
    boolean eat(Organism eatableOrganism, byte maxEatingProbability);

    /**
     * Reproduce animals and return them.
     */
    Set<Animal> reproduce(int maxCubs);

    /**
     * Move to random cell and return it.
     */
    Cell move(Cell[] cells);
}
