package ru.javarush.kornienko.island.configs.animals;

public class ReproduceConfig {
    private Class<?> reproducer;
    private byte couplingProbability;
    private byte birthProbability;
    private int maxCubs;

    public ReproduceConfig(Class<?> reproducer, byte couplingProbability, byte birthProbability, int maxCubs) {
        this.reproducer = reproducer;
        this.couplingProbability = couplingProbability;
        this.birthProbability = birthProbability;
        this.maxCubs = maxCubs;
    }

    public ReproduceConfig() {
    }

    public byte getBirthProbability() {
        return birthProbability;
    }

    public void setBirthProbability(byte birthProbability) {
        this.birthProbability = birthProbability;
    }

    public byte getCouplingProbability() {
        return couplingProbability;
    }

    public void setCouplingProbability(byte couplingProbability) {
        this.couplingProbability = couplingProbability;
    }

    public Class<?> getReproducer() {
        return reproducer;
    }

    public void setReproducer(Class<?> reproducer) {
        this.reproducer = reproducer;
    }

    public int getMaxCubs() {
        return maxCubs;
    }

    public void setMaxCubs(int maxCubs) {
        this.maxCubs = maxCubs;
    }
}
