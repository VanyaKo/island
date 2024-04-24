package ru.javarush.kornienko.island.services;

import ru.javarush.kornienko.island.models.abstracts.Organism;

import java.util.Map;

public interface EatService extends OrganismService {
    /**
     * @return
     */
    Map<Class<? extends Organism>, Long> eatIslandOrganisms();
}
