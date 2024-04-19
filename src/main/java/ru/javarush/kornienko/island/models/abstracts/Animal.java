package ru.javarush.kornienko.island.models.abstracts;

import ru.javarush.kornienko.island.services.AnimalAction;

public abstract class Animal extends Organism implements AnimalAction {
    protected Animal(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }

    protected Animal() {
        super();
    }

    @Override
    public void eat() {

    }

    @Override
    public Organism reproduce() {
        return null;
    }

    @Override
    public void move() {

    }
}
