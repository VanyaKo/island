package ru.javarush.kornienko.island.configs.action;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import ru.javarush.kornienko.island.models.abstracts.Organism;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class EatProbabilityConfig {
    private final ObjectMapper objectMapper;
    private final String pathToJson;

    public EatProbabilityConfig(ObjectMapper objectMapper, String pathToJson) {
        this.objectMapper = objectMapper;
        this.pathToJson = pathToJson;
    }

    public EatProbabilityPair[] readEatingProbability() {
        URL resource = Organism.class.getClassLoader().getResource(pathToJson);
        try {
            StringProbabilityPair[] stringProbabilityPairs =
                    objectMapper.readValue(resource, StringProbabilityPair[].class);
            return readProbabilityPairs(stringProbabilityPairs);
        } catch(IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private EatProbabilityPair[] readProbabilityPairs(StringProbabilityPair[] stringProbabilityPairs) throws ClassNotFoundException {
        EatProbabilityPair[] eatProbabilityPairs = new EatProbabilityPair[stringProbabilityPairs.length];
        for(int i = 0; i < stringProbabilityPairs.length; i++) {
            StringProbabilityPair stringProbabilityPair = stringProbabilityPairs[i];
            Class<?> eaterClass = Class.forName(stringProbabilityPair.eater);
            Map<Class<?>, Byte> eatables = getEatableClasses(stringProbabilityPair);
            eatProbabilityPairs[i] = new EatProbabilityPair(eaterClass, eatables);
        }
        return eatProbabilityPairs;
    }

    private @NotNull Map<Class<?>, Byte> getEatableClasses(StringProbabilityPair stringProbabilityPair) throws ClassNotFoundException {
        Map<Class<?>, Byte> eatables = new HashMap<>();
        for(Map.Entry<String, Byte> stringEatable : stringProbabilityPair.eatable.entrySet()) {
            eatables.put(Class.forName(stringEatable.getKey()), stringEatable.getValue());
        }
        return eatables;
    }

    private static class StringProbabilityPair implements Serializable {
        private String eater;
        private Map<String, Byte> eatable;

        public StringProbabilityPair() {
        }

        public void setEater(String eater) {
            this.eater = eater;
        }

        public void setEatable(Map<String, Byte> eatable) {
            this.eatable = eatable;
        }
    }
}
