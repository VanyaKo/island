package ru.javarush.kornienko.island.entities.predators;

import ru.javarush.kornienko.island.configs.OrganismConfig;

@OrganismConfig(fileName = "configs/animals/predators/python.json")
public class Python extends Predator {
    public Python() {
        super();
    }

    public Python(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }
}
