/**
 * Copyright 2015-2016 by Metanome Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.metanome.backend.result_postprocessing.visualization.FunctionalDependency;

import de.metanome.algorithm_helper.data_structures.PositionListIndex;
import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.backend.constants.Constants;
import de.metanome.backend.result_postprocessing.helper.ColumnInformation;
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.results.FunctionalDependencyResult;
import de.metanome.backend.result_postprocessing.visualization.JSONPrinter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;


public class FunctionalDependencyVisualization {

  protected List<FunctionalDependencyResult> results;
  protected TableInformation tableInformation;
  protected String prefixTreeJsonFile;

  public FunctionalDependencyVisualization(List<FunctionalDependencyResult> results,
                                           TableInformation tableInformation) {
    this.results = results;
    this.tableInformation = tableInformation;

  }

  /**
   * Creates all visualization data for functional dependencies and writes them to a file.
   * @throws java.io.FileNotFoundException if file could not be found
   */
  public void createVisualizationData() throws FileNotFoundException {
    // set the path to store the data
    String currentPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    String relativeVisualizationPath = "../../src/visualization/FDResultAnalyzer/";
    this.prefixTreeJsonFile =
      combinePaths(currentPath + relativeVisualizationPath, "PrefixTree.json");

    // Clear the content of the json file
    JSONPrinter.clearFile(this.prefixTreeJsonFile);

    // Create a map from a dependant to all its determinants
    Map<ColumnIdentifier, Set<ColumnCombination>>
      dependantMap = createDependantMap();

    // Print to JSON file
    printFunctionalDependencyVisualizationData(dependantMap);
  }

  /**
   * Creates a reverted index on the functional dependency dependants.
   *
   * @return a mapping of each dependant to a sorted list of all its determinants
   */
  protected Map<ColumnIdentifier, Set<ColumnCombination>> createDependantMap() {
    Map<ColumnIdentifier, Set<ColumnCombination>> dependantMap = new HashMap<>();

    // Add for each dependant column all its determinant columns to the map
    for (FunctionalDependencyResult result : this.results) {
      ColumnIdentifier dependantColumn = result.getDependant();
      if (!dependantMap.containsKey(dependantColumn)) {
        dependantMap.put(dependantColumn, new TreeSet<ColumnCombination>());
      }
      ;
      dependantMap.get(dependantColumn).add(result.getDeterminant());
    }

    return dependantMap;
  }

  /**
   * Prints the data for the visualization for functional dependencies.
   *
   * @param dependantMap the dependant map
   */
  @SuppressWarnings(Constants.SUPPRESS_WARNINGS_UNCHECKED)
  public void printFunctionalDependencyVisualizationData(
    Map<ColumnIdentifier, Set<ColumnCombination>> dependantMap) {

    // table name should be the root
    JSONObject root = new JSONObject();
    root.put("name", tableInformation.getTableName());
    // store table size in root to show it in keyError
    root.put("tableSize", tableInformation.getRowCount());

    // build tree for each dependant column
    JSONArray rootChildren = new JSONArray();

    // sort the dependant columns
    List<ColumnIdentifier> dependantColumns = new ArrayList<>(dependantMap.keySet());
    Collections.sort(dependantColumns);

    for (ColumnIdentifier dependantColumn : dependantColumns) {
      Set<ColumnCombination> determinants = dependantMap.get(dependantColumn);
      BitSet
        dependantAsBitSet =
        tableInformation.getColumnInformationMap().get(dependantColumn.getColumnIdentifier())
          .getBitSet();

      // create a JSON for the dependant column
      BitSet path = new BitSet();
      JSONObject
        dependantJSON =
        printRecursive(dependantAsBitSet, determinants, path, -1, 0, determinants.size(), "");

      // Change the root name to the dependant column name
      dependantJSON.put("name", dependantColumn.getColumnIdentifier());
      // Add it to the table's children array
      rootChildren.add(dependantJSON);
    }

    root.put("children", rootChildren);

    JSONPrinter.writeToFile(this.prefixTreeJsonFile, root);
  }

  /**
   * Recursive helper function for printing prefix trees as JSON. The resulting JSON should look
   * like this: dependant - first column of first determinant - second column of first determinant -
   * third column of first determinant - first column of second determinant - first column of third
   * determinant - second column of third determinant
   *
   * @param dependant             the dependant
   * @param determinants          the determinants
   * @param path                  the current path
   * @param columnIndex           the current column index
   * @param determinantStartIndex the determinant start index
   * @param determinantEndIndex   the determinant end index
   * @param columnName            the column name
   *
   * @return the prefix tree branch as JSON defined by the parameters
   */
  @SuppressWarnings(Constants.SUPPRESS_WARNINGS_UNCHECKED)
  protected JSONObject printRecursive(BitSet dependant,
                                      Set<ColumnCombination> determinants,
                                      BitSet path,
                                      int columnIndex,
                                      int determinantStartIndex,
                                      int determinantEndIndex,
                                      String columnName) {
    // Prepare the result structure
    JSONObject result = new JSONObject();
    // Prepare the children array of the result
    JSONArray children = new JSONArray();

    if (columnIndex >= 0) {
      ColumnInformation columnInformation = tableInformation.getColumnInformationMap().get(
        columnName);
      path.set(columnInformation.getColumnIndex());
      result.put("name", columnName);
      result.put("size", columnInformation.getUniquenessRate());
      result
        .put("keyError", Math.abs(calculateKeyError(path) - calculateKeyError(path, dependant)));
    }

    // Add all columns of determinants at index 'columnIndex + 1'
    ColumnIdentifier nextColumn = getColumnIdentifier(determinants, determinantStartIndex,
      columnIndex + 1);
    int lastStart = determinantStartIndex;
    for (int i = determinantStartIndex + 1; i < determinantEndIndex; i++) {
      ColumnIdentifier otherColumn = getColumnIdentifier(determinants, i, columnIndex + 1);
      if (otherColumn != null && !otherColumn.equals(nextColumn)) {
        if (nextColumn != null) {
          children.add(
            printRecursive(dependant, determinants, path, columnIndex + 1, lastStart, i,
              nextColumn.getColumnIdentifier()));
        }
        nextColumn = otherColumn;
        lastStart = i;
      }
    }

    // Add the column at index 'columnIndex + 1' of the last determinant
    ColumnIdentifier column = getColumnIdentifier(determinants, lastStart, columnIndex + 1);
    if (column != null) {
      children.add(
        printRecursive(dependant, determinants, path, columnIndex + 1, lastStart,
          determinantEndIndex,
          column.getColumnIdentifier()));
    }

    // Only add the children if they exist
    if (!children.isEmpty()) {
      result.put("children", children);
    }

    return result;
  }


  /**
   * Combines the bit sets of the determinants and dependant and calls calculate key error.
   *
   * @param determinants the determinants as bit set
   * @param dependant    the dependant as bit set
   * @return the key error
   */
  private long calculateKeyError(BitSet determinants, BitSet dependant) {
    BitSet bitSet = (BitSet) determinants.clone();
    bitSet.set(dependant.nextSetBit(0));
    return calculateKeyError(bitSet);
  }

  /**
   * Calculates the key error for the given columns using the PLIs of the table. The key error is
   * equal to the number of entries, which has to be removed, so that the columns become unique.
   *
   * @param columnBitSet the columns as bit set
   * @return the key error
   */
  private long calculateKeyError(BitSet columnBitSet) {
    Map<BitSet, PositionListIndex> PLIs = this.tableInformation.getPLIs();

    // the PLI already exists
    if (PLIs.containsKey(columnBitSet)) {
      return PLIs.get(columnBitSet).getRawKeyError();
    }

    // get all individual columns as bit set
    List<BitSet> columns = new ArrayList<>();
    for (int i = columnBitSet.nextSetBit(0); i != -1; i = columnBitSet.nextSetBit(i + 1)) {
      BitSet bitSet = new BitSet();
      bitSet.set(i);
      columns.add(bitSet);
    }

    // calculate the new PLI
    PositionListIndex pli = PLIs.get(columns.get(0));
    for (int i = 1; i < columns.size(); i++) {
      pli = pli.intersect(PLIs.get(columns.get(i)));
    }
    PLIs.put(columnBitSet, pli);
    this.tableInformation.setPLIs(PLIs);

    return pli.getRawKeyError();
  }

  /**
   * Replaces the "/" with the platform dependent separator
   *
   * @param path Path which should become platform dependent
   * @return Returns the platform dependent path
   */
  public String unifyPath(String path) {
    return path.replaceAll("/", Matcher.quoteReplacement(Constants.FILE_SEPARATOR));
  }

  /**
   * Combines two paths in a platform independent way
   *
   * @param dirPath  Path of the directory
   * @param filePath Path of the file inside the directory
   * @return Returns the path of file in the given directory
   */
  public String combinePaths(String dirPath, String filePath) {
    // Unify the directory path
    String correctDirPath = unifyPath(dirPath);
    // Remove the separator at the path ending
    if (correctDirPath.endsWith(Constants.FILE_SEPARATOR)) {
      correctDirPath =
        correctDirPath.substring(0, correctDirPath.length() - Constants.FILE_SEPARATOR.length());
    }

    // Unify the file path
    String correctFilePath = unifyPath(filePath);
    // Remove separator at the path beginning
    if (correctFilePath.startsWith(Constants.FILE_SEPARATOR)) {
      correctFilePath = correctFilePath.substring(Constants.FILE_SEPARATOR.length());
    }

    // Return the combination
    return correctDirPath + Constants.FILE_SEPARATOR + correctFilePath;
  }

  /**
   * Gets the column identifier on given index of the column combination, which is at the given
   * index of the given set.
   *
   * @param columnCombinations the column combinations
   * @param index              the index of the column combination in the set
   * @param columnIndex        the index of the column in the column combination
   * @return the required column identifier
   */
  protected ColumnIdentifier getColumnIdentifier(Set<ColumnCombination> columnCombinations,
                                                 int index,
                                                 int columnIndex) {
    int curIndex = -1;
    for (ColumnCombination combination : columnCombinations) {
      curIndex++;
      if (curIndex != index) {
        continue;
      }
      int curColumnIndex = -1;
      for (ColumnIdentifier columnIdentifier : combination.getColumnIdentifiers()) {
        curColumnIndex++;
        if (curColumnIndex == columnIndex) {
          return columnIdentifier;
        }
      }
    }

    return null;
  }

}
