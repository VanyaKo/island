package ru.javarush.kornienko.island.configs;

public class IslandSizeConfig {
    private int width;
    private int height;

    public IslandSizeConfig(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
