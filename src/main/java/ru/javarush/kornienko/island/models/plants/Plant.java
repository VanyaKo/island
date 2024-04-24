package ru.javarush.kornienko.island.models.plants;

import ru.javarush.kornienko.island.models.abstracts.Organism;

public abstract class Plant extends Organism {
    protected Plant() {
        super();
    }

    protected Plant(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }
}
