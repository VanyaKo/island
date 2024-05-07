package ru.javarush.kornienko.island.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import ru.javarush.kornienko.island.consts.Consts;
import ru.javarush.kornienko.island.entities.abstracts.Organism;
import ru.javarush.kornienko.island.entities.annotations.ConcreteOrganism;
import ru.javarush.kornienko.island.exceptions.AppException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
        initTypes();
        initPrototypes();
    }

    public Collection<Organism> getPrototypes() {
        return prototypes.values();
    }

    public String getUnicodeByClass(Class<? extends Organism> clazz) {
        return prototypes.get(clazz).getUnicode();
    }

    private void initTypes() {
        try {
            types.addAll(getClasses(Consts.ENTITIES_DIRECTORY));
        } catch(ClassNotFoundException | IOException e) {
            throw new AppException(e);
        }
    }

    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     * @param packageName A base package.
     */
    private Set<Class<? extends Organism>> getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while(resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        Set<Class<? extends Organism>> classes = new HashSet<>();
        for(File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirectories.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     */
    private Set<Class<? extends Organism>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        Set<Class<? extends Organism>> classes = new HashSet<>();
        if(!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        assert files != null;
        for(File file : files) {
            if(file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if(file.getName().endsWith(".class")) {
                Class<?> clazz = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
                if(clazz.isAnnotationPresent(ConcreteOrganism.class)) {
                    classes.add((Class<? extends Organism>) clazz);
                }
            }
        }
        return classes;
    }

    private void initPrototypes() {
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
}
