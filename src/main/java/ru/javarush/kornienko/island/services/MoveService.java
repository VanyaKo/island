package ru.javarush.kornienko.island.services;

import ru.javarush.kornienko.island.models.Island;
import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.enums.Direction;
import ru.javarush.kornienko.island.models.island.Cell;

public interface MoveService {
    void move(Animal animal, Cell from, Direction direction, byte maxSpeed);
}
