package ru.javarush.kornienko.island.conditions;

import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.models.predators.Predator;

import java.util.Collection;
import java.util.Set;

public class OnlyPredatorsHandler extends Handler {
    @Override
    public boolean isConditionSatisfied(Island island) {
        return island.getIslandMap().values().stream()
                .flatMap(Collection::stream)
                .allMatch(organism -> Predator.class.isAssignableFrom(organism.getClass()));
    }

    @Override
    public String getConditionTrueMessage() {
        return "Остались только хищники.";
    }
}
