package com.github.lorenzopollastrini;

import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public class Table {

    private ArrayList<Cell> cells;
    private Coordinates maxDimensions;

    public List<Set<Cell>> getSets() {
        ArrayList<Set<Cell>> sets = new ArrayList<>();

        for (int i = 0; i <= maxDimensions.getColumn(); i++) {
            Set<Cell> set = getSet(i);
            sets.add(set);
        }

        return sets;
    }

    public Set<Cell> getSet(int columnIndex) {
        Stream<Cell> stream = cells.stream().filter(cell -> cell.getCoordinates().getColumn() == columnIndex);
        return stream.collect(Collectors.toCollection(HashSet::new));
    }
}
