package ru.javarush.kornienko.island.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javarush.kornienko.island.models.abstracts.Organism;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ProbabilityOfBeingEatenConfig {
    private final ObjectMapper objectMapper;
    private final String pathToJson;
    private final Set<ProbabilityPair> probabilityPairs;

    public ProbabilityOfBeingEatenConfig(ObjectMapper objectMapper, String pathToJson) {
        this.objectMapper = objectMapper;
        this.pathToJson = pathToJson;
        this.probabilityPairs = parseFileToSet();
    }

    public Set<ProbabilityPair> parseFileToSet() {
        URL resource = Organism.class.getClassLoader().getResource(pathToJson);
        try {
            ProbabilityPair[] inputProbabilityPairs =
                    objectMapper.readValue(resource, ProbabilityPair[].class);
            return new HashSet<>(Arrays.asList(inputProbabilityPairs));
        } catch(IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
