package ru.javarush.kornienko.island.configs.action;

public class ReproduceProbabilityEntry {
    private Class<?> reproducer;
    private int couplingProbability;

    public int getBirthProbability() {
        return birthProbability;
    }

    public int getCouplingProbability() {
        return couplingProbability;
    }

    private int birthProbability;

    public ReproduceProbabilityEntry(Class<?> reproducer, int couplingProbability, int birthProbability) {
        this.reproducer = reproducer;
        this.couplingProbability = couplingProbability;
        this.birthProbability = birthProbability;
    }

    public ReproduceProbabilityEntry() {
    }

    public void setCouplingProbability(int couplingProbability) {
        this.couplingProbability = couplingProbability;
    }

    public void setBirthProbability(int birthProbability) {
        this.birthProbability = birthProbability;
    }

    public void setReproducer(Class<?> reproducer) {
        this.reproducer = reproducer;
    }

    public Class<?> getReproducer() {
        return reproducer;
    }
}
