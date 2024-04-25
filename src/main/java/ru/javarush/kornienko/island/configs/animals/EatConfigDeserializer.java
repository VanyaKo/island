package ru.javarush.kornienko.island.configs.animals;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.javarush.kornienko.island.exceptions.AppException;
import ru.javarush.kornienko.island.models.abstracts.Organism;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EatConfigDeserializer extends StdDeserializer<EatConfig[]> {
    private ObjectMapper objectMapper;
    private String pathToJson;

    public EatConfigDeserializer(ObjectMapper objectMapper, String pathToJson) {
        this(null);
        this.objectMapper = objectMapper;
        this.pathToJson = pathToJson;
    }

    private EatConfigDeserializer(Class<?> vc) {
        super(vc);
    }

    public EatConfig[] readEatConfig() {
        URL resource = Organism.class.getClassLoader().getResource(pathToJson);
        try {
            SimpleModule module = new SimpleModule();
            module.addDeserializer(EatConfig[].class, this);
            objectMapper.registerModule(module);
            return objectMapper.readValue(resource, EatConfig[].class);
        } catch(IOException e) {
            throw new AppException(e);
        }
    }

    @Override
    public EatConfig[] deserialize(JsonParser p, DeserializationContext deserializationContext)
            throws IOException {
        List<EatConfig> eatConfigs = new ArrayList<>();
        JsonNode node = p.getCodec().readTree(p);
        try {
            for(JsonNode jsonNode : node) {
                Class<?> eater = Class.forName(jsonNode.get("eater").asText());
                Map<Class<?>, Byte> eatables = objectMapper.convertValue(jsonNode.get("eatables"), new TypeReference<>() {});
                eatConfigs.add(new EatConfig(eater, eatables));
            }
            return eatConfigs.toArray(new EatConfig[0]);
        } catch(ClassNotFoundException e) {
            throw new AppException(e);
        }
    }
}
