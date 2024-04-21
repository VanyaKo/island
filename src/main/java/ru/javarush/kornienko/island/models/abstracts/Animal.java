package ru.javarush.kornienko.island.models.abstracts;

import ru.javarush.kornienko.island.services.AnimalAction;

public abstract class Animal extends Organism implements AnimalAction {
    private byte healthPercent;

    public void decreaseHealth(byte amount) {
        healthPercent -= amount;
    }
    protected Animal(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
        this.healthPercent = 100;
    }

    protected Animal() {
        super();
        this.healthPercent = 100;
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
