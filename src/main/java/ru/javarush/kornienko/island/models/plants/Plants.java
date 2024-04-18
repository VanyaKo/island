package ru.javarush.kornienko.island.models.plants;

import ru.javarush.kornienko.island.models.abstracts.Organism;

public class Plants extends Plant {
    public Plants(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }

    public Plants(Organism organism) {
        super(organism.getWeight(), organism.getMaxCountOnCell(), organism.getMaxSpeed(), organism.getKilogramsForFullSaturation());
    }

}
