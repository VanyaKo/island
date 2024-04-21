package ru.javarush.kornienko.island.configs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class IslandConfig {
    private String pathToConfigFile;
    private Properties properties;

    public IslandConfig(Properties properties, String pathToConfigFile) {
        this.properties = properties;
        this.pathToConfigFile = pathToConfigFile;
    }

    public int getProperty(String property) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(pathToConfigFile);
        try {
            properties.load(inputStream);
            return Integer.parseInt(properties.getProperty(property));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}
