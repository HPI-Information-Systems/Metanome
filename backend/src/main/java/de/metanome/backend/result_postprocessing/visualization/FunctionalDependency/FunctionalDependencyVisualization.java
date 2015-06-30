/*
 * Copyright 2015 by the Metanome project
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

import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.results.FunctionalDependencyResult;
import de.metanome.backend.result_postprocessing.visualization.JSONPrinter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;


public class FunctionalDependencyVisualization {

  private List<FunctionalDependencyResult> results;
  private TableInformation tableInformation;


  public FunctionalDependencyVisualization(List<FunctionalDependencyResult> results,
                                           TableInformation tableInformation) {
    this.results = results;
    this.tableInformation = tableInformation;
  }

  /**
   * Creates all visualization data and writes them to disc.
   */
  public void createVisualizationData() {
    // Create a map from a dependant to all its determinants
    Map<ColumnIdentifier, Set<ColumnCombination>>
        dependantMap = createDependantMap(this.results);

    // Get the storage directory path
    String currentPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();

    // Print to JSON file
    printFunctionalDependencyVisualizationData(
        currentPath + "../../visualization/FDResultAnalyzer/",
        this.results,
        dependantMap,
        this.tableInformation);
  }

  /**
   * Creates a reverted index on the functional dependency dependants.
   *
   * @param results the functional dependencies
   * @return a mapping of each dependant to a sorted list of all its determinants
   */
  private Map<ColumnIdentifier, Set<ColumnCombination>> createDependantMap(
      List<FunctionalDependencyResult> results) {
    Map<ColumnIdentifier, Set<ColumnCombination>> dependantMap = new HashMap<>();

    // Add for each dependant column all its determinant columns to the map
    for (FunctionalDependencyResult result : results) {
      ColumnIdentifier dependantColumn = result.getDependant();
      if (!dependantMap.containsKey(dependantColumn)) {
        dependantMap.put(dependantColumn, new TreeSet<ColumnCombination>());
      };
      dependantMap.get(dependantColumn).add(result.getDeterminant());
    }

    return dependantMap;
  }

  /**
   * Prints the data for the visualization for functional dependencies.
   *
   * @param filePath         the file
   * @param results          the results
   * @param dependantMap     the dependant map
   * @param tableInformation the table information
   */
  @SuppressWarnings("unchecked")
  public void printFunctionalDependencyVisualizationData(String filePath,
                                                         List<FunctionalDependencyResult> results,
                                                         Map<ColumnIdentifier, Set<ColumnCombination>> dependantMap,
                                                         TableInformation tableInformation) {

    // Table name should be the root
    JSONObject root = new JSONObject();
    root.put("name", tableInformation.getTableName());
    //store table size in root to show it in keyError
    root.put("tableSize", tableInformation.getRowCount());

    // Build tree for each dependant column
    JSONArray rootChildren = new JSONArray();

    // Sort the dependant columns based on their index
    List<ColumnIdentifier> dependantColumns = new ArrayList<>(dependantMap.keySet());
    Collections.sort(dependantColumns);

    for (ColumnIdentifier dependantColumn : dependantColumns) {
      Set<ColumnCombination> determinants = dependantMap.get(dependantColumn);

      // Create a JSON for the dependant column
      JSONObject dependantJSON = printRecursive(dependantColumn, determinants, -1, 0, determinants.size(), "");

      // Change the root name to the dependant column name
      dependantJSON.put("name", dependantColumn.getColumnIdentifier());
      // Add it to the table's children array
      rootChildren.add(dependantJSON);
    }

    root.put("children", rootChildren);

    filePath = combinePaths(filePath, "PrefixTree.json");
    JSONPrinter.writeToFile(filePath, root);
  }


  /**
   * Recursive helper function for printing prefix trees as JSON
   *
   * @return the prefix tree branch as JSON defined by the parameters
   */
  @SuppressWarnings("unchecked")
  protected JSONObject printRecursive(ColumnIdentifier dependant,
                                    Set<ColumnCombination> determinants,
                                    int columnIndex,
                                    int determinantStartIndex,
                                    int determinantEndIndex,
                                    String columnName) {
    // Prepare the result structure
    JSONObject result = new JSONObject();
    // Prepare the children array of the result
    JSONArray children = new JSONArray();

    if (columnIndex >= 0) {
      result.put("name", columnName);
      result.put("size", tableInformation.getColumnInformationMap().get(columnName).getUniquenessRate());
      result.put("keyError", 1);
    }

    // Add all columns of determinants at index 'columnIndex + 1'
    ColumnIdentifier nextColumn = get(determinants, determinantStartIndex, columnIndex + 1);
    int lastStart = determinantStartIndex;
    for (int i = determinantStartIndex + 1; i < determinantEndIndex; i++) {
      ColumnIdentifier otherColumn = get(determinants, i, columnIndex + 1);
      if (otherColumn != null && !otherColumn.equals(nextColumn)) {
        if (nextColumn != null) {
          children.add(
              printRecursive(dependant, determinants, columnIndex + 1, lastStart, i, nextColumn.getColumnIdentifier()));
        }
        nextColumn = otherColumn;
        lastStart = i;
      }
    }

    // Add the column at index 'columnIndex + 1' of the last determinant
    ColumnIdentifier column = get(determinants, lastStart, columnIndex + 1);
    if (column != null) {
      children.add(
          printRecursive(dependant, determinants, columnIndex + 1, lastStart, determinantEndIndex, column.getColumnIdentifier()));
    }

    // Only add the children if they exist
    if (!children.isEmpty()) {
      result.put("children", children);
    }

    return result;
  }

  protected static ColumnIdentifier get(Set<ColumnCombination> columnCombinations, int index,
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
        if (curColumnIndex == columnIndex)
          return columnIdentifier;
      }
    }

    return null;
  }

  /**
   * Replaces the "/" with the platform dependent separator
   *
   * @param path Path which should become platform dependent
   * @return Returns the platform dependent path
   */
  public String unifyPath(String path) {
    return path.replaceAll("/", Matcher.quoteReplacement(File.separator));
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
    if (correctDirPath.endsWith(File.separator)) {
      correctDirPath =
          correctDirPath.substring(0, correctDirPath.length() - File.separator.length());
    }

    // Unify the file path
    String correctFilePath = unifyPath(filePath);
    // Remove separator at the path beginning
    if (correctFilePath.startsWith(File.separator)) {
      correctFilePath = correctFilePath.substring(File.separator.length());
    }

    // Return the combination
    return correctDirPath + File.separator + correctFilePath;
  }


}
