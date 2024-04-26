package ru.javarush.kornienko.island.conditions;

import ru.javarush.kornienko.island.entities.island.Island;
import ru.javarush.kornienko.island.entities.plants.Plant;

public class NoPlantsHandler extends Handler {
    @Override
    public boolean isConditionSatisfied(Island island) {
        return ifIslandHasType(island, Plant.class);
    }

    @Override
    public String getConditionTrueMessage() {
        return "Все растения закончились.";
    }
}
