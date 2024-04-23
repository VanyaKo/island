package ru.javarush.kornienko.island;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javarush.kornienko.island.configs.EatingProbabilityConfig;
import ru.javarush.kornienko.island.configs.IslandConfig;
import ru.javarush.kornienko.island.configs.ProbabilityPair;
import ru.javarush.kornienko.island.consts.Consts;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.services.MoveService;
import ru.javarush.kornienko.island.configs.PrototypeFactory;
import ru.javarush.kornienko.island.services.impls.EatServiceImpl;
import ru.javarush.kornienko.island.services.impls.MoveServiceImpl;
import ru.javarush.kornienko.island.services.impls.RemoveEatenOrganismService;
import ru.javarush.kornienko.island.services.impls.ReproduceService;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        PrototypeFactory prototypeFactory = new PrototypeFactory(objectMapper);
        EatingProbabilityConfig eatingProbabilityConfig = new EatingProbabilityConfig(objectMapper, Consts.EATING_PROBABILITY_CONFIG);
        ProbabilityPair[] probabilityPairs = eatingProbabilityConfig.readEatingProbability();

        Properties properties = new Properties();
        IslandConfig islandConfig = new IslandConfig(properties, Consts.ISLAND_CONFIG);
        Island island = new Island(islandConfig, prototypeFactory);

        System.out.println(prototypeFactory.getPrototypes().stream()
                .mapToInt(Organism::getMaxCountOnCell)
                .sum());

        island.initEmptyIsland();
        island.placeOrganisms();

        EatServiceImpl eatServiceImpl = new EatServiceImpl(island, probabilityPairs);

        Map<Cell, List<Organism>> islandMap = island.getIslandMap();
        eatServiceImpl.eat();

        // eat
        RemoveEatenOrganismService removeEatenOrganismService = new RemoveEatenOrganismService();
//        removeEatenOrganismService.removeEatenOrganisms(islandMap, eatenOrganisms);

        // reproduce
        ReproduceService reproduceService = new ReproduceService();
        reproduceService.reproduceAnimalsOnIsland(islandMap, island.getMaxAnimalsPerCell());

        // move
        MoveService moveService = new MoveServiceImpl(island);
        moveService.moveIslandAnimals();
        doStep(island);
    }



    private static void doStep(Island island) {
//        eat();
//        reproduce();
//        move(island);
    }

//    private static void move(Island island) {
//        ChooseDirectionService chooseDirectionService = new ChooseDirectionService();
//        MoveService moveService = new MoveServiceImpl(island);
//        for(Map.Entry<Cell, List<Organism>> cellListEntry : island.getIslandMap().entrySet()) {
//            Cell cell = cellListEntry.getKey();
//            List<Animal> animals = cellListEntry.getValue()
//                    .stream()
//                    .filter(organism -> organism instanceof Animal)
//                    .map(organism -> (Animal) organism)
//                    .toList();
//            for(Animal animal : animals) {
//                byte maxSpeed = animal.getMaxSpeed();
//                Direction direction = chooseDirectionService.chooseDirection();
//                moveService.move(animal, cell, direction, maxSpeed);
//            }
//        }
//    }
}