package ru.javarush.kornienko.island.models.predators;

import ru.javarush.kornienko.island.configs.OrganismConfig;

@OrganismConfig(fileName = "configs/animals/predators/bear.json")
public class Bear extends Predator {
    public Bear() {
        super();
    }

    public Bear(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }
}
