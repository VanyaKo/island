package ru.javarush.kornienko.island.entities.predators;

import ru.javarush.kornienko.island.configs.OrganismConfig;
import ru.javarush.kornienko.island.entities.annotations.ConcreteOrganism;

@ConcreteOrganism
@OrganismConfig(fileName = "configs/animals/predators/eagle.json")
public class Eagle extends Predator {
}
