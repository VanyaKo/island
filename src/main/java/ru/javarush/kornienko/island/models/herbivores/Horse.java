package ru.javarush.kornienko.island.models.herbivores;

import ru.javarush.kornienko.island.configs.OrganismConfig;

@OrganismConfig(fileName = "configs/animals/herbivores/horse.json")
public class Horse extends Herbivore {
    public Horse() {
        super();
    }

    public Horse(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }
}
