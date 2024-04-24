package ru.javarush.kornienko.island.conditions;

import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.island.Island;

public class AnimalsDiedHandler extends Handler {
    @Override
    public boolean isConditionSatisfied(Island island) {
        return ifIslandHasType(island, Animal.class);
    }

    @Override
    public String getConditionTrueMessage() {
        return "Все животные умерли \uD83D\uDC80";
    }
}
