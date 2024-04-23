package ru.javarush.kornienko.island.services;

import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;

public interface AnimalAction {
    /**
     * Trie once to eat and return if it was successful.
     * @return true if eating was successful, false otherwise.
     */
    boolean eat(Organism eatableOrganism, byte maxEatingProbability);

    Animal reproduce();

    /**
     * Move to random cell and return it.
     */
    Cell move(Cell[] cells);
}
