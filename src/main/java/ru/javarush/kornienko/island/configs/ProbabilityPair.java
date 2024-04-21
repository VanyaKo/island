package ru.javarush.kornienko.island.configs;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public final class ProbabilityPair implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    private final Class<?> eater;
    private final Map<Class<?>, Byte> eatable;

    public ProbabilityPair(Class<?> eater, Map<Class<?>, Byte> eatable) {
        this.eater = eater;
        this.eatable = eatable;
    }

    public Class<?> eater() {
        return eater;
    }

    public Map<Class<?>, Byte> eatable() {
        return eatable;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ProbabilityPair) obj;
        return Objects.equals(this.eater, that.eater) &&
               Objects.equals(this.eatable, that.eatable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eater, eatable);
    }

    @Override
    public String toString() {
        return "ProbabilityPair[" +
               "eater=" + eater + ", " +
               "eatable=" + eatable + ']';
    }

}
