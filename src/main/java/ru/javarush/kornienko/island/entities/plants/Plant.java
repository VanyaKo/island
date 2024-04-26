package ru.javarush.kornienko.island.entities.plants;

import ru.javarush.kornienko.island.entities.abstracts.Organism;

public abstract class Plant extends Organism {
    protected Plant() {
        super();
    }

    protected Plant(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }
}
