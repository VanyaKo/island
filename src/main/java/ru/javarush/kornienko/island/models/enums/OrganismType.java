package ru.javarush.kornienko.island.models.enums;

import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.herbivores.Buffalo;
import ru.javarush.kornienko.island.models.herbivores.Caterpillar;
import ru.javarush.kornienko.island.models.herbivores.Deer;
import ru.javarush.kornienko.island.models.herbivores.Duck;
import ru.javarush.kornienko.island.models.herbivores.Goat;
import ru.javarush.kornienko.island.models.herbivores.Hog;
import ru.javarush.kornienko.island.models.herbivores.Horse;
import ru.javarush.kornienko.island.models.herbivores.Mouse;
import ru.javarush.kornienko.island.models.herbivores.Rabbit;
import ru.javarush.kornienko.island.models.plants.Plants;
import ru.javarush.kornienko.island.models.predators.Bear;
import ru.javarush.kornienko.island.models.predators.Eagle;
import ru.javarush.kornienko.island.models.predators.Fox;
import ru.javarush.kornienko.island.models.predators.Python;
import ru.javarush.kornienko.island.models.predators.Wolf;

import java.util.Arrays;

public enum OrganismType {
    WOLF("wolf", Wolf.class),
    PYTHON("python", Python.class),
    FOX("fox", Fox.class),
    BEAR("bear", Bear.class),
    EAGLE("eagle", Eagle.class),
    HORSE("horse", Horse.class),
    DEER("deer", Deer.class),
    RABBIT("rabbit", Rabbit.class),
    MOUSE("mouse", Mouse.class),
    GOAT("goat", Goat.class),
    HOG("hog", Hog.class),
    BUFFALO("buffalo", Buffalo.class),
    DUCK("duck", Duck.class),
    CATERPILLAR("caterpillar", Caterpillar.class),
    PLANTS("plants", Plants.class);

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
