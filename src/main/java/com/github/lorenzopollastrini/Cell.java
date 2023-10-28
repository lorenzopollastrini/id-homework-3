package com.github.lorenzopollastrini;

import lombok.Getter;

@Getter
public class Cell {

    private Coordinates Coordinates;
    private String cleanedText;

    @Override
    public String toString() {
        return cleanedText + " [" + Coordinates.getRow() + ", " + Coordinates.getColumn() + "]";
    }
}
