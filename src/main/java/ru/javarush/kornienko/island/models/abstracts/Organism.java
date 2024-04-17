package ru.javarush.kornienko.island.models.abstracts;

import ru.javarush.kornienko.island.services.AnimalAction;

public class Organism {
    protected final double weight;
    protected final int maxCountOnCell;
    protected final byte maxSpeed;
    protected final double kilogramsForFullSaturation;

    public Organism(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        this.weight = weight;
        this.maxCountOnCell = maxCountOnCell;
        this.maxSpeed = maxSpeed;
        this.kilogramsForFullSaturation = kilogramsForFullSaturation;
    }

    public double getWeight() {
        return weight;
    }

    public int getMaxCountOnCell() {
        return maxCountOnCell;
    }

    public byte getMaxSpeed() {
        return maxSpeed;
    }

    public double getKilogramsForFullSaturation() {
        return kilogramsForFullSaturation;
    }
}
