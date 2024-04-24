package ru.javarush.kornienko.island.configs.action;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.jetbrains.annotations.NotNull;
import ru.javarush.kornienko.island.exceptions.AppException;
import ru.javarush.kornienko.island.models.abstracts.Organism;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EatConfigDeserializer {
    private final ObjectMapper objectMapper;
    private final String pathToJson;

    public EatConfigDeserializer(ObjectMapper objectMapper, String pathToJson) {
        this.objectMapper = objectMapper;
        this.pathToJson = pathToJson;
    }

    public EatConfig[] readEatingProbability() {
        URL resource = Organism.class.getClassLoader().getResource(pathToJson);
        try {
            ObjectMapper mapper = new ObjectMapper();
            SimpleModule module = new SimpleModule();
            module.addDeserializer(EatConfig[].class, new EatProbabilityPairDeserializera());
            mapper.registerModule(module);
            return mapper.readValue(resource, EatConfig[].class);
        } catch(IOException e) {
            throw new AppException(e);
        }
    }

    private EatConfig[] readProbabilityPairs(StringProbabilityPair[] stringProbabilityPairs) throws ClassNotFoundException {
        EatConfig[] eatConfigs = new EatConfig[stringProbabilityPairs.length];
        for(int i = 0; i < stringProbabilityPairs.length; i++) {
            StringProbabilityPair stringProbabilityPair = stringProbabilityPairs[i];
            Class<?> eaterClass = Class.forName(stringProbabilityPair.eater);
            Map<Class<?>, Byte> eatables = getEatableClasses(stringProbabilityPair);
            eatConfigs[i] = new EatConfig(eaterClass, eatables);
        }
        return eatConfigs;
    }

    private @NotNull Map<Class<?>, Byte> getEatableClasses(StringProbabilityPair stringProbabilityPair) throws ClassNotFoundException {
        Map<Class<?>, Byte> eatables = new HashMap<>();
        for(Map.Entry<String, Byte> stringEatable : stringProbabilityPair.eatable.entrySet()) {
            eatables.put(Class.forName(stringEatable.getKey()), stringEatable.getValue());
        }
        return eatables;
    }

    static class EatProbabilityPairDeserializera extends StdDeserializer<EatConfig[]> {

        public EatProbabilityPairDeserializera() {
            this(null);
        }

        public EatProbabilityPairDeserializera(Class<?> vc) {
            super(vc);
        }

        @Override
        public EatConfig[] deserialize(JsonParser jp, DeserializationContext deserializationContext)
                throws IOException {
            List<EatConfig> eatConfigs = new ArrayList<>();
            JsonNode node = jp.getCodec().readTree(jp);
            try {
                for(JsonNode jsonNode : node) {
                    Class<?> eater = Class.forName(jsonNode.get("eater").asText());
                    ObjectMapper mapper = new ObjectMapper();
                    Map<Class<?>, Byte> eatable = mapper.convertValue(jsonNode.get("eatables"), new TypeReference<>(){});
                    eatConfigs.add(new EatConfig(eater, eatable));
                }
                return eatConfigs.toArray(new EatConfig[0]);
            } catch(ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
