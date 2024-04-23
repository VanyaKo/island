package ru.javarush.kornienko.island.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javarush.kornienko.island.configs.EatingProbabilityConfig;
import ru.javarush.kornienko.island.configs.IslandConfig;
import ru.javarush.kornienko.island.configs.ProbabilityPair;
import ru.javarush.kornienko.island.configs.PrototypeFactory;
import ru.javarush.kornienko.island.consts.Consts;
import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.models.plants.Plant;
import ru.javarush.kornienko.island.models.predators.Predator;
import ru.javarush.kornienko.island.models.predators.Wolf;
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
    public void run() {
        ObjectMapper objectMapper = new ObjectMapper();
        PrototypeFactory prototypeFactory = new PrototypeFactory(objectMapper);
        EatingProbabilityConfig eatingProbabilityConfig = new EatingProbabilityConfig(objectMapper, Consts.EATING_PROBABILITY_CONFIG);
        ProbabilityPair[] probabilityPairs = eatingProbabilityConfig.readEatingProbability();

        Properties properties = new Properties();
        IslandConfig islandConfig = new IslandConfig(properties, Consts.ISLAND_CONFIG);
        Island island = new Island(islandConfig, prototypeFactory);
        island.initEmptyIsland();
        island.placeAnimals();

        startGameCycle(prototypeFactory, island, probabilityPairs);
    }

    private void startGameCycle(PrototypeFactory prototypeFactory, Island island, ProbabilityPair[] probabilityPairs) {
        int cycleCounter = 0;
        for(int i = 0; i < 6; i++) {
            StatisticsService statisticsService = new StatisticsServiceImpl(prototypeFactory);
            System.out.println("НАЧАЛО ТАКТА #" + cycleCounter + "\n");

            island.placePlants();

            // print current organism info
            CollectClassesService collectClassesService = new CollectClassesServiceImpl(island);
            statisticsService.printCurrentOrganismInfo(collectClassesService.getClassesToCountMap());

            // eat
            EatService eatService = new EatServiceImpl(island, probabilityPairs);
            eatService.eatIslandOrganisms();
            Map<Class<? extends Organism>, Long> eatenOrganismClassCount = ((EatServiceImpl) eatService).getEatenOrganismClassCount();
            statisticsService.printEatInfo(eatenOrganismClassCount);

            // reproduce
            ReproduceService reproduceService = new ReproduceServiceImpl(island);
            reproduceService.reproduceIslandAnimals();
            Map<Class<? extends Organism>, Long> newbornAnimalClassToCount = ((ReproduceServiceImpl) reproduceService).getNewbornAnimalClassCount();
            statisticsService.printReproduceInfo(newbornAnimalClassToCount);

            // move
            MoveService moveService = new MoveServiceImpl(island);
            moveService.moveIslandAnimals();
            Map<Class<? extends Organism>, Long> movedOrganismClassToCount = ((MoveServiceImpl) moveService).getMovedOrganismClassToCount();
            statisticsService.printMoveInfo(movedOrganismClassToCount);


            // die if hungry
            DieService dieService = new DieServiceImpl(island);
            dieService.killHungryIslandAnimals();
            Map<Class<? extends Organism>, Long> diedOrganismClassToCount = ((DieServiceImpl) dieService).getDiedOrganismClassToCount();
            statisticsService.printDieInfo(diedOrganismClassToCount);

            System.out.println("КОНЕЦ ТАКТА #" + cycleCounter++ + "\n\n");
        }
    }
}
