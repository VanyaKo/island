package ru.javarush.kornienko.island.conditions;

import ru.javarush.kornienko.island.models.island.Island;

public abstract class Handler {
    public abstract boolean isConditionSatisfied(Island island);
}
