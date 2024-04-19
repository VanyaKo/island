package ru.javarush.kornienko.island.configs;

import java.io.Serializable;
import java.util.Map;

public class ProbabilityPair implements Serializable {
    private String eater;
    private Map<String, Byte> eatable;

    public ProbabilityPair() {
    }

    public ProbabilityPair(String eater, Map<String, Byte> eatable) {
        this.eater = eater;
        this.eatable = eatable;
    }

    public String getEater() {
        return eater;
    }

    public void setEater(String eater) {
        this.eater = eater;
    }

    public Map<String, Byte> getEatable() {
        return eatable;
    }

    public void setEatable(Map<String, Byte> eatable) {
        this.eatable = eatable;
    }
}
