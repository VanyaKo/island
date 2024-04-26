package ru.javarush.kornienko.island.conditions;

import ru.javarush.kornienko.island.entities.abstracts.Organism;
import ru.javarush.kornienko.island.entities.island.Island;

import java.util.Collection;

public abstract class Handler {
    public abstract boolean isConditionSatisfied(Island island);

    public abstract String getConditionTrueMessage();

    protected boolean ifIslandHasType(Island island, Class<? extends Organism> clazz) {
        return island.getIslandMap().values().stream()
                .flatMap(Collection::stream)
                .noneMatch(organism -> clazz.isAssignableFrom(organism.getClass()));
    }
}
