package ru.javarush.kornienko.island.services;

import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;

public interface AnimalAction extends OrganismService {
    /**
     * Try eating once and return if it was successful.
     */
    boolean eat(Organism eatableOrganism, byte maxEatingProbability);

    Animal reproduce();

    /**
     * Move to random cell and return it.
     */
    Cell move(Cell[] cells);
}
