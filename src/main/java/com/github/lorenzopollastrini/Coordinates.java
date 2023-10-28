package com.github.lorenzopollastrini;

import lombok.Getter;

@Getter
public class Coordinates {

    private int row;
    private int column;

    @Override
    public String toString() {
        return "[" + row + ", " + column + "]";
    }
}
