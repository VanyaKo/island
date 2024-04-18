package ru.javarush.kornienko.island.models.herbivores;

import ru.javarush.kornienko.island.configs.Config;

@Config(fileName = "configs/animals/herbivores/rabbit.json")
public class Rabbit extends Herbivore{
    public Rabbit(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }
}
