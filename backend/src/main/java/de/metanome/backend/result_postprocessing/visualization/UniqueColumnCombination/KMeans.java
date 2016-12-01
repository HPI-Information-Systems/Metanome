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

import java.util.*;

/**
 * Clusters the unique column combination visualization data.
 */
public class KMeans {

  private static final double
    THRESHOLD = 0.03;
  private static final double CLUSTER_COUNT_FACTOR = 1.5;

  private List<List<UniqueColumnCombinationVisualizationData>> clusters;
  private List<List<UniqueColumnCombinationVisualizationData>> previousClusters;
  private List<UniqueColumnCombinationVisualizationData> centroids;
  private List<Double> diff;
  private int clusterCount;


  /**
   * Clusters the given data.
   *
   * @param data the data to cluster
   */
  public void cluster(List<UniqueColumnCombinationVisualizationData> data) {
    int clusterCount = 2;
    double prev = 5, prevprev = 5, improvement = 1;
    double cohesion;

    while (improvement > THRESHOLD && clusterCount < data.size() / CLUSTER_COUNT_FACTOR) {
      this.clusterCount = clusterCount;
      resetAll();
      initializeCentroids(data);

      // get the clusters for the given cluster count
      do {
        findClusters(data);
        updateCentroids();
      } while (!isFinished());

      // calculate the cohesion
      cohesion = calculateCohesion();
      improvement = 1 - cohesion / prevprev;
      prevprev = prev;
      prev = cohesion;

      updatePreviousClusters();
      clusterCount++;
    }
  }

  /**
   * Assigns each data point to the nearest cluster.
   *
   * @param dataPoints the data points
   */
  private void findClusters(List<UniqueColumnCombinationVisualizationData> dataPoints) {
    // clear old cluster assignment
    for (List<UniqueColumnCombinationVisualizationData> cluster : this.clusters) {
      cluster.clear();
    }

    // for each data point find the nearest cluster and assign it to this cluster
    for (UniqueColumnCombinationVisualizationData dataPoint : dataPoints) {
      int clusterIndex = getNearestCluster(dataPoint);
      this.clusters.get(clusterIndex).add(dataPoint);
    }
  }

  /**
   * Find the index of the nearest cluster for the given data point.
   *
   * @param dataPoint the data point
   * @return the index of the nearest cluster
   */
  private int getNearestCluster(UniqueColumnCombinationVisualizationData dataPoint) {
    this.diff.clear();
    for (int i = 0; i < this.clusters.size(); ++i) {
      this.diff.add(i, this.centroids.get(i).calculateDiff(dataPoint));
    }
    return diff.indexOf(Collections.min(diff));
  }

  /**
   * Updates the cluster centroids.
   */
  private void updateCentroids() {
    this.centroids.clear();
    for (int i = 0; i < this.clusters.size(); i++) {
      this.centroids.add(i, calculateMean(this.clusters.get(i)));
    }
  }

  /**
   * Calculates the mean of the given data list.
   *
   * @param data a list of unique column combination visualization data
   * @return the mean values of all given data points
   */
  private UniqueColumnCombinationVisualizationData calculateMean(
    List<UniqueColumnCombinationVisualizationData> data) {
    double tempMin = 0;
    double tempMax = 0;
    double tempAvg = 0;
    double tempMinDist = 0;
    double tempMaxDist = 0;
    double tempMeanDist = 0;
    double tempCount = 0;
    double tempRandomness = 0;

    for (UniqueColumnCombinationVisualizationData d : data) {
      tempMin += d.getMinUniqueness();
      tempMax += d.getMaxUniqueness();
      tempAvg += d.getAvgUniqueness();
      tempMinDist += d.getMinDistance();
      tempMaxDist += d.getMaxDistance();
      tempMeanDist += d.getMedianDistance();
      tempCount += d.getColumnCount();
      tempRandomness += d.getRandomness();
    }

    return new UniqueColumnCombinationVisualizationData(
      null,
      tempMin / data.size(),
      tempMax / data.size(),
      tempAvg / data.size(),
      tempMinDist / data.size(),
      tempMaxDist / data.size(),
      tempMeanDist / data.size(),
      tempCount / data.size(),
      tempRandomness / data.size());
  }

  /**
   * @return the clusters
   */
  public List<List<UniqueColumnCombinationVisualizationData>> getClusters() {
    return this.clusters;
  }

