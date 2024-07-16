package ru.javarush.kornienko.island.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import ru.javarush.kornienko.island.conditions.Handler;
import ru.javarush.kornienko.island.conditions.enums.HandlerType;
import ru.javarush.kornienko.island.configs.IslandConfig;
import ru.javarush.kornienko.island.configs.PrototypeFactory;
import ru.javarush.kornienko.island.configs.animals.EatConfig;
import ru.javarush.kornienko.island.configs.animals.EatConfigDeserializer;
import ru.javarush.kornienko.island.configs.animals.MapConfigDeserializer;
import ru.javarush.kornienko.island.configs.animals.ReproduceConfig;
import ru.javarush.kornienko.island.configs.animals.ReproduceConfigDeserializer;
import ru.javarush.kornienko.island.consts.Consts;
import ru.javarush.kornienko.island.entities.abstracts.Animal;
import ru.javarush.kornienko.island.entities.abstracts.Organism;
import ru.javarush.kornienko.island.entities.island.Cell;
import ru.javarush.kornienko.island.entities.island.Island;
import ru.javarush.kornienko.island.exceptions.AppException;
import ru.javarush.kornienko.island.services.FileService;
import ru.javarush.kornienko.island.services.Logger;
import ru.javarush.kornienko.island.services.actions.ActionService;
import ru.javarush.kornienko.island.services.actions.CollectClassesService;
import ru.javarush.kornienko.island.services.actions.DieService;
import ru.javarush.kornienko.island.services.actions.EatService;
import ru.javarush.kornienko.island.services.actions.MoveService;
import ru.javarush.kornienko.island.services.actions.ReproduceService;
import ru.javarush.kornienko.island.services.statistics.LongInfoType;
import ru.javarush.kornienko.island.services.statistics.ShortInfoType;
import ru.javarush.kornienko.island.services.statistics.StatisticsService;

