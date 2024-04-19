package ru.javarush.kornienko.island.models.predators;

import ru.javarush.kornienko.island.models.abstracts.Animal;

public abstract class Predator extends Animal {
    protected Predator(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }

    protected Predator() {
        super();
    }
}
