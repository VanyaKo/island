package ru.javarush.kornienko.island.configs.animals;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import ru.javarush.kornienko.island.exceptions.AppException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReproduceConfigDeserializer extends StdDeserializer<ReproduceConfig[]> {
    private ObjectMapper objectMapper;
    private String pathToJson;

    public ReproduceConfigDeserializer(ObjectMapper objectMapper, String pathToJson) {
        this(null);
        this.objectMapper = objectMapper;
        this.pathToJson = pathToJson;
    }

    private ReproduceConfigDeserializer(Class<?> vc) {
        super(vc);
    }

    public ReproduceConfig[] readReproduceConfig() {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(pathToJson);
        try {
            SimpleModule module = new SimpleModule();
            module.addDeserializer(ReproduceConfig[].class, this);
            objectMapper.registerModule(module);
            return objectMapper.readValue(resource, ReproduceConfig[].class);
        } catch(IOException e) {
            throw new AppException("Error with reading of reproduce probability json", e);
        }
    }

    @Override
    public ReproduceConfig[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        List<ReproduceConfig> reproduceConfigs = new ArrayList<>();
        JsonNode node = p.getCodec().readTree(p);
        try {
            for(JsonNode jsonNode : node) {
                Class<?> reproducer = Class.forName(jsonNode.get("reproducer").asText());
                byte couplingProbability = jsonNode.get("couplingProbability").numberValue().byteValue();
                byte birthProbability = jsonNode.get("birthProbability").numberValue().byteValue();
                int maxCubs = jsonNode.get("maxCubs").intValue();
                reproduceConfigs.add(new ReproduceConfig(reproducer, couplingProbability, birthProbability, maxCubs));
            }
            return reproduceConfigs.toArray(new ReproduceConfig[0]);
        } catch(ClassNotFoundException e) {
            throw new AppException(e);
        }
    }
}
