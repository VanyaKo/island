package ru.javarush.kornienko.island.services.impls;

import ru.javarush.kornienko.island.models.enums.Direction;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ChooseDirectionService {
    public Direction chooseDirection() {
        return Direction.values()[ThreadLocalRandom.current().nextInt(Direction.values().length)];
    }
}
