package ru.javarush.kornienko.island.conditions;

import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.models.predators.Predator;

import java.util.Set;

public class OnlyPredatorsHandler extends Handler {
    @Override
    public boolean isConditionSatisfied(Island island) {
        for(Set<Organism> organisms : island.getIslandMap().values()) {
            for(Organism organism : organisms) {
                if(!Predator.class.isAssignableFrom(organism.getClass())) {
                    return false;
                }
            }
        }
        return true;
    }
}
