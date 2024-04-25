package ru.javarush.kornienko.island.models.herbivores;

import ru.javarush.kornienko.island.configs.OrganismConfig;

@OrganismConfig(fileName = "configs/animals/herbivores/hog.json")
public class Hog extends Herbivore {
    public Hog() {
        super();
    }

    public Hog(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }
}
