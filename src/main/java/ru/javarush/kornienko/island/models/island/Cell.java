package ru.javarush.kornienko.island.models.island;

import java.util.Objects;

public final class Cell {
    private final int x;
    private final int y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Cell) obj;
        return this.x == that.x &&
               this.y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Cell[" +
               "x=" + x + ", " +
               "y=" + y + ']';
    }


}
