package ru.javarush.kornienko.island.entities.herbivores;

import ru.javarush.kornienko.island.configs.OrganismConfig;

@OrganismConfig(fileName = "configs/animals/herbivores/mouse.json")
public class Mouse extends Herbivore {
    public Mouse() {
        super();
    }

    public Mouse(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }
}
