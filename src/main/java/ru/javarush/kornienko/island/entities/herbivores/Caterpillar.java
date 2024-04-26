package ru.javarush.kornienko.island.entities.herbivores;

import ru.javarush.kornienko.island.configs.OrganismConfig;

@OrganismConfig(fileName = "configs/animals/herbivores/caterpillar.json")
public class Caterpillar extends Herbivore {
    public Caterpillar() {
        super();
    }

    public Caterpillar(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }
}