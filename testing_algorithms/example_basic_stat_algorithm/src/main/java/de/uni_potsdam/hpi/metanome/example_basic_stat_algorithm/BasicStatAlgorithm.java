/*
 * Copyright 2014 by the Metanome project
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

package de.uni_potsdam.hpi.metanome.example_basic_stat_algorithm;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.BasicStatisticsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.FileInputParameterAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.FileInputGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.BasicStatisticsResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.BasicStatistic;

import java.util.LinkedList;
import java.util.List;

/**
 * An example algorithm for testing that expects 5 {@link de.uni_potsdam.hpi.metanome.algorithm_integration.input.FileInputGenerator}s
 * and sends a {@link de.uni_potsdam.hpi.metanome.algorithm_integration.results.BasicStatistic} to
 * the result receiver.
 *
 * @author Jakob Zwiener
 */
public class BasicStatAlgorithm implements BasicStatisticsAlgorithm, FileInputParameterAlgorithm {

  public static final String INPUT_FILE_IDENTIFIER = "input file";
  public static final int NUMBER_OF_INPUT_FILES = 5;
  public static final String STATISTIC_NAME = "file name statistic";
  public static final ColumnIdentifier
      COLUMN_IDENTIFIER =
      new ColumnIdentifier("testTable", "testColumn");

  protected BasicStatisticsResultReceiver resultReceiver = null;
  protected FileInputGenerator[] inputs = null;

  @Override
  public List<ConfigurationSpecification> getConfigurationRequirements() {
    List<ConfigurationSpecification> configuration = new LinkedList<>();

    configuration.add(new ConfigurationSpecificationCsvFile(INPUT_FILE_IDENTIFIER,
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

    String filePath = inputs[4].getInputFile().getAbsolutePath();

    resultReceiver.receiveResult(new BasicStatistic(STATISTIC_NAME, filePath, COLUMN_IDENTIFIER));
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
}
