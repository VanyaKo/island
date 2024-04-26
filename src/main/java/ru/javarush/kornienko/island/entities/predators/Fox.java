package ru.javarush.kornienko.island.entities.predators;

import ru.javarush.kornienko.island.configs.OrganismConfig;

@OrganismConfig(fileName = "configs/animals/predators/fox.json")
public class Fox extends Predator {
    public Fox() {
        super();
    }

    public Fox(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }
}
