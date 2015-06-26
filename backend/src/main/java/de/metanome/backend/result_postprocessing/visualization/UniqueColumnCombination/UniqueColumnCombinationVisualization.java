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

package de.metanome.backend.result_postprocessing.visualization.UniqueColumnCombination;

import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.backend.result_postprocessing.helper.ColumnInformation;
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.results.UniqueColumnCombinationResult;
import de.metanome.backend.result_postprocessing.visualization.JSONPrinter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Prepares the data to visualize unique column combinations and writes them to disc.
 */
public class UniqueColumnCombinationVisualization {

  private List<UniqueColumnCombinationResult> results;
  private TableInformation tableInformation;
  private Map<String, Double> columnUniqueness;


  public UniqueColumnCombinationVisualization(List<UniqueColumnCombinationResult> results,
                                              TableInformation tableInformation) {
    this.results = results;
    this.tableInformation = tableInformation;
    this.columnUniqueness = new HashMap<>();

    // Calculate uniqueness value for each column
    long rowCount = this.tableInformation.getRowCount();
    for (ColumnInformation column : this.tableInformation.getColumnInformationMap().values()) {
      columnUniqueness.put(column.getColumnName(),
                           (double) column.getDistinctValuesCount() / rowCount);
    }
  }

  /**
   * Creates all visualization data and writes them to disc.
   */
  public void createVisualizationData() {
    // Convert results into visualization results
    List<UniqueColumnCombinationVisualizationData> visualizationResults = new LinkedList<>();
    for (UniqueColumnCombinationResult result : this.results) {
      if (result.getColumnCombination().getColumnIdentifiers().size() > 1) {
        visualizationResults.add(createVisualizationResult(result));
      }
    }

    if (visualizationResults.size() > 1) {
      // Cluster the data
      KMeans kMeans = new KMeans();
      kMeans.cluster(visualizationResults);

      // Get the storage directory path
      String currentPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
      currentPath = currentPath + "../../visualization/UCCResultAnalyzer/";

      // Print data for visualization to files
      JSONPrinter.printCluster(currentPath + "/UCCClusters.json", kMeans.getClusterInformation());
      JSONPrinter.printClusterData(currentPath + "/UCCData.json", kMeans.getClusters());
      JSONPrinter.printColumnCombinations(
          currentPath + "/UCCHistograms.json",
          getClusterData(kMeans.getAllColumnCombinationsOfClusters()));
    }

  }

  /**
   * Converts the given unique column combination result to visualization data.
   *
   * @param result the result
   * @return the visualization data
   */
  protected UniqueColumnCombinationVisualizationData createVisualizationResult(
      UniqueColumnCombinationResult result) {
    List<Double> uniqueness = new LinkedList<>();
    List<Double> distances = new LinkedList<>();
    double columnCount = result.getColumnCombination().getColumnIdentifiers().size();

    // Get uniqueness values for each column
    for (ColumnIdentifier column : result.getColumnCombination().getColumnIdentifiers()) {
      uniqueness.add(this.columnUniqueness.get(column.getColumnIdentifier()));
    }

    // Calculate average uniqueness
    double sumUniqueness = 0;
    for (double d : uniqueness) {
      sumUniqueness += d;
    }
    double avgUniqueness = sumUniqueness / uniqueness.size();

    // Calculates distances for each column
    Collections.sort(uniqueness);
    for (int i = 0; i < uniqueness.size() - 1; i++) {
      distances.add(i, uniqueness.get(i + 1) - uniqueness.get(i));
    }
    Collections.sort(distances);

    return new UniqueColumnCombinationVisualizationData(
        result.getColumnCombination(),
        uniqueness.get(0),
        uniqueness.get(uniqueness.size() - 1),
        avgUniqueness,
        distances.get(0),
        distances.get(distances.size() - 1),
        distances.get(distances.size() / 2),
        columnCount,
        result.getRandomness());
  }

  /**
   * For each cluster and column combination in a cluster create a map of the column name and its
   * uniqueness value.
   *
   * @param clusterContent the column combinations of each cluster
   * @return map of column name and uniqueness value for each column combination and cluster
   */
  private List<List<HashMap<String, Double>>> getClusterData(
      List<List<ColumnCombination>> clusterContent) {
    List<List<HashMap<String, Double>>> clusters = new ArrayList<>();

    for (List<ColumnCombination> columnCombinations : clusterContent) {
      List<HashMap<String, Double>> clusterData = new ArrayList<>();
      for (ColumnCombination columnCombination : columnCombinations) {
        clusterData.add(getColumnCombinationUniqueness(columnCombination));
      }
      clusters.add(clusterData);
    }
    return clusters;
  }

  /**
   * Maps the name of the column to its uniqueness values for each column in the given unique column
   * combination
   *
   * @param columnCombination the unique column combination
   * @return the map
   */
  private HashMap<String, Double> getColumnCombinationUniqueness(
      ColumnCombination columnCombination) {
    HashMap<String, Double> columnCombinationUniqueness = new HashMap<>();
    for (ColumnIdentifier column : columnCombination.getColumnIdentifiers()) {
      columnCombinationUniqueness.put(column.getColumnIdentifier(),
                                      this.columnUniqueness.get(column.getColumnIdentifier()));
    }
    return columnCombinationUniqueness;
  }

}
