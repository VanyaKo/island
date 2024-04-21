package ru.javarush.kornienko.island.models.abstracts;

import ru.javarush.kornienko.island.services.AnimalAction;

import java.util.Collection;

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

    /**
     * Tries to eat only one time.
     * @param eatableOrganisms
     */
    @Override
    public void eat(Collection<Organism> eatableOrganisms) {
        
    }

    @Override
    public Organism reproduce() {
        return null;
    }

    @Override
    public void move() {

    }
}
