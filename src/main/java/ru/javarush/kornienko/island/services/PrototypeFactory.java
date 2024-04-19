package ru.javarush.kornienko.island.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import ru.javarush.kornienko.island.configs.Config;
import ru.javarush.kornienko.island.models.abstracts.Organism;
import ru.javarush.kornienko.island.models.herbivores.Buffalo;
import ru.javarush.kornienko.island.models.herbivores.Caterpillar;
import ru.javarush.kornienko.island.models.herbivores.Deer;
import ru.javarush.kornienko.island.models.herbivores.Duck;
import ru.javarush.kornienko.island.models.herbivores.Goat;
import ru.javarush.kornienko.island.models.herbivores.Hog;
import ru.javarush.kornienko.island.models.herbivores.Horse;
import ru.javarush.kornienko.island.models.herbivores.Mouse;
import ru.javarush.kornienko.island.models.herbivores.Rabbit;
import ru.javarush.kornienko.island.models.plants.Plants;
import ru.javarush.kornienko.island.models.predators.Bear;
import ru.javarush.kornienko.island.models.predators.Eagle;
import ru.javarush.kornienko.island.models.predators.Fox;
import ru.javarush.kornienko.island.models.predators.Python;
import ru.javarush.kornienko.island.models.predators.Wolf;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PrototypeFactory {
    private final Set<Class<? extends Organism>> types;
    private final Map<Class<? extends Organism>, Organism> prototypes;
    private final ObjectMapper objectMapper;

    public PrototypeFactory(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
        prototypes = new HashMap<>();
        types = new HashSet<>();
        types.add(Wolf.class);
        types.add(Python.class);
        types.add(Fox.class);
        types.add(Bear.class);
        types.add(Eagle.class);
        types.add(Horse.class);
        types.add(Deer.class);
        types.add(Rabbit.class);
        types.add(Mouse.class);
        types.add(Goat.class);
        types.add(Hog.class);
        types.add(Buffalo.class);
        types.add(Duck.class);
        types.add(Caterpillar.class);
        types.add(Plants.class);
        init();
    }

    public Organism cloneOrganism(Class<? extends Organism> type) {
        throw new RuntimeException();
    }

    private void init() {
        for(Class<? extends Organism> type : types) {
            Organism organism = createPrototype(type);
            prototypes.put(type, organism);
        }
    }

    private Organism createPrototype(Class<? extends Organism> type) {
        if(!type.isAnnotationPresent(Config.class)) {
            throw new IllegalArgumentException(String.format("Prototype class %s must have @Config annotation", type.getSimpleName()));
        }
        URL resource = getConfigFilePath(type);
        return loadObject(resource, type);
    }

    private URL getConfigFilePath(@NotNull Class<? extends Organism> type) {
        Config config = type.getAnnotation(Config.class);
        return type.getClassLoader().getResource(config.fileName());
    }

    private Organism loadObject(URL resource, @NotNull Class<? extends Organism> type) {
        Organism organism;
        try {
            organism = objectMapper.readValue(resource, type);
        } catch(IOException e) {
            throw new RuntimeException(e);
//            throw new RuntimeException(String.format("Cannot find config file %s for class %s", resource.getFile(), type));
        }
        return organism;
    }

    public Collection<Organism> getPrototypes() {
        return prototypes.values();
    }

    public Map<Class<? extends Organism>, Organism> getPrototypesMap() {
        return prototypes;
    }

    public Organism getPrototype(@NotNull Class<? extends Organism> type) {
        if(prototypes.containsKey(type)) {
            return prototypes.get(type);
        }
        Organism organism = createPrototype(type);
        prototypes.put(type, organism);
        return organism;
    }
}
