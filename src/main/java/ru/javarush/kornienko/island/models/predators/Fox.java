package ru.javarush.kornienko.island.models.predators;

import ru.javarush.kornienko.island.configs.Config;

@Config(fileName = "configs/animals/predators/fox.json")
public class Fox extends Predator {
    public Fox(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }
}
