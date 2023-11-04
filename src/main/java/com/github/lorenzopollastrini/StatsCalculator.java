package com.github.lorenzopollastrini;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StatsCalculator {

    public static void main(String[] args) throws Exception {
        String tablesPathString = "tables.json";

        int tables = 0;
        int rows = 0;
        int columns = 0;
        int nullCells = 0;
        HashMap<Integer, Integer> rowCount2TableCount = new HashMap<>();
        HashMap<Integer, Integer> columnCount2TableCount = new HashMap<>();
        HashMap<Integer, Integer> distinctValues2ColumnCount = new HashMap<>();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(tablesPathString));

        Gson gson = new Gson();

        String currentLine;
        Table currentTable;
        while ((currentLine = bufferedReader.readLine()) != null) {
            // Update table count
            tables++;

            System.out.println("Analyzing table #" + tables);

            // Calculate current row and column count
            currentTable = gson.fromJson(currentLine, Table.class);
            Coordinates currentDimensions = currentTable.getMaxDimensions();
            int currentRows = currentDimensions.getRow(); // Ignore header rows (assuming one header row per table)
            int currentColumns = currentDimensions.getColumn() + 1;

            // Update global row and column count
            rows += currentRows;
            columns += currentColumns;

            // Update distribution of rows across tables, ignoring header rows (assuming one header row per table)
            if (!rowCount2TableCount.containsKey(currentRows))
                rowCount2TableCount.put(currentRows, 1);
            else
                rowCount2TableCount.put(currentRows, rowCount2TableCount.get(currentRows) + 1);

            // Update distribution of columns across tables
            if (!columnCount2TableCount.containsKey(currentColumns))
                columnCount2TableCount.put(currentColumns, 1);
            else
                columnCount2TableCount.put(currentColumns, columnCount2TableCount.get(currentColumns) + 1);

            List<List<Cell>> currentSets = currentTable.getSets();
            for (List<Cell> currentSet : currentSets) {
                Stream<String> currentCleanedTextsStream = currentSet.stream()
                        .filter(cell -> !cell.isHeader() && !cell.getCleanedText().isBlank())
                        .map(Cell::getCleanedText);

                ArrayList<String> currentCleanedTextsArrayList = currentCleanedTextsStream
                        .collect(Collectors.toCollection(ArrayList::new));

                // Update global null cell count (assuming one header row per table)
                nullCells += currentSet.size() - 1 - currentCleanedTextsArrayList.size();

                HashSet<String> currentCleanedTextsHashSet = new HashSet<>(currentCleanedTextsArrayList);

                int currentDistinctValues = currentCleanedTextsHashSet.size();

                // Update distribution of distinct values across columns
                if (!distinctValues2ColumnCount.containsKey(currentDistinctValues))
                    distinctValues2ColumnCount.put(currentDistinctValues, 1);
                else
                    distinctValues2ColumnCount.put(currentDistinctValues,
                            distinctValues2ColumnCount.get(currentDistinctValues) + 1);
            }
        }

        System.out.println("Numero di tabelle: " + tables + "\n" +
                "Numero medio di righe: " + (float) rows/tables + "\n" +
                "Numero medio di colonne: " + (float) columns/tables + "\n" +
                "Numero medio di valori nulli: " + (float) nullCells/tables + "\n" +
                "Distribuzione del numero di righe tra le tabelle: " + rowCount2TableCount + "\n" +
                "Distribuzione del numero di colonne tra le tabelle: " + columnCount2TableCount + "\n" +
                "Distribuzione del numero di valori distinti tra le colonne: " + distinctValues2ColumnCount + "\n");
    }

}
