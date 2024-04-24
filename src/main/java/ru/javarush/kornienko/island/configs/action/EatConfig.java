package ru.javarush.kornienko.island.configs.action;

import java.util.Collections;
import java.util.Map;

public record EatConfig(Class<?> eater, Map<Class<?>, Byte> eatables) {
    public EatConfig(Class<?> eater, Map<Class<?>, Byte> eatables) {
        this.eater = eater;
        this.eatables = Collections.unmodifiableMap(eatables);
    }

    @Override
    public String toString() {
        return "ProbabilityPair[" +
               "eater=" + eater + ", " +
               "eatable=" + eatables + ']';
    }
}
