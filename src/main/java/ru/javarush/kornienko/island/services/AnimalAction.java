package ru.javarush.kornienko.island.services;

import ru.javarush.kornienko.island.models.abstracts.Organism;

import java.util.Collection;

public interface AnimalAction {
    void eat(Collection<Organism> eatableOrganisms);
    Organism reproduce();
    void move();
}
