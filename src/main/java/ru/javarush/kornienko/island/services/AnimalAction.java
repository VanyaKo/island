package ru.javarush.kornienko.island.services;

import ru.javarush.kornienko.island.configs.ProbabilityPair;
import ru.javarush.kornienko.island.models.abstracts.Organism;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AnimalAction {
    boolean eat(Organism eatableOrganism, byte minEatingProbability);
    Organism reproduce();
    void move();
}
