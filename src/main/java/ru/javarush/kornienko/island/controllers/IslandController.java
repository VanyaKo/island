package ru.javarush.kornienko.island.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import ru.javarush.kornienko.island.conditions.Handler;
import ru.javarush.kornienko.island.conditions.enums.HandlerType;
import ru.javarush.kornienko.island.configs.IslandConfig;
import ru.javarush.kornienko.island.configs.PrototypeFactory;
import ru.javarush.kornienko.island.configs.action.EatProbabilityConfig;
import ru.javarush.kornienko.island.configs.action.EatProbabilityPair;
import ru.javarush.kornienko.island.configs.action.MoveProbabilityConfig;
import ru.javarush.kornienko.island.configs.action.ReproduceProbabilityConfig;
import ru.javarush.kornienko.island.configs.action.ReproduceProbabilityEntry;
import ru.javarush.kornienko.island.consts.Consts;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.services.CollectClassesService;
import ru.javarush.kornienko.island.services.DieService;
import ru.javarush.kornienko.island.services.EatService;
import ru.javarush.kornienko.island.services.MoveService;
import ru.javarush.kornienko.island.services.ReproduceService;
import ru.javarush.kornienko.island.services.StatisticsService;
import ru.javarush.kornienko.island.services.impls.CollectClassesServiceImpl;
import ru.javarush.kornienko.island.services.impls.DieServiceImpl;
import ru.javarush.kornienko.island.services.impls.EatServiceImpl;
import ru.javarush.kornienko.island.services.impls.MoveServiceImpl;
import ru.javarush.kornienko.island.services.impls.ReproduceServiceImpl;
import ru.javarush.kornienko.island.services.impls.StatisticsServiceImpl;

import java.util.Map;
import java.util.Properties;

public class IslandController {
    private static @NotNull Island getIsland(PrototypeFactory prototypeFactory) {
        Properties properties = new Properties();
        IslandConfig islandConfig = new IslandConfig(properties, Consts.ISLAND_CONFIG);
        Island island = new Island(islandConfig, prototypeFactory);
        island.initEmptyIsland();
        island.placeAnimals();
        return island;
    }

    public void run(String condition) {
        Handler handler = HandlerType.getHandlerByName(condition);

        ObjectMapper objectMapper = new ObjectMapper();
        PrototypeFactory prototypeFactory = new PrototypeFactory(objectMapper);

        EatProbabilityConfig eatProbabilityConfig = new EatProbabilityConfig(objectMapper,
                Consts.EAT_PROBABILITY_CONFIG_JSON);
        EatProbabilityPair[] eatProbabilityPairs = eatProbabilityConfig.readEatingProbability();

        MoveProbabilityConfig moveProbabilityConfig = new MoveProbabilityConfig(objectMapper,
                Consts.MOVE_PROBABILITY_CONFIG_JSON);
        Map<Class<?>, Integer> classToMoveProbability = moveProbabilityConfig.readMoveProbability();

        ReproduceProbabilityConfig reproduceProbabilityConfig = new ReproduceProbabilityConfig(objectMapper,
                Consts.REPRODUCE_PROBABILITY_CONFIG_JSON);
        ReproduceProbabilityEntry[] reproduceProbabilityEntries = reproduceProbabilityConfig.readReproduceProbability();

        Island island = getIsland(prototypeFactory);

        startGameCycle(handler, prototypeFactory, island, eatProbabilityPairs, classToMoveProbability, reproduceProbabilityEntries);
    }

    private void startGameCycle(Handler handler, PrototypeFactory prototypeFactory, Island island,
                                EatProbabilityPair[] eatProbabilityPairs, Map<Class<?>, Integer> classToMoveProbability,
                                ReproduceProbabilityEntry[] reproduceProbabilityEntries) {
        int cycleCounter = 0;
        do {
            StatisticsService statisticsService = new StatisticsServiceImpl(prototypeFactory);
            System.out.println(Consts.LINE_DELIMITER);
            System.out.println();
            System.out.println("ТАКТ " + cycleCounter++ + "\n");

            System.out.println("Выросло " + island.placePlants() + " растений.\n");

            // print current organism info
            CollectClassesService collectClassesService = new CollectClassesServiceImpl(island);
            Map<Class<? extends Organism>, Long> initialClassesToCount = collectClassesService.getClassesToCountMap();
            statisticsService.printCurrentOrganismInfo(initialClassesToCount);

            // eat
            EatService eatService = new EatServiceImpl(island, eatProbabilityPairs);
            Map<Class<? extends Organism>, Long> eatenOrganismClassCount = eatService.eatIslandOrganisms();
            statisticsService.printEatInfo(eatenOrganismClassCount);

            // reproduce
            ReproduceService reproduceService = new ReproduceServiceImpl(island, reproduceProbabilityEntries);
            Map<Class<? extends Organism>, Long> newbornAnimalClassToCount = reproduceService.reproduceIslandAnimals();
            statisticsService.printReproduceInfo(newbornAnimalClassToCount);

            // move
            MoveService moveService = new MoveServiceImpl(island, classToMoveProbability);
            Map<Class<? extends Organism>, Long> movedOrganismClassToCount = moveService.moveIslandAnimals();
            statisticsService.printMoveInfo(movedOrganismClassToCount);

            // die if hungry
            DieService dieService = new DieServiceImpl(island);
            Map<Class<? extends Organism>, Long> diedOrganismClassToCount = dieService.killHungryIslandAnimals();
            statisticsService.printDieInfo(diedOrganismClassToCount);

            // compute difference
            // print current organism info
            statisticsService.printDifferenceInfo(initialClassesToCount, collectClassesService.getClassesToCountMap());
        } while(!handler.isConditionSatisfied(island));
        System.out.println("\n" + Consts.LINE_DELIMITER + "\n");
        System.out.println(handler.getConditionTrueMessage());
        System.out.println("Симуляция \uD83C\uDFDD\uFE0F завершена!");
    }
}
