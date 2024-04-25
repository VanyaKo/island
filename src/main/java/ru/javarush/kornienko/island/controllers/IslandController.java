package ru.javarush.kornienko.island.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import ru.javarush.kornienko.island.conditions.Handler;
import ru.javarush.kornienko.island.conditions.enums.HandlerType;
import ru.javarush.kornienko.island.configs.IslandConfig;
import ru.javarush.kornienko.island.configs.PrototypeFactory;
import ru.javarush.kornienko.island.configs.animals.EatConfigDeserializer;
import ru.javarush.kornienko.island.configs.animals.EatConfig;
import ru.javarush.kornienko.island.configs.animals.MoveConfigDeserializer;
import ru.javarush.kornienko.island.configs.animals.ReproduceConfigDeserializer;
import ru.javarush.kornienko.island.configs.animals.ReproduceConfig;
import ru.javarush.kornienko.island.consts.Consts;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.services.CollectClassesService;
import ru.javarush.kornienko.island.services.DieService;
import ru.javarush.kornienko.island.services.EatService;
import ru.javarush.kornienko.island.services.MoveService;
import ru.javarush.kornienko.island.services.ReproduceService;
import ru.javarush.kornienko.island.services.StatisticsService;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

        EatConfigDeserializer eatConfigDeserializer = new EatConfigDeserializer(objectMapper,
                Consts.EAT_PROBABILITY_CONFIG_JSON);
        EatConfig[] eatConfigs = eatConfigDeserializer.readEatingProbability();

        MoveConfigDeserializer moveConfigDeserializer = new MoveConfigDeserializer(objectMapper,
                Consts.MOVE_PROBABILITY_CONFIG_JSON);
        Map<Class<?>, Byte> classToMoveProbability = moveConfigDeserializer.readMoveConfig();

        ReproduceConfigDeserializer reproduceConfigDeserializer = new ReproduceConfigDeserializer(objectMapper,
                Consts.REPRODUCE_PROBABILITY_CONFIG_JSON);
        ReproduceConfig[] reproduceProbabilityEntries = reproduceConfigDeserializer.readReproduceProbability();

        Island island = getIsland(prototypeFactory);

        startGameCycle(handler, prototypeFactory, island, eatConfigs, classToMoveProbability, reproduceProbabilityEntries);
    }

    private void startGameCycle(Handler handler, PrototypeFactory prototypeFactory, Island island,
                                EatConfig[] eatConfigs, Map<Class<?>, Byte> classToMoveProbability,
                                ReproduceConfig[] reproduceProbabilityEntries) {
        int cycleCounter = 0;
        do {
            StatisticsService statisticsService = new StatisticsService(prototypeFactory);
            System.out.println(Consts.LINE_DELIMITER);
            System.out.println();
            System.out.println("ТАКТ " + cycleCounter++ + "\n");
            int cycleSeconds = 5;

            ExecutorService placePlantsExecutor = Executors.newSingleThreadExecutor();
            Future<Long> placePlantsFuture = placePlantsExecutor.submit(island::placePlants);
            System.out.println("Выросло " + island.placePlants() + " растений.\n");

            // print current organism info
            CollectClassesService collectClassesService = new CollectClassesService(island);
            Map<Class<? extends Organism>, Long> initialClassesToCount = collectClassesService.getClassesToCountMap();
            statisticsService.printCurrentOrganismInfo(initialClassesToCount);

            // eat
            EatService eatService = new EatService(island, eatConfigs);
            Map<Class<? extends Organism>, Long> eatenOrganismClassCount = eatService.eatIslandOrganisms();
            statisticsService.printEatInfo(eatenOrganismClassCount);

            // reproduce
            ReproduceService reproduceService = new ReproduceService(island, reproduceProbabilityEntries);
            Map<Class<? extends Organism>, Long> newbornAnimalClassToCount = reproduceService.reproduceIslandAnimals();
            statisticsService.printReproduceInfo(newbornAnimalClassToCount);

            // move
            MoveService moveService = new MoveService(island, classToMoveProbability);
            Map<Class<? extends Organism>, Long> movedOrganismClassToCount = moveService.moveIslandAnimals();
            statisticsService.printMoveInfo(movedOrganismClassToCount);

            // die if hungry
            DieService dieService = new DieService(island);
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
