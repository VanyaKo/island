package ru.javarush.kornienko.island.models.predators;

import ru.javarush.kornienko.island.configs.Config;

@Config(fileName = "configs/animals/predators/python.json")
public class Python extends Predator {
    public Python() {
        super();
    }
    public Python(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }
}
