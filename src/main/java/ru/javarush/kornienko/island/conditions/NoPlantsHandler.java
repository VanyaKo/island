package ru.javarush.kornienko.island.conditions;

import ru.javarush.kornienko.island.models.island.Island;

public class NoPlantsHandler extends Handler {
    @Override
    public boolean isConditionSatisfied(Island island) {
        return false;
    }
}
