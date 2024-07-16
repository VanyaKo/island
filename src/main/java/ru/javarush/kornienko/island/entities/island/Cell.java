package ru.javarush.kornienko.island.entities.island;

public record Cell(int x, int y) implements Cloneable {

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Cell[" +
               "x=" + x + ", " +
               "y=" + y + ']';
    }


}
