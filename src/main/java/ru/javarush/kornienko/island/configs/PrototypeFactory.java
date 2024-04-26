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
import java.util.Map;
import java.util.Set;

public class PrototypeFactory {
    private final Set<Class<? extends Organism>> types;
    private final Map<Class<? extends Organism>, String> unicodes;
    private final Map<Class<? extends Organism>, Organism> prototypes;
    private final ObjectMapper objectMapper;

    public PrototypeFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        prototypes = new HashMap<>();
        unicodes = new HashMap<>();
        unicodes.put(Wolf.class, "\uD83D\uDC3A");
        unicodes.put(Python.class, "\uD83D\uDC0D");
        unicodes.put(Fox.class, "\uD83E\uDD8A");
        unicodes.put(Bear.class, "\uD83D\uDC3B");
        unicodes.put(Eagle.class, "\uD83E\uDD85");
        unicodes.put(Horse.class, "\uD83D\uDC0E");
        unicodes.put(Deer.class, "\uD83E\uDD8C");
        unicodes.put(Rabbit.class, "\uD83D\uDC07");
        unicodes.put(Mouse.class, "\uD83D\uDC01");
        unicodes.put(Goat.class, "\uD83D\uDC10");
        unicodes.put(Sheep.class, "\uD83D\uDC11");
        unicodes.put(Hog.class, "\uD83D\uDC17");
        unicodes.put(Buffalo.class, "\uD83D\uDC03");
        unicodes.put(Duck.class, "\uD83E\uDD86");
        unicodes.put(Caterpillar.class, "\uD83D\uDC1B");
        unicodes.put(Grass.class, "\uD83C\uDF31");
        types = unicodes.keySet();
        init();
    }

    private void init() {
        for(Class<? extends Organism> type : types) {
            Organism organism = createPrototype(type);
            prototypes.put(type, organism);
        }
    }

    public String getUnicodeByClass(Class<? extends Organism> clazz) {
        return unicodes.get(clazz);
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
}
