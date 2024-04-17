package ru.javarush.kornienko.island.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.enums.OrganismType;

import java.util.EnumMap;
import java.util.Map;

public class OrganismPropertyConfig {
    private Map<OrganismType, Organism> organismMap;

    public OrganismPropertyConfig(ObjectMapper objectMapper, String pathToJson) {
        // TODO: parse json to organismMapConfig
        organismMap = new EnumMap<>(OrganismType.class);

        // Заглушка
        organismMap.put(OrganismType.GRASS, new Organism(1, 12, (byte) 1, 1));
        organismMap.put(OrganismType.WOLF, new Organism(10, 120, (byte) 10, 10));
    }


    public Map<OrganismType, Organism> getOrganismMap() {
        return new EnumMap<>(organismMap);
    }
}
