package ru.javarush.kornienko.island.models.herbivores;

import ru.javarush.kornienko.island.configs.OrganismConfig;

@OrganismConfig(fileName = "configs/animals/herbivores/deer.json")
public class Deer extends Herbivore {
    public Deer(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }

    public Deer() {
        super();
    }
}
