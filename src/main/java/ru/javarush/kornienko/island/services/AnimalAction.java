package ru.javarush.kornienko.island.services;

import ru.javarush.kornienko.island.models.abstracts.Organism;

public interface AnimalAction {
    void eat();
    Organism reproduce();
    void move();
}
