package ru.javarush.kornienko.island.models.abstracts;

import ru.javarush.kornienko.island.consts.Consts;
import ru.javarush.kornienko.island.models.island.Cell;
import ru.javarush.kornienko.island.services.AnimalAction;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal extends Organism implements AnimalAction {
    private double healthPercent;

    protected Animal(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
        this.healthPercent = 100;
    }

    protected Animal() {
        super();
        this.healthPercent = 100;
    }

    public double decreaseHealth(double amount) {
        healthPercent -= amount;
        return healthPercent;
    }

    @Override
    public boolean eat(Organism eatableOrganism, byte maxEatingProbability) {
        int currentEatingProbability = ThreadLocalRandom.current().nextInt(101);
        if(currentEatingProbability <= maxEatingProbability) {
            healthPercent += getAddedSaturation(eatableOrganism);
            if(healthPercent > 100) {
                healthPercent = 100;
            }
            return true;
        }
        return false;
    }

    private double getAddedSaturation(Organism eatableOrganism) {
        return eatableOrganism.weight * Consts.HUNDRED_PERCENT / kilogramsForFullSaturation;
    }


    @Override
    public Animal reproduce() {
        try {
            return (Animal) clone();
        } catch(CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param cells
     * @return destination cell
     */
    @Override
    public Cell move(Cell[] cells) {
        return cells[ThreadLocalRandom.current().nextInt(cells.length)];
    }
}
