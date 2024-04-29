package ru.javarush.kornienko.island.consts;

public class Consts {
    public static final String ENTITIES_DIRECTORY = "ru.javarush.kornienko.island.entities";

    public static final String EAT_CONFIG_JSON = "configs/eat_config.json";
    public static final String MOVE_CONFIG_JSON = "configs/move_config.json";
    public static final String REPRODUCE_CONFIG_JSON = "configs/reproduce_config.json";
    public static final String START_ANIMAL_NUM_CONFIG_JSON = "configs/start_animal_num_config.json";
    public static final String ISLAND_CONFIG = "configs/island/island.properties";
    public static final int MIN_NUMBER_OF_ANIMALS_TO_REPRODUCE = 2;

    public static final String PATH_TO_LOG_DIR = "logs";
    public static final String PATH_TO_LOG_FILE = PATH_TO_LOG_DIR + "/%s.log";

    public static final int HUNDRED_PERCENT = 100;
    public static final double PERCENT_TO_DECREASE_FROM_ANIMAL_STARVATION = (byte) 20;

    public static final String LINE_DELIMITER = "=====================================================".repeat(3);
    public static final String LIST_PRINT_ITEM = "- ";
    public static final int DEFAULT_NUMBER_PROPERTY_VALUE = -1;

    private Consts() {

    }
}
