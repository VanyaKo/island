package ru.javarush.kornienko.island.configs.animals;

public record ReproduceConfig(Class<?> reproducer, byte couplingProbability, byte birthProbability, int maxCubs) {

}
