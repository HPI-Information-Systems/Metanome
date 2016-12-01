/**
 * Copyright 2014-2016 by Metanome Project
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
package de.metanome.algorithms.testing.example_basic_stat_algorithm;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.algorithm_types.BasicStatisticsAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.FileInputParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput;
import de.metanome.algorithm_integration.input.FileInputGenerator;
import de.metanome.algorithm_integration.result_receiver.BasicStatisticsResultReceiver;
import de.metanome.algorithm_integration.results.BasicStatistic;
import de.metanome.algorithm_integration.results.basic_statistic_values.BasicStatisticValueDouble;
import de.metanome.algorithm_integration.results.basic_statistic_values.BasicStatisticValueInteger;
import de.metanome.algorithm_integration.results.basic_statistic_values.BasicStatisticValueIntegerList;

import java.util.ArrayList;
import java.util.List;

/**
 * An example algorithm for testing that expects 5 {@link de.metanome.algorithm_integration.input.FileInputGenerator}s
 * and sends a {@link de.metanome.algorithm_integration.results.BasicStatistic} to the result
 * receiver.
 *
 * @author Jakob Zwiener
 */
public class BasicStatAlgorithm implements BasicStatisticsAlgorithm, FileInputParameterAlgorithm {

  public static final String INPUT_FILE_IDENTIFIER = "input file";
  public static final int NUMBER_OF_INPUT_FILES = 5;
  public static final String STATISTIC_NAME_MIN = "MIN";
  public static final Integer STATISTIC_VALUE_MIN = 5;
  public static final String STATISTIC_NAME_MAX = "MAX";
  public static final Integer STATISTIC_VALUE_MAX = 10;
  public static final String STATISTIC_NAME_AVG = "AVG";
  public static final Double STATISTIC_VALUE_AVG = 7.3;
  public static final ColumnIdentifier
      COLUMN_IDENTIFIER_1 =
      new ColumnIdentifier("WDC_planets.csv", "Name");
  public static final ColumnIdentifier
    COLUMN_IDENTIFIER_2 =
    new ColumnIdentifier("WDC_planets.csv", "Type");
  protected BasicStatisticsResultReceiver resultReceiver = null;
  protected FileInputGenerator[] inputs = null;

  @Override
  public ArrayList<ConfigurationRequirement<?>> getConfigurationRequirements() {
    ArrayList<ConfigurationRequirement<?>> configuration = new ArrayList<>();

    configuration.add(new ConfigurationRequirementFileInput(INPUT_FILE_IDENTIFIER,
                                                            NUMBER_OF_INPUT_FILES));

    return configuration;
  }

  @Override
  public void execute() throws AlgorithmExecutionException {
    if (resultReceiver == null) {
      throw new AlgorithmExecutionException("No result receiver was set.");
    }
    if (inputs == null) {
      throw new AlgorithmExecutionException("No inputs were set.");
    }
    if (inputs.length != NUMBER_OF_INPUT_FILES) {
      throw new AlgorithmExecutionException("Wrong number of inputs set.");
    }

    BasicStatistic result1 = new BasicStatistic(COLUMN_IDENTIFIER_1);
    result1.addStatistic(STATISTIC_NAME_MIN, new BasicStatisticValueInteger(STATISTIC_VALUE_MIN));
    result1.addStatistic(STATISTIC_NAME_MAX, new BasicStatisticValueInteger(STATISTIC_VALUE_MAX));
    result1.addStatistic(STATISTIC_NAME_AVG, new BasicStatisticValueDouble(STATISTIC_VALUE_AVG));

    List<Integer> list = new ArrayList<>();
    list.add(4);
    list.add(2);
    list.add(1);
    result1.addStatistic("DISTINCT", new BasicStatisticValueIntegerList(list));

    BasicStatistic result2 = new BasicStatistic(COLUMN_IDENTIFIER_2);
    result2.addStatistic(STATISTIC_NAME_MIN, new BasicStatisticValueInteger(STATISTIC_VALUE_MIN));
    result2.addStatistic(STATISTIC_NAME_AVG, new BasicStatisticValueDouble(STATISTIC_VALUE_AVG));

    resultReceiver.receiveResult(result1);
    resultReceiver.receiveResult(result2);
  }

  @Override
  public void setResultReceiver(BasicStatisticsResultReceiver resultReceiver) {
    this.resultReceiver = resultReceiver;
  }

  @Override
  public void setFileInputConfigurationValue(String identifier, FileInputGenerator... values)
      throws AlgorithmConfigurationException {
    if (!identifier.equals(INPUT_FILE_IDENTIFIER)) {
      throw new AlgorithmConfigurationException(
          "Metanome attempted to set a file input with wrong identifier.");
    }
    if (values.length != NUMBER_OF_INPUT_FILES) {
      throw new AlgorithmConfigurationException(
          "Metanome attempted to set an incorrect number of file inputs.");
    }
    inputs = values;
  }

  @Override
  public String getAuthors() {
    return "Metanome Team";
  }

  @Override
  public String getDescription() {
    return "Example algorithm for basic statistics.";
  }
}
