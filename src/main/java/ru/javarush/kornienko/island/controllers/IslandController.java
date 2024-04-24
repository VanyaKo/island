package ru.javarush.kornienko.island.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javarush.kornienko.island.conditions.Handler;
import ru.javarush.kornienko.island.conditions.enums.HandlerType;
import ru.javarush.kornienko.island.configs.EatingProbabilityConfig;
import ru.javarush.kornienko.island.configs.IslandConfig;
import ru.javarush.kornienko.island.configs.ProbabilityPair;
import ru.javarush.kornienko.island.configs.PrototypeFactory;
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
    public void run(String condition) {
        Handler handler = HandlerType.getHandlerByName(condition);

        ObjectMapper objectMapper = new ObjectMapper();
        PrototypeFactory prototypeFactory = new PrototypeFactory(objectMapper);
        EatingProbabilityConfig eatingProbabilityConfig = new EatingProbabilityConfig(objectMapper, Consts.EATING_PROBABILITY_CONFIG);
        ProbabilityPair[] probabilityPairs = eatingProbabilityConfig.readEatingProbability();

        Properties properties = new Properties();
        IslandConfig islandConfig = new IslandConfig(properties, Consts.ISLAND_CONFIG);
        Island island = new Island(islandConfig, prototypeFactory);
        island.initEmptyIsland();
        island.placeAnimals();

        startGameCycle(handler, prototypeFactory, island, probabilityPairs);
    }

    private void startGameCycle(Handler handler, PrototypeFactory prototypeFactory, Island island, ProbabilityPair[] probabilityPairs) {
        int cycleCounter = 0;
        while(!handler.isConditionSatisfied(island)) {
            StatisticsService statisticsService = new StatisticsServiceImpl(prototypeFactory);
            System.out.println("ТАКТ " + cycleCounter + "\n");

            System.out.println("Выросло " + island.placePlants() + " растений.\n");

            // print current organism info
            CollectClassesService collectClassesService = new CollectClassesServiceImpl(island);
            Map<Class<? extends Organism>, Long> initialClassesToCount = collectClassesService.getClassesToCountMap();
            statisticsService.printCurrentOrganismInfo(initialClassesToCount);

            // eat
            EatService eatService = new EatServiceImpl(island, probabilityPairs);
            Map<Class<? extends Organism>, Long> eatenOrganismClassCount = eatService.eatIslandOrganisms();
            statisticsService.printEatInfo(eatenOrganismClassCount);

            // reproduce
            ReproduceService reproduceService = new ReproduceServiceImpl(island);
            Map<Class<? extends Organism>, Long> newbornAnimalClassToCount = reproduceService.reproduceIslandAnimals();
            statisticsService.printReproduceInfo(newbornAnimalClassToCount);

            // move
            MoveService moveService = new MoveServiceImpl(island);
            Map<Class<? extends Organism>, Long> movedOrganismClassToCount = moveService.moveIslandAnimals();
            statisticsService.printMoveInfo(movedOrganismClassToCount);

            // die if hungry
            DieService dieService = new DieServiceImpl(island);
            Map<Class<? extends Organism>, Long> diedOrganismClassToCount = dieService.killHungryIslandAnimals();
            statisticsService.printDieInfo(diedOrganismClassToCount);

            // compute difference
            // print current organism info
            statisticsService.printDifferenceInfo(initialClassesToCount, collectClassesService.getClassesToCountMap());

            System.out.println();
        }
    }
}
