package ru.javarush.kornienko.island.models.predators;

import ru.javarush.kornienko.island.configs.Config;

@Config(fileName = "configs/animals/predators/eagle.json")
public class Eagle extends Predator {
    public Eagle() {
        super();
    }

    public Eagle(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }
}
