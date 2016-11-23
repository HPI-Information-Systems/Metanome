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
package de.metanome.algorithms.testing.example_ucc_algorithm;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.algorithm_types.FileInputParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.IntegerParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.StringParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementInteger;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementString;
import de.metanome.algorithm_integration.input.FileInputGenerator;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;
import de.metanome.algorithm_integration.results.UniqueColumnCombination;

import java.util.ArrayList;

public class ExampleAlgorithm implements UniqueColumnCombinationsAlgorithm,
                                         StringParameterAlgorithm, FileInputParameterAlgorithm,
                                         IntegerParameterAlgorithm {

  protected String path1, path2 = null;
  protected UniqueColumnCombinationResultReceiver resultReceiver;

  public final static String STRING_IDENTIFIER = "path to output";
  public final static String FILE_IDENTIFIER = "input file";
  public final static String INTEGER_IDENTIFIER = "up to level";
  public final static String DESCRIPTION = "Example algorithm for unique column combinations.";
  public final static String AUTHORS = "Metanome Team";


  @Override
  public ArrayList<ConfigurationRequirement<?>> getConfigurationRequirements() {
    ArrayList<ConfigurationRequirement<?>> configurationRequirement = new ArrayList<>();

    ConfigurationRequirementString
        requirementString = new ConfigurationRequirementString(
        STRING_IDENTIFIER, 2);
    requirementString.setRequired(true);
    ConfigurationRequirementFileInput requirementFile = new ConfigurationRequirementFileInput(
        FILE_IDENTIFIER, 3, 5);
    requirementFile.setRequired(false);
    ConfigurationRequirementInteger
        requirementInteger = new ConfigurationRequirementInteger(
        INTEGER_IDENTIFIER, 1);
    requirementInteger.setDefaultValues(new Integer[]{3});
    requirementInteger.setRequired(true);

    configurationRequirement.add(requirementString);
    configurationRequirement.add(requirementFile);
    configurationRequirement.add(requirementInteger);

    return configurationRequirement;
  }

  @Override
  public void execute() throws AlgorithmExecutionException {
    if ((path1 != null) && (path2 != null)) {
      try {
        resultReceiver.receiveResult(new UniqueColumnCombination(
            new ColumnIdentifier("WDC_planets.csv", "Name"),
            new ColumnIdentifier("WDC_planets.csv", "Type")));
      } catch (CouldNotReceiveResultException | ColumnNameMismatchException e) {
        e.printStackTrace();
        throw e;
      }
    }
  }

  @Override
  public String getAuthors() {
    return AUTHORS;
  }

  @Override
  public String getDescription() {
    return DESCRIPTION;
  }

  @Override
  public void setResultReceiver(
      UniqueColumnCombinationResultReceiver resultReceiver) {
    this.resultReceiver = resultReceiver;
  }

  @Override
  public void setStringConfigurationValue(String identifier, String... values)
      throws AlgorithmConfigurationException {
    if ((identifier.equals(STRING_IDENTIFIER)) && (values.length == 2)) {
      path1 = values[0];
      path2 = values[1];
    } else {
      throw new AlgorithmConfigurationException("Incorrect identifier or value list length.");
    }
  }

  @Override
  public void setFileInputConfigurationValue(String identifier, FileInputGenerator... values)
      throws AlgorithmConfigurationException {
    if (!identifier.equals(FILE_IDENTIFIER) && values.length < 3 && values.length > 5) {
      throw new AlgorithmConfigurationException("Incorrect identifier or value list length.");
    }
  }

  @Override
  public void setIntegerConfigurationValue(String identifier, Integer... values)
      throws AlgorithmConfigurationException {
    if (!identifier.equals(INTEGER_IDENTIFIER) && values.length != 1) {
      throw new AlgorithmConfigurationException("Incorrect identifier or value list length.");
    }
  }
}
