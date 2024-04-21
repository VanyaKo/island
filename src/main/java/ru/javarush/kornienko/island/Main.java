package ru.javarush.kornienko.island;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javarush.kornienko.island.configs.IslandConfig;
import ru.javarush.kornienko.island.configs.EatingProbabilityConfig;
import ru.javarush.kornienko.island.consts.Consts;
import ru.javarush.kornienko.island.models.enums.Direction;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;
import ru.javarush.kornienko.island.services.MoveService;
import ru.javarush.kornienko.island.services.PrototypeFactory;
import ru.javarush.kornienko.island.services.impls.ChooseDirectionService;
import ru.javarush.kornienko.island.services.impls.MoveServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        PrototypeFactory prototypeFactory = new PrototypeFactory(objectMapper);
        EatingProbabilityConfig eatingProbabilityConfig = new EatingProbabilityConfig(objectMapper, Consts.EATING_PROBABILITY_CONFIG);

        Properties properties = new Properties();
        IslandConfig islandConfig = new IslandConfig( properties, Consts.ISLAND_CONFIG);
        Island island = new Island(islandConfig, prototypeFactory);

        System.out.println(prototypeFactory.getPrototypes().stream()
                .mapToInt(Organism::getMaxCountOnCell)
                .sum());

        island.initEmptyIsland();
        island.placeOrganisms();


        doStep(island);
    }

    private static void doStep(Island island) {
        eat();
        move(island);
        reproduce();
    }

    private static void reproduce() {

    }

    private static void eat() {

    }

    private static void move(Island island) {
        ChooseDirectionService chooseDirectionService = new ChooseDirectionService();
        MoveService moveService = new MoveServiceImpl(island);
        for(Map.Entry<Cell, List<Organism>> cellListEntry : island.getIslandMap().entrySet()) {
            Cell cell = cellListEntry.getKey();
            List<Animal> animals = cellListEntry.getValue()
                    .stream()
                    .filter(organism -> organism instanceof Animal)
                    .map(organism -> (Animal) organism)
                    .toList();
            for(Animal animal : animals) {
                byte maxSpeed = animal.getMaxSpeed();
                Direction direction = chooseDirectionService.chooseDirection();
                moveService.move(animal, cell, direction, maxSpeed);
            }
        }
    }
}