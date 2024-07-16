package ru.javarush.kornienko.island.entities.abstracts;

import java.io.Serializable;

public abstract class Organism implements Serializable, Cloneable {
    protected double weight;
    protected int maxCountOnCell;
    protected byte maxSpeed;
    protected double kilogramsForFullSaturation;
    protected String unicode;

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

    public String getUnicode() {
        return unicode;
    }
}
