package ru.javarush.kornienko.island.models.herbivores;

import ru.javarush.kornienko.island.models.abstracts.Animal;

public abstract class Herbivore extends Animal {
    public Herbivore(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }
}
