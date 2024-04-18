package ru.javarush.kornienko.island.models.herbivores;

import ru.javarush.kornienko.island.configs.Config;

@Config(fileName = "configs/animals/herbivores/duck.json")
public class Duck extends Herbivore{
    public Duck(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }
}
