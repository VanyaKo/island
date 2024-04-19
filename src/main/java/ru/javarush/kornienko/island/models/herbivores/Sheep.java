package ru.javarush.kornienko.island.models.herbivores;

import ru.javarush.kornienko.island.configs.Config;

@Config(fileName = "configs/animals/herbivores/sheep.json")
public class Sheep extends Herbivore {
    public Sheep() {
        super();
    }

    public Sheep(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }
}
