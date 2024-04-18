package ru.javarush.kornienko.island.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.enums.OrganismType;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class OrganismPropertyConfig {
    private Map<OrganismType, Organism> organismMap;
    private ObjectMapper objectMapper;
    private String pathToJson;

    public OrganismPropertyConfig(ObjectMapper objectMapper, String pathToJson) {
        // TODO: parse json to organismMapConfig
        this.objectMapper = objectMapper;
        this.pathToJson = pathToJson;
        organismMap = parseMap();

        // Заглушка
//        organismMap.put(OrganismType.GRASS, new Organism(1, 12, (byte) 1, 1));
//        organismMap.put(OrganismType.WOLF, new Organism(10, 120, (byte) 10, 10));
    }

    private Map<OrganismType, Organism> parseMap() {
        Map<OrganismType, Organism> organismMap = new HashMap<>();
        try {
            Map<String, Organism> inputs =
                    objectMapper.readValue(new URL("file:" + pathToJson), Map.class);
            System.out.println();
//            for(Map.Entry<String, Organism> entry : inputs.entrySet()) {
//                OrganismType organismType = OrganismType.getOrganismTypeByName(entry.getKey());
//                Class<? extends Organism> organismClass = organismType.getClazz();
//                LinkedHashMap<String, > value = entry.getValue();
//                organismClass.getConstructor(double.class, int.class, byte.class, double.class)
//                        .newInstance(value.ge(), value.getMaxCountOnCell(), value.getMaxSpeed(), value.getKilogramsForFullSaturation());
//                organismMap.put(organismType, value);
//            }
        } catch(IOException e) {
            e.printStackTrace();
        } /*catch(InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }*/
        return organismMap;
    }


    public Map<OrganismType, Organism> getOrganismMap() {
        return new EnumMap<>(organismMap);
    }
}
