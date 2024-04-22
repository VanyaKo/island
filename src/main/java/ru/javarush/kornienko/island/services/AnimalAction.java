package ru.javarush.kornienko.island.services;

import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;

public interface AnimalAction {
    boolean eat(Organism eatableOrganism, byte minEatingProbability);

    Organism reproduce();

    /**
     *
     * @param cells
     * @return destination cell
     */
    Cell move(Cell[] cells);
}
