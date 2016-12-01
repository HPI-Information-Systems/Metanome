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

import java.util.HashMap;


public class UniqueColumnCombinationVisualizationData {

  private static final double MIN_UNIQUENESS_FACTOR = 5;
  private static final double MAX_UNIQUENESS_FACTOR = 5;
  private static final double AVG_UNIQUENESS_FACTOR = 3;
  private static final double MIN_DISTANCE_FACTOR = 2;
  private static final double MAX_DISTANCE_FACTOR = 2;
  private static final double MEDIAN_DISTANCE_FACTOR = 2;
  private static final double COUNT_FACTOR = 0.15;
  private static final double RANDOMNESS_FACTOR = 1;

  private ColumnCombination columnCombination;
  private double minUniqueness;
  private double maxUniqueness;
  private double avgUniqueness;
  private double minDistance;
  private double maxDistance;
  private double medianDistance;
  private double columnCount;
  private double randomness;

  /**
   * Holds all information needed to plot the three different visualizations for unique column
   * combinations.
   *
   * @param columnCombination column combination
   * @param minUniqueness     the minimal uniqueness
   * @param maxUniqueness     the maximal uniqueness
   * @param avgUniqueness     the average uniqueness
   * @param minDistance       the minimal distance
   * @param maxDistance       the maximal distance
   * @param medianDistance    the median distance
   * @param columnCount       the column count
   * @param randomness        the randomness
   */
  public UniqueColumnCombinationVisualizationData(ColumnCombination columnCombination,
                                                  double minUniqueness,
                                                  double maxUniqueness, double avgUniqueness,
                                                  double minDistance, double maxDistance,
                                                  double medianDistance, double columnCount,
                                                  double randomness) {
    this.columnCombination = columnCombination;
    this.minUniqueness = minUniqueness;
    this.maxUniqueness = maxUniqueness;
    this.avgUniqueness = avgUniqueness;
    this.minDistance = minDistance;
    this.maxDistance = maxDistance;
    this.medianDistance = medianDistance;
    this.columnCount = columnCount;
    this.randomness = randomness;
  }

  /**
   * @return all values of the visualization data in a hash map
   */
  public HashMap<String, Double> getValues() {
    HashMap<String, Double> values = new HashMap<>();
//    values.put("Minimum Uniqueness", this.minUniqueness);
//    values.put("Maximum Uniqueness", this.maxUniqueness);
    values.put("Average Uniqueness", this.avgUniqueness);
//    values.put("Minimum Distance", this.minDistance);
//    values.put("Maximum Distance", this.maxDistance);
//    values.put("Median Distance", this.medianDistance);
    values.put("Number of Columns", this.columnCount);
    values.put("Randomness", this.randomness);

    return values;
  }

  /**
   * Converts the visualization data to a string.
   *
   * @return the string
   */
  public String toString() {
    String
      format =
      "{ minUniqueness: %f, maxUniqueness: %f, avgUniqueness: %f, minDistance: %f, maxDistance: "
        + "%f, medianDistance: %f, columnCount: %f, randomness: %f }";
    return String
      .format(format, minUniqueness, maxUniqueness, avgUniqueness, minDistance, maxDistance,
        medianDistance, columnCount, randomness);
  }

  /**
   * Calculates the difference between this visualization data point and another visualization data
   * point.
   *
   * @param other the other visualization data
   * @return the difference
   */
  public double calculateDiff(UniqueColumnCombinationVisualizationData other) {
    return (Math.abs(this.minUniqueness - other.getMinUniqueness()) * MIN_UNIQUENESS_FACTOR
      + Math.abs(this.maxUniqueness - other.getMaxUniqueness()) * MAX_UNIQUENESS_FACTOR
      + Math.abs(this.avgUniqueness - other.getAvgUniqueness()) * AVG_UNIQUENESS_FACTOR
      + Math.abs(this.minDistance - other.getMinDistance()) * MIN_DISTANCE_FACTOR
      + Math.abs(this.maxDistance - other.getMaxDistance()) * MAX_DISTANCE_FACTOR
      + Math.abs(this.medianDistance - other.getMedianDistance()) * MEDIAN_DISTANCE_FACTOR
      + Math.abs(this.columnCount - other.getColumnCount()) * COUNT_FACTOR
      + Math.abs(this.randomness - other.getRandomness()) * RANDOMNESS_FACTOR
    ) / 8;
  }

  public void setValues(double minUniqueness, double maxUniqueness, double avgUniqueness,
                        double minDistance, double maxDistance,
                        double medianDistance, double columnCount, double randomness) {
    this.minUniqueness = minUniqueness;
    this.maxUniqueness = maxUniqueness;
    this.avgUniqueness = avgUniqueness;
    this.minDistance = minDistance;
    this.maxDistance = maxDistance;
    this.medianDistance = medianDistance;
    this.columnCount = columnCount;
    this.randomness = randomness;
  }

  public double getMinUniqueness() {
    return minUniqueness;
  }

  public double getMaxUniqueness() {
    return maxUniqueness;
  }

  public double getAvgUniqueness() {
    return avgUniqueness;
  }

  public double getMinDistance() {
    return minDistance;
  }

  public double getMaxDistance() {
    return maxDistance;
  }

  public double getMedianDistance() {
    return medianDistance;
  }

  public double getColumnCount() {
    return columnCount;
  }

  public double getRandomness() {
    return randomness;
  }

  public ColumnCombination getColumnCombination() {
    return columnCombination;
  }

}
