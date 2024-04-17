package ru.javarush.kornienko.island.models.plants;

import ru.javarush.kornienko.island.models.abstracts.Organism;

public class Grass extends Plant {
    public Grass(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }

    public Grass(Organism organism) {
        super(organism.getWeight(), organism.getMaxCountOnCell(), organism.getMaxSpeed(), organism.getKilogramsForFullSaturation());
    }

}
