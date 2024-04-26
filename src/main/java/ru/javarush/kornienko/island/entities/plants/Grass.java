package ru.javarush.kornienko.island.entities.plants;

import ru.javarush.kornienko.island.configs.OrganismConfig;

@OrganismConfig(fileName = "configs/plants/grass.json")
public class Grass extends Plant {
    public Grass() {
        super();
    }

    public Grass(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }
}
