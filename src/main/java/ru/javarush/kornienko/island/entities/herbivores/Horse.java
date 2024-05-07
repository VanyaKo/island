package ru.javarush.kornienko.island.entities.herbivores;

import ru.javarush.kornienko.island.configs.OrganismConfig;
import ru.javarush.kornienko.island.entities.annotations.ConcreteOrganism;

@ConcreteOrganism
@OrganismConfig(fileName = "configs/animals/herbivores/horse.json")
public class Horse extends Herbivore {
}
