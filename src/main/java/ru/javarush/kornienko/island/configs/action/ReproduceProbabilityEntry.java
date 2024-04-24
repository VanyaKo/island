package ru.javarush.kornienko.island.configs.action;

public class ReproduceProbabilityEntry {
    private Class<?> reproducer;
    private int couplingProbability;
    private int birthProbability;
    private int maxCubs;

    public ReproduceProbabilityEntry(Class<?> reproducer, int couplingProbability, int birthProbability, int maxCubs) {
        this.reproducer = reproducer;
        this.couplingProbability = couplingProbability;
        this.birthProbability = birthProbability;
        this.maxCubs = maxCubs;
    }

    public ReproduceProbabilityEntry() {
    }

    public int getBirthProbability() {
        return birthProbability;
    }

    public void setBirthProbability(int birthProbability) {
        this.birthProbability = birthProbability;
    }

    public int getCouplingProbability() {
        return couplingProbability;
    }

    public void setCouplingProbability(int couplingProbability) {
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
