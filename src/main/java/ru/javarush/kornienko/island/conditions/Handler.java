package ru.javarush.kornienko.island.conditions;

import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Island;

import java.util.Set;

public abstract class Handler {
    public abstract boolean isConditionSatisfied(Island island);

    public abstract String getConditionTrueMessage();

    protected boolean ifIslandHasType(Island island, Class<? extends Organism> clazz) {
        for(Set<Organism> organisms : island.getIslandMap().values()) {
            for(Organism organism : organisms) {
                if(clazz.isAssignableFrom(organism.getClass())) {
                    return false;
                }
            }
        }
        return true;
    }
}
