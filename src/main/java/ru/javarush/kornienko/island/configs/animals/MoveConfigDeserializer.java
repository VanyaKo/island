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
import java.util.Map;

public class MoveConfigDeserializer extends StdDeserializer<Map<Class<?>, Byte>> {
    private ObjectMapper objectMapper;
    private String pathToJson;

    public MoveConfigDeserializer(ObjectMapper objectMapper, String pathToJson) {
        this(null);
        this.objectMapper = objectMapper;
        this.pathToJson = pathToJson;
    }

    private MoveConfigDeserializer(Class<?> vc) {
        super(vc);
    }

    @SuppressWarnings("unchecked")
    public Map<Class<?>, Byte> readMoveConfig() {
        URL resource = Organism.class.getClassLoader().getResource(pathToJson);
        try {
            SimpleModule module = new SimpleModule();
            module.addDeserializer(Map.class, this);
            objectMapper.registerModule(module);
            return objectMapper.readValue(resource, Map.class);
        } catch(IOException e) {
            throw new AppException(e);
        }
    }

    @Override
    public Map<Class<?>, Byte> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        return objectMapper.convertValue(node, new TypeReference<>() {
        });
    }
}
