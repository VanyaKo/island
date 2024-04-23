package ru.javarush.kornienko.island.services;

import ru.javarush.kornienko.island.models.abstracts.Organism;

import java.util.Map;

public interface StatisticsService {
    void printCurrentOrganismInfo();
    void printEatInfo(Map<Class<? extends Organism>, Long> organismClassesToCount);
    void printReproduceInfo(Map<Class<? extends Organism>, Long> organismClassesToCount);
    void printMoveInfo(Map<Class<? extends Organism>, Long> organismClassesToCount);
    void printDieInfo(Map<Class<? extends Organism>, Long> organismClassesToCount);
}
