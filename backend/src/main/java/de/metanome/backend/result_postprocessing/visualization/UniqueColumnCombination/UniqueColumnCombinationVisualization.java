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
package de.metanome.backend.result_postprocessing.visualization.UniqueColumnCombination;

import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.backend.constants.Constants;
import de.metanome.backend.result_postprocessing.helper.ColumnInformation;
import de.metanome.backend.result_postprocessing.helper.TableInformation;
import de.metanome.backend.result_postprocessing.results.UniqueColumnCombinationResult;
import de.metanome.backend.result_postprocessing.visualization.JSONPrinter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * Prepares the data to visualize unique column combinations and writes them to disc.
 */
public class UniqueColumnCombinationVisualization {

  protected List<UniqueColumnCombinationResult> results;
  protected Map<String, Double> columnUniqueness;


  public UniqueColumnCombinationVisualization(List<UniqueColumnCombinationResult> results,
                                              TableInformation tableInformation) {
    this.results = results;
    this.columnUniqueness = new HashMap<>();

    // Calculate uniqueness value for each column
    long rowCount = tableInformation.getRowCount();
    for (ColumnInformation column : tableInformation.getColumnInformationMap().values()) {
      columnUniqueness.put(column.getColumnName(),
        (double) column.getDistinctValuesCount() / rowCount);
    }

  }

  /**
   * Creates all visualization data and writes them to disc.
   * @throws java.io.FileNotFoundException if file could not be found
   */
  public void createVisualizationData() throws FileNotFoundException {
    // Get paths for the json files
    String currentPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    currentPath = currentPath + "../../src/visualization/UCCResultAnalyzer/";
    String clusterFile = currentPath + "/UCCClusters.json";
    String dataFile = currentPath + "/UCCData.json";
    String histogramFile = currentPath + "/UCCHistograms.json";

    // Clear the json files
    JSONPrinter.clearFile(clusterFile);
    JSONPrinter.clearFile(clusterFile);
    JSONPrinter.clearFile(clusterFile);

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

      // Print data for visualization to files
      printCluster(clusterFile, kMeans.getClusterInformation());
      printClusterData(dataFile, kMeans.getClusters());
      printColumnCombinations(histogramFile,
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
  protected HashMap<String, Double> getColumnCombinationUniqueness(
    ColumnCombination columnCombination) {
    HashMap<String, Double> columnCombinationUniqueness = new HashMap<>();
    for (ColumnIdentifier column : columnCombination.getColumnIdentifiers()) {
      columnCombinationUniqueness.put(column.getColumnIdentifier(),
        this.columnUniqueness.get(column.getColumnIdentifier()));
    }
    return columnCombinationUniqueness;
  }


  /**
   * Prints information about clusters to file.
   *
   * @param filePath File path to the output file
   * @param clusters List of clusters
   */
  @SuppressWarnings(Constants.SUPPRESS_WARNINGS_UNCHECKED)
  public void printCluster(String filePath,
                           List<HashMap<String, Double>> clusters) {
    JSONArray jsonCluster = new JSONArray();

    for (int i = 0; i < clusters.size(); i++) {
      HashMap<String, Double> info = clusters.get(i);
      JSONObject jsonEntry = new JSONObject();

      jsonEntry.put("ClusterNr", i);
      for (Map.Entry<String, Double> infoEntry : info.entrySet()) {
        jsonEntry.put(infoEntry.getKey(), infoEntry.getValue());
      }
      jsonCluster.add(jsonEntry);
    }

    JSONPrinter.writeToFile(filePath, jsonCluster);
  }

  /**
   * Prints the contained data of all clusters to file.
   *
   * @param filePath    File path to the output file
   * @param clusterData Data of the cluster
   */
  @SuppressWarnings(Constants.SUPPRESS_WARNINGS_UNCHECKED)
  public void printClusterData(String filePath,
                               List<List<UniqueColumnCombinationVisualizationData>> clusterData) {
    JSONArray jsonData = new JSONArray();
    int id = 0;

    for (int i = 0; i < clusterData.size(); i++) {
      List<UniqueColumnCombinationVisualizationData> dataList = clusterData.get(i);

      for (UniqueColumnCombinationVisualizationData data : dataList) {
        HashMap<String, Double> info = data.getValues();
        JSONObject jsonEntry = new JSONObject();

        //Insert Cluster Number
        jsonEntry.put("ClusterNr", i);
        //Insert UCC ID
        jsonEntry.put("UCCid", id);

        //Insert all info items
        for (Map.Entry<String, Double> infoEntry : info.entrySet()) {
          jsonEntry.put(infoEntry.getKey(), infoEntry.getValue());
        }

        jsonData.add(jsonEntry);
        id++;
      }
    }
    JSONPrinter.writeToFile(filePath, jsonData);
  }


  /**
   * Prints the data of the cluster for a histogram.
   *
   * @param filePath File path to the output file
   * @param clusters Clusters containing the information for plotting histograms
   */
  @SuppressWarnings(Constants.SUPPRESS_WARNINGS_UNCHECKED)
  public void printColumnCombinations(String filePath,
                                      List<List<HashMap<String, Double>>> clusters) {
    JSONArray json = new JSONArray();
    int id = 0;

    for (List<HashMap<String, Double>> cluster : clusters) {
      for (HashMap<String, Double> data : cluster) {
        JSONObject entryJSON = new JSONObject();

        // Insert UCC ID
        entryJSON.put("UCCid", id);

        // Insert Histogram Data
        JSONArray jsonData = new JSONArray();

        for (Map.Entry<String, Double> dataPoint : data.entrySet()) {
          JSONObject jsonEntry = new JSONObject();
          jsonEntry.put("Column Name", dataPoint.getKey());
          jsonEntry.put("Uniqueness", dataPoint.getValue());
          jsonData.add(jsonEntry);
        }

        entryJSON.put("histogramData", jsonData);
        json.add(entryJSON);
        id++;
      }
    }

    JSONPrinter.writeToFile(filePath, json);
  }


}