import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IslandController {
    public void run(String condition) {
        try {
            Handler handler = HandlerType.getHandlerByName(condition);

            ObjectMapper objectMapper = new ObjectMapper();
            PrototypeFactory prototypeFactory = new PrototypeFactory(objectMapper);

            EatConfigDeserializer eatConfigDeserializer = new EatConfigDeserializer(objectMapper,
                    Consts.EAT_CONFIG_JSON);
            MapConfigDeserializer mapConfigDeserializer = new MapConfigDeserializer();
            ReproduceConfigDeserializer reproduceConfigDeserializer = new ReproduceConfigDeserializer(objectMapper,
                    Consts.REPRODUCE_CONFIG_JSON);

            EatConfig[] eatConfigs = eatConfigDeserializer.readEatConfig();
            Map<Class<?>, Integer> moveConfig = mapConfigDeserializer.readMoveConfig(objectMapper,
                    Consts.MOVE_CONFIG_JSON);
            Map<Class<?>, Integer> startAnimalNumConfig = mapConfigDeserializer.readMoveConfig(objectMapper,
                    Consts.START_ANIMAL_NUM_CONFIG_JSON);
            ReproduceConfig[] reproduceConfigs = reproduceConfigDeserializer.readReproduceConfig();

            Island island = initIsland(prototypeFactory, startAnimalNumConfig);
            startGameCycle(handler, prototypeFactory, island, eatConfigs, moveConfig, reproduceConfigs);
        } catch(Exception e) {
            FileService fileService = new FileService();
            Logger logger = new Logger(fileService);
            logger.logException(e);
            System.out.println(e.getLocalizedMessage());
        }
    }

    private @NotNull Island getIsland(PrototypeFactory prototypeFactory) {
        Properties properties = new Properties();
        IslandConfig islandConfig = new IslandConfig(properties, Consts.ISLAND_CONFIG);
        return new Island(islandConfig, prototypeFactory);
    }

    private @NotNull Island initIsland(PrototypeFactory prototypeFactory, Map<Class<?>, Integer> startAnimalNumConfig) {
        Island island = getIsland(prototypeFactory);
        island.initEmptyIsland();
        island.growPlants();
        island.initAnimals(startAnimalNumConfig);
        return island;
    }

    private void startGameCycle(Handler handler, PrototypeFactory prototypeFactory, Island island,
                                EatConfig[] eatConfigs, Map<Class<?>, Integer> moveConfig,
                                ReproduceConfig[] reproduceConfigs) {
        int cycleCounter = 0;
        int cellCount = island.getCellCount();
        int millisPerCycle = island.getCycleDuration();

        CollectClassesService collectClassesService = new CollectClassesService(island);
        ReproduceService reproduceService = new ReproduceService(island, reproduceConfigs);
        EatService eatService = new EatService(island, eatConfigs);
        MoveService moveService = new MoveService(island, moveConfig);
        DieService dieService = new DieService(island);
        StatisticsService statisticsService = new StatisticsService(prototypeFactory);

        do {
            ExecutorService growPlantsExecutor = Executors.newSingleThreadExecutor();
            ExecutorService reproduceExecutor = Executors.newFixedThreadPool(cellCount);
            ExecutorService eatExecutor = Executors.newFixedThreadPool(cellCount);
            ExecutorService dieExecutor = Executors.newFixedThreadPool(cellCount);
            ExecutorService moveExecutor = Executors.newSingleThreadExecutor();

            System.out.println(Consts.LINE_DELIMITER + "\n");
            System.out.println("Выполняется такт " + ++cycleCounter + "...\n");

            Map<Class<? extends Organism>, Long> initialInfo = collectClassesService.getClassesToCountMap();
            resetChangeRecord(island, eatService, moveService, reproduceService, dieService);

            growPlantsExecutor.submit(island::growPlants);
            for(Map.Entry<Cell, Set<Organism>> cellToOrganismsEntry : island.getIslandMap().entrySet()) {
                reproduceExecutor.submit(() -> reproduceService.reproduceCellOrganisms(cellToOrganismsEntry));
                eatExecutor.submit(() -> eatService.eatCellAnimals(cellToOrganismsEntry));
                dieExecutor.submit(() -> dieService.killCellHungryAnimals(cellToOrganismsEntry));
            }
            for(Map.Entry<Animal, Cell> animalToCellEntry : moveService.getAnimalsToMove().entrySet()) {
                moveExecutor.submit(() -> moveService.moveCellAnimalOnCell(animalToCellEntry));
            }

            try {
                Thread.sleep(millisPerCycle);
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new AppException(e);
            }

            shutdownNow(growPlantsExecutor, eatExecutor, reproduceExecutor, dieExecutor, moveExecutor);

            Map<Class<? extends Organism>, Long> grownPlantClassToCount = island.getGrownPlantClassToCount();
            Map<Class<? extends Organism>, Long> newbornClassToCountMap = reproduceService.getOrganismClassCountMap();
            Map<Class<? extends Organism>, Long> eatenOrganismClassToCount = eatService.getOrganismClassCountMap();
            Map<Class<? extends Organism>, Long> movedOrganismClassToCount = moveService.getOrganismClassCountMap();
            Map<Class<? extends Organism>, Long> diedAnimalToCountMap = dieService.getOrganismClassCountMap();

            statisticsService.printLongInfo(initialInfo, LongInfoType.OVERALL_INFO);
            statisticsService.printShortInfo(grownPlantClassToCount, ShortInfoType.GROWN_INFO);
            statisticsService.printShortInfo(newbornClassToCountMap, ShortInfoType.REPRODUCE_INFO);
            statisticsService.printLongInfo(eatenOrganismClassToCount, LongInfoType.EAT_INFO);
            statisticsService.printShortInfo(movedOrganismClassToCount, ShortInfoType.MOVE_INFO);
            statisticsService.printShortInfo(diedAnimalToCountMap, ShortInfoType.DIE_INFO);
            statisticsService.printDifferenceInfo(initialInfo, collectClassesService.getClassesToCountMap());

        } while(!handler.isConditionSatisfied(island));

        System.out.println("\n" + Consts.LINE_DELIMITER + "\n");
        System.out.println(handler.getConditionTrueMessage());
        System.out.println("Симуляция \uD83C\uDFDD️ завершена!");
    }

    private void resetChangeRecord(Island island, ActionService... actionServices) {
        island.resetGrownOrganismsMap();
        Arrays.stream(actionServices).forEach(ActionService::resetOrganismClassToCountMap);
    }

    private void shutdownNow(ExecutorService... executorServices) {
        Arrays.stream(executorServices).forEach(ExecutorService::shutdownNow);
    }
}
