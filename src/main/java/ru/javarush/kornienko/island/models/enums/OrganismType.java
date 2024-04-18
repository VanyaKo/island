package ru.javarush.kornienko.island.models.enums;

import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.herbivores.Mouse;
import ru.javarush.kornienko.island.models.plants.Grass;
import ru.javarush.kornienko.island.models.predators.Wolf;

import java.util.Arrays;

public enum OrganismType {
    WOLF("wolf", Wolf.class),
    MOUSE("mouse", Mouse.class),
    GRASS("grass", Grass.class);

    private final String name;
    private final Class<? extends Organism> clazz;

    OrganismType(String name, Class<? extends Organism> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public static OrganismType getOrganismTypeByName(String name) {
        return Arrays.stream(values())
                .filter(value -> name.equals(value.name)).findFirst()
                .orElse(null);
    }
}
