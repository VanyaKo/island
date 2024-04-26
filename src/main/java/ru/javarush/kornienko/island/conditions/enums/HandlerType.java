package ru.javarush.kornienko.island.conditions.enums;

import ru.javarush.kornienko.island.conditions.AnimalsDiedHandler;
import ru.javarush.kornienko.island.conditions.Handler;
import ru.javarush.kornienko.island.conditions.NoPlantsHandler;
import ru.javarush.kornienko.island.conditions.OnlyPredatorsHandler;
import ru.javarush.kornienko.island.exceptions.AppException;

public enum HandlerType {
    ONLY_PREDATORS(new OnlyPredatorsHandler()),
    NO_PLANTS(new NoPlantsHandler()),
    ANIMALS_DIED(new AnimalsDiedHandler());

    private final Handler handler;

    HandlerType(Handler handler) {
        this.handler = handler;
    }

    public static Handler getHandlerByName(String conditionName) {
        try {
            return HandlerType.valueOf(conditionName.toUpperCase()).handler;
        } catch(IllegalArgumentException e) {
            throw new AppException("\"" + conditionName + "\"" + " condition not found", e);
        }
    }
}
