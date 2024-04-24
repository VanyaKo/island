package ru.javarush.kornienko.island.services;

import ru.javarush.kornienko.island.models.abstracts.Organism;

import java.util.Map;

public interface DieService extends OrganismService {
    Map<Class<? extends Organism>, Long> killHungryIslandAnimals();
}
