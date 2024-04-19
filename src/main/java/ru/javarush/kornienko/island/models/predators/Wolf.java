package ru.javarush.kornienko.island.models.predators;

import ru.javarush.kornienko.island.configs.Config;
import ru.javarush.kornienko.island.models.abstracts.Organism;

@Config(fileName = "configs/animals/predators/wolf.json")
public class Wolf extends Predator{
    public Wolf(double weight, int maxCountOnCell, byte maxSpeed, double kilogramsForFullSaturation) {
        super(weight, maxCountOnCell, maxSpeed, kilogramsForFullSaturation);
    }

    public Wolf() {
        super();
    }

    //TODO: move this constructor logic to main
    public Wolf(Organism organism) {
        super(organism.getWeight(), organism.getMaxCountOnCell(), organism.getMaxSpeed(), organism.getKilogramsForFullSaturation());
    }
}
