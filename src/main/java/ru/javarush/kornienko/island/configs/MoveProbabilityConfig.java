package ru.javarush.kornienko.island.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javarush.kornienko.island.exceptions.AppException;
import ru.javarush.kornienko.island.models.abstracts.Organism;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MoveProbabilityConfig {
    private final ObjectMapper objectMapper;
    private final String pathToJson;

    public MoveProbabilityConfig(ObjectMapper objectMapper, String pathToJson) {
        this.objectMapper = objectMapper;
        this.pathToJson = pathToJson;
    }

    @SuppressWarnings("unchecked")
    public Map<Class<?>, Integer> readMoveProbability() {
        URL resource = Organism.class.getClassLoader().getResource(pathToJson);
        try {
            Map<String, Integer> stringMap = objectMapper.readValue(resource, Map.class);
            return convertToClassMap(stringMap);
        } catch(IOException e) {
            throw new AppException("Error with reading of move probability json", e);
        }
    }

    private Map<Class<?>, Integer> convertToClassMap(Map<String, Integer> stringMap) {
        Map<Class<?>, Integer> classMap = new HashMap<>();
        for(Map.Entry<String, Integer> stringEntry : stringMap.entrySet()) {
            try {
                classMap.put(Class.forName(stringEntry.getKey()), stringEntry.getValue());
            } catch(ClassNotFoundException e) {
                throw new AppException("Cannot create class for name " + stringEntry.getKey(), e);
            }
        }
        return classMap;
    }
}
