package ru.javarush.kornienko.island.models.plants;

import ru.javarush.kornienko.island.configs.Config;
import ru.javarush.kornienko.island.models.abstracts.Organism;

@Config(fileName = "configs/plants/plants.json")
public class Plants extends Plant {
    public Plants() {
        super();
    }

    public Plants(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }

    public Plants(Organism organism) {
        super(organism.getWeight(), organism.getMaxCountOnCell(), organism.getMaxSpeed(), organism.getKilogramsForFullSaturation());
    }

}
