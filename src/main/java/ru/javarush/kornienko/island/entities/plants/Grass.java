package ru.javarush.kornienko.island.entities.plants;

import ru.javarush.kornienko.island.configs.OrganismConfig;
import ru.javarush.kornienko.island.entities.annotations.ConcreteOrganism;

@ConcreteOrganism
@OrganismConfig(fileName = "configs/plants/grass.json")
public class Grass extends Plant {
}
