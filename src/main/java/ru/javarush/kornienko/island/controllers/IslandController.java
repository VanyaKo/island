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
import ru.javarush.kornienko.island.exceptions.AppException;
import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.services.CollectClassesService;
import ru.javarush.kornienko.island.services.DieService;
import ru.javarush.kornienko.island.services.EatService;
import ru.javarush.kornienko.island.services.MoveService;
import ru.javarush.kornienko.island.services.ReproduceService;
import ru.javarush.kornienko.island.services.StatisticsService;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class IslandController {
    private @NotNull Island getIsland(PrototypeFactory prototypeFactory) {
        Properties properties = new Properties();
        IslandConfig islandConfig = new IslandConfig(properties, Consts.ISLAND_CONFIG);
        return new Island(islandConfig, prototypeFactory);
    }

    public void run(String condition) {
        Handler handler = HandlerType.getHandlerByName(condition);

        ObjectMapper objectMapper = new ObjectMapper();
        PrototypeFactory prototypeFactory = new PrototypeFactory(objectMapper);

        EatConfigDeserializer eatConfigDeserializer = new EatConfigDeserializer(objectMapper,
                Consts.EAT_CONFIG_JSON);
        EatConfig[] eatConfigs = eatConfigDeserializer.readEatConfig();

        MoveConfigDeserializer moveConfigDeserializer = new MoveConfigDeserializer(objectMapper,
                Consts.MOVE_CONFIG_JSON);
        Map<Class<?>, Byte> moveConfig = moveConfigDeserializer.readMoveConfig();

        ReproduceConfigDeserializer reproduceConfigDeserializer = new ReproduceConfigDeserializer(objectMapper,
                Consts.REPRODUCE_CONFIG_JSON);
        ReproduceConfig[] reproduceConfigs = reproduceConfigDeserializer.readReproduceConfig();

        Island island = getIsland(prototypeFactory);
        island.initEmptyIsland();
        island.placePlants();
        island.placeAnimals();

        startGameCycle(handler, prototypeFactory, island, eatConfigs, moveConfig, reproduceConfigs);
    }

    private void startGameCycle(Handler handler, PrototypeFactory prototypeFactory, Island island,
                                EatConfig[] eatConfigs, Map<Class<?>, Byte> moveConfig,
                                ReproduceConfig[] reproduceConfigs) {
        int cycleCounter = 0;
        int cellCount = island.getCellCount();
        int cycleSeconds = island.getCycleDuration();

        CollectClassesService collectClassesService = new CollectClassesService(island);
        StatisticsService statisticsService = new StatisticsService(prototypeFactory);
        EatService eatService = new EatService(island, eatConfigs);
        MoveService moveService = new MoveService(island, moveConfig);
        ReproduceService reproduceService = new ReproduceService(island, reproduceConfigs);
        DieService dieService = new DieService(island);

        ExecutorService collectInfoExecutor = Executors.newSingleThreadExecutor();
        ExecutorService placePlantsExecutor = Executors.newSingleThreadExecutor();
        ExecutorService statisticsExecutor = Executors.newSingleThreadExecutor();
        ExecutorService eatExecutor = Executors.newFixedThreadPool(cellCount);
        ExecutorService reproduceExecutor = Executors.newFixedThreadPool(cellCount);
        ExecutorService dieExecutor = Executors.newFixedThreadPool(cellCount);
        do {
            try {
                System.out.println(Consts.LINE_DELIMITER + "\n");
                System.out.println("ТАКТ " + cycleCounter++ + "\n");

                ExecutorService moveExecutor = Executors.newFixedThreadPool(moveService.getAnimalsToMove().size());
                eatService.resetEatenOrganismsMap();
                moveService.resetMovedOrganismsMap();
                reproduceService.resetNewbornClassToCountMap();
                dieService.resetDiedAndSurvivedOrganisms();

                // submit tasks
                Future<Map<Class<? extends Organism>, Long>> initialInfoFuture = collectInfoExecutor.submit(collectClassesService::getClassesToCountMap);
                Future<Long> placePlantsFuture = placePlantsExecutor.submit(island::placePlants);
                for(Map.Entry<Cell, Set<Organism>> cellToOrganismsEntry : island.getIslandMap().entrySet()) {
                    eatExecutor.submit(() -> eatService.eatCellAnimals(cellToOrganismsEntry));
                    reproduceExecutor.submit(() -> reproduceService.reproduceCellOrganisms(cellToOrganismsEntry));
                    dieExecutor.submit(() -> dieService.killCellHungryAnimals(cellToOrganismsEntry));
                }
                for(Map.Entry<Animal, Cell> animalToCellEntry : moveService.getAnimalsToMove().entrySet()) {
                    moveExecutor.submit(() -> moveService.moveCellAnimals(animalToCellEntry));
                }

                // collect results
                Map<Class<? extends Organism>, Long> initialInfo = initialInfoFuture.get(cycleSeconds, TimeUnit.SECONDS);
                long plantsGrown = placePlantsFuture.get(cycleSeconds, TimeUnit.SECONDS);
                ConcurrentMap<Class<? extends Organism>, Long> eatenOrganismClassToCount = eatService.getEatenOrganismClassCount();
                ConcurrentMap<Class<? extends Organism>, Long> movedOrganismClassToCount = moveService.getMovedOrganismClassToCount();
                Map<Class<? extends Organism>, Long> diedAnimalToCountMap = dieService.getDiedAnimals();

                // print statistics
                statisticsExecutor.submit(() -> statisticsService.printCurrentOrganismInfo(initialInfo));
                statisticsExecutor.submit(() -> statisticsService.printPlantsGrownInfo(plantsGrown));
                statisticsExecutor.submit(() -> statisticsService.printEatInfo(eatenOrganismClassToCount));
                statisticsExecutor.submit(() -> statisticsService.printMoveInfo(movedOrganismClassToCount));
                statisticsExecutor.submit(() -> statisticsService.printReproduceInfo(reproduceService.getNewbornClassToCountMap()));
                statisticsExecutor.submit(() -> statisticsService.printDieInfo(diedAnimalToCountMap));
                statisticsExecutor.submit(() ->
                        statisticsService.printDifferenceInfo(initialInfo, collectClassesService.getClassesToCountMap()));
            } catch(InterruptedException | ExecutionException | TimeoutException e) {
                throw new AppException(e);
            }
        } while(!handler.isConditionSatisfied(island));
        System.out.println("\n" + Consts.LINE_DELIMITER + "\n");
        System.out.println(handler.getConditionTrueMessage());
        System.out.println("Симуляция \uD83C\uDFDD\uFE0F завершена!");
    }

}