  /**
   * @return all column combinations in a cluster
   */
  public List<List<ColumnCombination>> getAllColumnCombinationsOfClusters() {
    List<List<ColumnCombination>> result = new LinkedList<>();

    for (List<UniqueColumnCombinationVisualizationData> cluster : this.clusters) {
      List<ColumnCombination> columnCombinations = new LinkedList<>();
      for (UniqueColumnCombinationVisualizationData dataPoint : cluster) {
        columnCombinations.add(dataPoint.getColumnCombination());
      }
      result.add(columnCombinations);
    }

    return result;
  }

  /**
   * @return a map containing general information about the cluster (e.g. size, average number of
   * columns, ...)
   */
  public List<HashMap<String, Double>> getClusterInformation() {
    List<HashMap<String, Double>> clusterInformationList = new ArrayList<>();

    for (List<UniqueColumnCombinationVisualizationData> cluster : this.clusters) {
      UniqueColumnCombinationVisualizationData centroid = calculateMean(cluster);
      HashMap<String, Double> clusterInformation = new HashMap<>();
      clusterInformation.put("Size", (double) cluster.size());
      clusterInformation.put("Average Number of Columns", centroid.getColumnCount());
      clusterInformation.put("Average Uniqueness", centroid.getAvgUniqueness());
      clusterInformation.put("Randomness", centroid.getRandomness());
      clusterInformationList.add(clusterInformation);
    }

    return clusterInformationList;
  }

  /**
   * Checks if the previous clusters are the same as the current clusters
   *
   * @return true, if the clusters are the same, false otherwise
   */
  private boolean isFinished() {
    for (int i = 0; i < this.clusters.size() && i < this.previousClusters.size(); ++i) {
      if (this.previousClusters.get(i).size() != this.clusters.get(i).size()) {
        updatePreviousClusters();
        return false;
      }
      if (!(this.previousClusters.get(i).containsAll(this.clusters.get(i)) && this.clusters.get(i)
        .containsAll(this.previousClusters.get(i)))) {
        updatePreviousClusters();
        return false;
      }
    }
    updatePreviousClusters();
    return true;
  }

  /**
   * Copies the clusters to the previous clusters.
   */
  private void updatePreviousClusters() {
    this.previousClusters = new ArrayList<>();
    this.previousClusters.addAll(this.clusters);
  }


  /**
   * Resets all data structures.
   */
  private void resetAll() {
    this.previousClusters = new LinkedList<>();
    this.clusters = new LinkedList<>();
    this.centroids = new LinkedList<>();
    this.diff = new LinkedList<>();

    for (int i = 0; i < this.clusterCount; i++) {
      this.clusters.add(new LinkedList<UniqueColumnCombinationVisualizationData>());
    }
  }

  /**
   * Gets the maximal difference between a centroid and a data point.
   *
   * @return the maximal difference
   */
  private double calculateCohesion() {
    double maxDiff = 0;
    for (int i = 0; i < this.clusters.size(); i++) {
      for (int j = 0; j < this.clusters.get(i).size(); j++) {
        maxDiff =
          Math.max(maxDiff, this.centroids.get(i).calculateDiff(this.clusters.get(i).get(j)));
      }
    }
    return maxDiff;
  }

  /**
   * Initializes the cluster centroids: The data points, which have the highest distances to the
   * mean value of all data points, are chosen.
   *
   * @param dataPoints the data points
   */
  private void initializeCentroids(List<UniqueColumnCombinationVisualizationData> dataPoints) {
    UniqueColumnCombinationVisualizationData meanDataPoint = calculateMean(dataPoints);

    // Calculate the distance from all data points to the mean data point
    List<Double> distances = new ArrayList<>();
    for (int i = 0; i < dataPoints.size(); i++) {
      distances.add(i, dataPoints.get(i).calculateDiff(meanDataPoint));
    }

    // sort distances in reverse order
    List<Double> sortedDistances = new ArrayList<>();
    sortedDistances.addAll(distances);
    Collections.sort(sortedDistances, Collections.reverseOrder());

    // add the data points with the highest distance to the centroids
    for (int j = 0; j < this.clusters.size(); j++) {
      double dist = sortedDistances.get(j);
      this.centroids.add(dataPoints.get(distances.indexOf(dist)));
    }
  }


}
