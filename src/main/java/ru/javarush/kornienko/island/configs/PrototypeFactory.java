package ru.javarush.kornienko.island.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import ru.javarush.kornienko.island.entities.abstracts.Organism;
import ru.javarush.kornienko.island.entities.herbivores.Buffalo;
import ru.javarush.kornienko.island.entities.herbivores.Caterpillar;
import ru.javarush.kornienko.island.entities.herbivores.Deer;
import ru.javarush.kornienko.island.entities.herbivores.Duck;
import ru.javarush.kornienko.island.entities.herbivores.Goat;
import ru.javarush.kornienko.island.entities.herbivores.Hog;
import ru.javarush.kornienko.island.entities.herbivores.Horse;
import ru.javarush.kornienko.island.entities.herbivores.Mouse;
import ru.javarush.kornienko.island.entities.herbivores.Rabbit;
import ru.javarush.kornienko.island.entities.herbivores.Sheep;
import ru.javarush.kornienko.island.entities.plants.Grass;
import ru.javarush.kornienko.island.entities.predators.Bear;
import ru.javarush.kornienko.island.entities.predators.Eagle;
import ru.javarush.kornienko.island.entities.predators.Fox;
import ru.javarush.kornienko.island.entities.predators.Python;
import ru.javarush.kornienko.island.entities.predators.Wolf;
import ru.javarush.kornienko.island.exceptions.AppException;

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

    public PrototypeFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        types = new HashSet<>();
        prototypes = new HashMap<>();
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
        types.add(Sheep.class);
        types.add(Hog.class);
        types.add(Buffalo.class);
        types.add(Duck.class);
        types.add(Caterpillar.class);
        types.add(Grass.class);
        init();
    }

    private void init() {
        for(Class<? extends Organism> type : types) {
            Organism organism = createPrototype(type);
            prototypes.put(type, organism);
        }
    }

    private Organism createPrototype(Class<? extends Organism> type) {
        if(!type.isAnnotationPresent(OrganismConfig.class)) {
            throw new IllegalArgumentException(String.format("Prototype class %s must have @%s annotation", OrganismConfig.class.getSimpleName(), type.getSimpleName()));
        }
        URL resource = getConfigFilePath(type);
        return loadObject(resource, type);
    }

    private URL getConfigFilePath(@NotNull Class<? extends Organism> type) {
        OrganismConfig organismConfig = type.getAnnotation(OrganismConfig.class);
        return type.getClassLoader().getResource(organismConfig.fileName());
    }

    private Organism loadObject(URL resource, @NotNull Class<? extends Organism> type) {
        try {
            return objectMapper.readValue(resource, type);
        } catch(IOException e) {
            throw new AppException(e);
        }
    }

    public Collection<Organism> getPrototypes() {
        return prototypes.values();
    }

    public String getUnicodeByClass(Class<? extends Organism> clazz) {
        return prototypes.get(clazz).getUnicode();
    }
}
