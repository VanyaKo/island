package ru.javarush.kornienko.island.configs;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public final class ProbabilityPair implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    private final Class<?> eater;
    private final Map<Class<?>, Byte> eatables;

    public ProbabilityPair(Class<?> eater, Map<Class<?>, Byte> eatables) {
        this.eater = eater;
        this.eatables = Collections.unmodifiableMap(eatables);
    }

    public Class<?> getEater() {
        return eater;
    }

    public Map<Class<?>, Byte> getEatables() {
        return eatables;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ProbabilityPair) obj;
        return Objects.equals(this.eater, that.eater) &&
               Objects.equals(this.eatables, that.eatables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eater, eatables);
    }

    @Override
    public String toString() {
        return "ProbabilityPair[" +
               "eater=" + eater + ", " +
               "eatable=" + eatables + ']';
    }

}
