package ru.javarush.kornienko.island.conditions;

import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.models.plants.Plant;

public class NoPlantsHandler extends Handler {
    @Override
    public boolean isConditionSatisfied(Island island) {
        return ifIslandHasType(island, Plant.class);
    }
}
