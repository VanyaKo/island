package ru.javarush.kornienko.island.models.plants;

import ru.javarush.kornienko.island.configs.OrganismConfig;
import ru.javarush.kornienko.island.models.abstracts.Organism;

@OrganismConfig(fileName = "configs/plants/grass.json")
public class Grass extends Plant {
    public Grass() {
        super();
    }

    public Grass(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }

    public Grass(Organism organism) {
        super(organism.getWeight(), organism.getMaxCountOnCell(), organism.getMaxSpeed(), organism.getKilogramsForFullSaturation());
    }

}
