package ru.javarush.kornienko.island.models.enums;

import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.herbivores.Mouse;
import ru.javarush.kornienko.island.models.plants.Grass;
import ru.javarush.kornienko.island.models.predators.Wolf;

public enum OrganismType {
    WOLF("wolf", Wolf.class),
    MOUSE("mouse", Mouse.class),
    GRASS("grass", Grass.class);

    private final String type;
    private final Class<? extends Organism> clazz;

    OrganismType(String type, Class<? extends Organism> clazz) {
        this.type = type;
        this.clazz = clazz;
    }
}
