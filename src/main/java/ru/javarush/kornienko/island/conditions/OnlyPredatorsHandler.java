package ru.javarush.kornienko.island.conditions;

import ru.javarush.kornienko.island.entities.abstracts.Animal;
import ru.javarush.kornienko.island.entities.island.Island;
import ru.javarush.kornienko.island.entities.predators.Predator;

import java.util.Collection;

public class OnlyPredatorsHandler extends Handler {
    @Override
    public boolean isConditionSatisfied(Island island) {
        return island.getIslandMap().values().stream()
                .flatMap(Collection::stream)
                .filter(organism -> Animal.class.isAssignableFrom(organism.getClass()))
                .allMatch(organism -> Predator.class.isAssignableFrom(organism.getClass()));
    }

    @Override
    public String getConditionTrueMessage() {
        return "Остались только хищники.";
    }
}
