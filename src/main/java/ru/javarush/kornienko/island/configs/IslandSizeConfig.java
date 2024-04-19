package ru.javarush.kornienko.island.configs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class IslandSizeConfig {
    private int width;
    private int height;
    private String pathToConfigFile;
    private Properties properties;

    public IslandSizeConfig(Properties properties, String pathToConfigFile) {
        this.properties = properties;
        this.pathToConfigFile = pathToConfigFile;
        init();
    }

    public String getPathToConfigFile() {
        return pathToConfigFile;
    }

    public void setPathToConfigFile(String pathToConfigFile) {
        this.pathToConfigFile = pathToConfigFile;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void init() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(pathToConfigFile);
        try {
            properties.load(inputStream);
            this.width = Integer.parseInt(properties.getProperty("width", "100"));
            this.height = Integer.parseInt(properties.getProperty("height", "20"));
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
