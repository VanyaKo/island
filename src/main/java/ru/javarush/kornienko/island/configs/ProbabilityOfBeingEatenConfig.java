package ru.javarush.kornienko.island.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javarush.kornienko.island.models.abstracts.Organism;

import java.util.HashMap;
import java.util.Map;

public class ProbabilityOfBeingEatenConfig {
    private Map<Map<Organism, Organism>, Byte> possibiityMap;

    public ProbabilityOfBeingEatenConfig(ObjectMapper objectMapper, String pathToJson) {
        // TODO: parse json to organismMapConfig
        possibiityMap = new HashMap<Map<Organism, Organism>, Byte>();
    }
}
