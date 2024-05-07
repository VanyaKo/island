package ru.javarush.kornienko.island.configs;

import ru.javarush.kornienko.island.exceptions.AppException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class IslandConfig {
    private final String pathToConfigFile;
    private final Properties properties;

    public IslandConfig(Properties properties, String pathToConfigFile) {
        this.properties = properties;
        this.pathToConfigFile = pathToConfigFile;
    }

    public int getProperty(String property) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(pathToConfigFile);
        try {
            properties.load(inputStream);
        } catch(IOException e) {
            throw new AppException("Cannot load island properties", e);
        }
        return Integer.parseInt(properties.getProperty(property));
    }
}
