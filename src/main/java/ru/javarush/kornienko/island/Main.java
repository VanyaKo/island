package ru.javarush.kornienko.island;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javarush.kornienko.island.configs.EatingProbabilityConfig;
import ru.javarush.kornienko.island.configs.IslandConfig;
import ru.javarush.kornienko.island.configs.ProbabilityPair;
import ru.javarush.kornienko.island.consts.Consts;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.island.Cell;
import ru.javarush.kornienko.island.models.island.Island;
import ru.javarush.kornienko.island.services.DieService;
import ru.javarush.kornienko.island.services.EatService;
import ru.javarush.kornienko.island.services.MoveService;
import ru.javarush.kornienko.island.configs.PrototypeFactory;
import ru.javarush.kornienko.island.services.ReproduceService;
import ru.javarush.kornienko.island.services.impls.DieServiceImpl;
import ru.javarush.kornienko.island.services.impls.EatServiceImpl;
import ru.javarush.kornienko.island.services.impls.MoveServiceImpl;
import ru.javarush.kornienko.island.services.impls.ReproduceServiceImpl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        ObjectMapper objectMapper = new ObjectMapper();
        PrototypeFactory prototypeFactory = new PrototypeFactory(objectMapper);
        EatingProbabilityConfig eatingProbabilityConfig = new EatingProbabilityConfig(objectMapper, Consts.EATING_PROBABILITY_CONFIG);
        ProbabilityPair[] probabilityPairs = eatingProbabilityConfig.readEatingProbability();

        Properties properties = new Properties();
        IslandConfig islandConfig = new IslandConfig(properties, Consts.ISLAND_CONFIG);
        Island island = new Island(islandConfig, prototypeFactory);
        island.initEmptyIsland();
        island.placeOrganisms();

        main.doStep(island, probabilityPairs);
    }



    private void doStep(Island island, ProbabilityPair[] probabilityPairs) {
        // eat
        EatService eatService = new EatServiceImpl(island, probabilityPairs);
        long eatenOrganismCount = eatService.eatIslandOrganisms();
        System.out.println("Всего поело " + eatenOrganismCount + " животных.");

        // reproduce
        ReproduceService reproduceService = new ReproduceServiceImpl(island);
        long newbornAnimalCount = reproduceService.reproduceIslandAnimals();
        System.out.println("Все размножилось " + newbornAnimalCount + " животных.");

        // move
        MoveService moveService = new MoveServiceImpl(island);
        long movedAnimalCount = moveService.moveIslandAnimals();
        System.out.println("Все переместилось " + movedAnimalCount + " животных.");

        // die if hungry
        DieService dieService = new DieServiceImpl(island);
        long diedFromHungerAnimalCount = dieService.killHungryIslandAnimals();
        System.out.println("Умерло от голода " + diedFromHungerAnimalCount + " животных.");
    }
}