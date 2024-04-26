package ru.javarush.kornienko.island.entities.abstracts;

import java.io.Serializable;

public abstract class Organism implements Serializable, Cloneable {
    protected final double weight;
    protected final int maxCountOnCell;
    protected final byte maxSpeed;
    protected final double kilogramsForFullSaturation;

    protected Organism() {
        this.weight = -1;
        this.maxCountOnCell = -1;
        this.maxSpeed = -1;
        this.kilogramsForFullSaturation = -1;
    }

    protected Organism(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        this.weight = weight;
        this.maxCountOnCell = maxCountOnCell;
        this.maxSpeed = maxSpeed;
        this.kilogramsForFullSaturation = kilogramsForFullSaturation;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
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
