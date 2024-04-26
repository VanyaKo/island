package ru.javarush.kornienko.island.entities.herbivores;

import ru.javarush.kornienko.island.entities.abstracts.Animal;

public abstract class Herbivore extends Animal {
    protected Herbivore(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }

    protected Herbivore() {
        super();
    }
}
