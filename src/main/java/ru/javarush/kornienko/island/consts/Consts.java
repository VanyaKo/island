package ru.javarush.kornienko.island.consts;

public class Consts {
    public static final String EAT_PROBABILITY_CONFIG_JSON = "configs/eat_probability_config.json";
    public static final String MOVE_PROBABILITY_CONFIG_JSON = "configs/move_probability_config.json";
    public static final String REPRODUCE_PROBABILITY_CONFIG_JSON = "configs/reproduce_probability_config.json";
    public static final String ISLAND_CONFIG = "configs/map/island.properties";
    public static final int MIN_NUMBER_OF_ANIMALS_TO_REPRODUCE = 2;
    public static final int HUNDRED_PERCENT = 100;
    public static final double PERCENT_TO_DECREASE_FROM_ANIMAL_STARVATION = (byte) 20;
    public static final String LINE_DELIMITER = "=====================================================".repeat(3);

    private Consts() {

    }
}
