package ru.javarush.kornienko.island.services;

import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;

import java.util.Set;

public interface AnimalAction {
    /**
     * Try eating once and return if it was successful.
     */
    boolean eat(Organism eatableOrganism, byte maxEatingProbability);

    Set<Animal> reproduce(int maxCubs);

    /**
     * Move to random cell and return it.
     */
    Cell move(Cell[] cells);
}
