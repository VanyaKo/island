package ru.javarush.kornienko.island.services.impls;

import ru.javarush.kornienko.island.models.enums.Direction;

import java.util.Random;

public class ChooseDirectionService {
    private final Random random;

    public ChooseDirectionService(Random random) {
        this.random = random;
    }

    public Direction chooseDirection() {
        return Direction.values()[random.nextInt(Direction.values().length)];
    }
}
