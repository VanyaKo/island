package ru.javarush.kornienko.island;

import ru.javarush.kornienko.island.controllers.IslandController;

public class MainRunner {

    public static void main(String[] args) {
        IslandController islandController = new IslandController();
        islandController.run();
    }
}