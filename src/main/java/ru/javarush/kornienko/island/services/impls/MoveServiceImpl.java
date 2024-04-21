package ru.javarush.kornienko.island.services.impls;

import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.enums.Direction;
import ru.javarush.kornienko.island.models.island.Cell;
import ru.javarush.kornienko.island.services.MoveService;

public class MoveServiceImpl implements MoveService {
    private final Island island;

    public MoveServiceImpl(Island island) {
        this.island = island;
    }

    @Override
    public void move(Animal animal, Cell from, Direction direction, byte maxSpeed) {
        //TODO: check if can move and move:)
    }
}
