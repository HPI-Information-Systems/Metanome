package de.metanome.backend.result_postprocessing.result_analyzer.helper;

import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alexander Spivak on 07.01.2015.
 */
public class PLIBuilder {

  public static PLIHolder buildPLIs(RelationalInput relationalInput) {
    ColumnCombination allColumns = new ColumnCombination(relationalInput.numberOfColumns(), true);
    return buildPLIs(relationalInput, allColumns);
  }

  public static PLIHolder buildPLIs(RelationalInput relationalInput, ColumnCombination neededColumns) {

    List<Map<String, List<Integer>>> distinctValuesIndices = new ArrayList<>(neededColumns.getColumnCount());
    for(int i=0; i<neededColumns.getColumnCount(); i++){
      distinctValuesIndices.add(new HashMap<String, List<Integer>>());
    }

    List<Integer> columnIndices = neededColumns.getColumnIndices();

    // Build PLI list for each column and compute the row count
    int rowCount = 0;
    // Iterate over lines
    try {
      while (relationalInput.hasNext()) {
        List<String> line = relationalInput.next();
        // Iterate over the needed columns in rov
        for(int i=0; i<columnIndices.size(); i++){
          String cellValue = line.get(columnIndices.get(i));
          // Store the index in the list according to the cell value
          Map<String, List<Integer>> columnValuesMapping = distinctValuesIndices.get(i);
          if (!columnValuesMapping.containsKey(cellValue)) {
            columnValuesMapping.put(cellValue, new ArrayList<Integer>());
          }
          columnValuesMapping.get(cellValue).add(rowCount);
        }
        rowCount++;
      }
    }
    catch (InputIterationException e){
      e.printStackTrace();
    }

    // Build the real PLIs from precomputed structure
    PLIHolder pliHolder = new PLIHolder();
    for(int i=0; i<neededColumns.getColumnCount(); i++) {
      PLIList pliList = new PLIList(distinctValuesIndices.get(i));
      pliHolder.getPliMap().put(new ColumnCombination(columnIndices.get(i), neededColumns.getColumnCount()), pliList);
    }

    return pliHolder;
  }

}
