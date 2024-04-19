package ru.javarush.kornienko.island;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javarush.kornienko.island.configs.IslandSizeConfig;
import ru.javarush.kornienko.island.configs.OrganismPropertyConfig;
import ru.javarush.kornienko.island.configs.ProbabilityOfBeingEatenConfig;
import ru.javarush.kornienko.island.models.Island;
import ru.javarush.kornienko.island.models.abstracts.Animal;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.enums.Direction;
import ru.javarush.kornienko.island.models.enums.OrganismType;
import ru.javarush.kornienko.island.models.island.Cell;
import ru.javarush.kornienko.island.models.plants.Plants;
import ru.javarush.kornienko.island.models.predators.Wolf;
import ru.javarush.kornienko.island.services.MoveService;
import ru.javarush.kornienko.island.services.PrototypeFactory;
import ru.javarush.kornienko.island.services.impls.ChooseDirectionService;
import ru.javarush.kornienko.island.services.impls.MoveServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        ProbabilityOfBeingEatenConfig probabilityOfBeingEatenConfig =
                new ProbabilityOfBeingEatenConfig(objectMapper, "configs/probability_of_being_eaten.json");

        Properties properties = new Properties();
        IslandSizeConfig islandSizeConfig = new IslandSizeConfig(properties, "configs/map/island.properties");
        PrototypeFactory prototypeFactory = new PrototypeFactory(objectMapper);
        Random random = new Random();
        ChooseDirectionService chooseDirectionService = new ChooseDirectionService(random);
        Island island = createIslandArea(islandSizeConfig);
        MoveService moveService = new MoveServiceImpl(island);

//        int maxPlantsOnCell = getMaxOrganismsOnCell(organismPropertyConfig, OrganismType.PLANTS);
//        int maxWolvesOnCell = getMaxOrganismsOnCell(organismPropertyConfig, OrganismType.WOLF);

        //fill plants
//        island.getIslandMap().values()
//                .forEach(
//                        organisms -> IntStream.range(0, random.nextInt(maxPlantsOnCell))
//                                .mapToObj(i -> createGrass(organismPropertyConfig))
//                                .forEach(organisms::add)
//                );
//
//        //fill wolves
//        island.getIslandMap().values()
//                .forEach(
//                        organisms -> IntStream.range(0, random.nextInt(maxPlantsOnCell))
//                                .mapToObj(i -> createWolf(organismPropertyConfig))
//                                .forEach(organisms::add)
//                );

        // move wolf

        for(Map.Entry<Cell, List<Organism>> cellListEntry : island.getIslandMap().entrySet()) {
            Cell cell = cellListEntry.getKey();
            List<Animal> animals = cellListEntry.getValue()
                    .stream()
                    .filter(organism -> organism instanceof Animal)
                    .map(organism -> (Animal) organism)
                    .toList();
//            for(Animal animal : animals) {
//                byte wolfMaxSpeed = getMaxSpeed(organismPropertyConfig, OrganismType.WOLF); // 3 хода
//                Direction direction = chooseDirectionService.chooseDirection(); // LEFT
//                moveService.move(animal, cell, direction, wolfMaxSpeed);
//            }
        }

        System.out.println(islandSizeConfig);
    }

    // TODO: avoid creation by hand - update with reflection
    private static Plants createGrass(OrganismPropertyConfig organismPropertyConfig) {
        return new Plants(organismPropertyConfig.getOrganismMap().get(OrganismType.PLANTS));
    }

    private static Wolf createWolf(OrganismPropertyConfig organismPropertyConfig) {
        return new Wolf(organismPropertyConfig.getOrganismMap().get(OrganismType.WOLF));
    }

    private static int getMaxOrganismsOnCell(OrganismPropertyConfig organismPropertyConfig, OrganismType organismType) {
        return organismPropertyConfig.getOrganismMap().get(organismType).getMaxCountOnCell();
    }

    private static byte getMaxSpeed(OrganismPropertyConfig organismPropertyConfig, OrganismType organismType) {
        return organismPropertyConfig.getOrganismMap().get(organismType).getMaxSpeed();
    }

    private static Island createIslandArea(IslandSizeConfig islandSizeConfig) {
        Map<Cell, List<Organism>> islandMap = new HashMap<>();
        for(int i = 0; i < islandSizeConfig.getWidth(); i++) {
            for(int j = 0; j < islandSizeConfig.getHeight(); j++) {
                Cell cell = new Cell(i, j);
                islandMap.put(cell, new ArrayList<>());
            }
        }
        return new Island(islandMap);
    }
}