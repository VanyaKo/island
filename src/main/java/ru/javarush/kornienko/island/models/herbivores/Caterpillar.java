package ru.javarush.kornienko.island.models.herbivores;

import ru.javarush.kornienko.island.configs.Config;

@Config(fileName = "configs/animals/herbivores/caterpillar.json")
public class Caterpillar extends Herbivore{
    public Caterpillar(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }
}
