package ru.javarush.kornienko.island.conditions;

import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Island;

import java.util.List;

public class AnimalsDiedHandler extends Handler {
    @Override
    public boolean isConditionSatisfied(Island island) {
        for(List<Organism> organisms : island.getIslandMap().values()) {
            for(Organism organism : organisms) {
                if(organism instanceof Animal) {
                    return false;
                }
            }
        }
        return true;
    }
}
