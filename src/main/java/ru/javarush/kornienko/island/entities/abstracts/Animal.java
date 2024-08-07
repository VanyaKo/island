package ru.javarush.kornienko.island.entities.abstracts;

import ru.javarush.kornienko.island.consts.Consts;
import ru.javarush.kornienko.island.entities.island.Cell;
import ru.javarush.kornienko.island.exceptions.AppException;
import ru.javarush.kornienko.island.services.actions.AnimalAction;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Animal extends Organism implements AnimalAction {
    private double healthPercent;

    public double decreaseHealth(double amount) {
        healthPercent -= amount;
        return healthPercent;
    }

    @Override
    public boolean eat(Organism eatableOrganism, byte maxEatingProbability) {
        int currentEatingProbability = ThreadLocalRandom.current().nextInt(Consts.HUNDRED_PERCENT + 1);
        if(currentEatingProbability <= maxEatingProbability) {
            healthPercent += getAddedSaturation(eatableOrganism);
            if(healthPercent > 100) {
                healthPercent = 100;
            }
            return true;
        }
        return false;
    }

    @Override
    public Set<Animal> reproduce(int maxCubs) {
        try {
            Set<Animal> cubs = new HashSet<>();
            int cubNumber = ThreadLocalRandom.current().nextInt(1, maxCubs + 1);
            for(int i = 0; i < cubNumber; i++) {
                cubs.add((Animal) clone());
            }
            return cubs;
        } catch(CloneNotSupportedException e) {
            throw new AppException(e);
        }
    }

    @Override
    public synchronized Cell move(Cell[] cells) {
        return cells[ThreadLocalRandom.current().nextInt(cells.length)];
    }

    private synchronized double getAddedSaturation(Organism eatableOrganism) {
        return eatableOrganism.weight * Consts.HUNDRED_PERCENT / kilogramsForFullSaturation;
    }
}
