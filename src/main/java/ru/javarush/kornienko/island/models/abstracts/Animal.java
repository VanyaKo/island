package ru.javarush.kornienko.island.models.abstracts;

import ru.javarush.kornienko.island.models.island.Cell;
import ru.javarush.kornienko.island.services.AnimalAction;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal extends Organism implements AnimalAction {
    public static final int HUNDRED_PERCENT = 100;
    private double healthPercent;

    protected Animal(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
        this.healthPercent = 100;
    }

    protected Animal() {
        super();
        this.healthPercent = 100;
    }

    public void decreaseHealth(byte amount) {
        healthPercent -= amount;
    }

    /**
     * Tries to eat only one time.
     * @return true if eating successful, false otherwise;
     */
    @Override
    public boolean eat(Organism eatableOrganism, byte minEatingProbability) {
        int currentEatingProbability = ThreadLocalRandom.current().nextInt(101);
        if(currentEatingProbability <= minEatingProbability) {
            healthPercent += getAddedSaturation(eatableOrganism);
            if(healthPercent > 100) {
                healthPercent = 100;
            }
            return true;
        }
        return false;
    }

    private double getAddedSaturation(Organism eatableOrganism) {
        return eatableOrganism.weight * HUNDRED_PERCENT / kilogramsForFullSaturation;
    }


    @Override
    public Organism reproduce() {
        try {
            return (Organism) clone();
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
