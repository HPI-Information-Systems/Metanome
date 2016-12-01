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
package de.metanome.algorithms.testing.example_holistic_algorithm;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.StringParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementString;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;
import de.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;
import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.algorithm_integration.results.UniqueColumnCombination;

import java.util.ArrayList;

public class ExampleAlgorithm
    implements FunctionalDependencyAlgorithm, UniqueColumnCombinationsAlgorithm,
               StringParameterAlgorithm {

  protected String path = null;
  protected FunctionalDependencyResultReceiver fdResultReceiver;
  protected UniqueColumnCombinationResultReceiver uccResultReceiver;

  @Override
  public ArrayList<ConfigurationRequirement<?>> getConfigurationRequirements() {
    ArrayList<ConfigurationRequirement<?>> configurationRequirement = new ArrayList<>();

    ConfigurationRequirementString
        requirementString = new ConfigurationRequirementString(
        "pathToOutputFile", 2);
    requirementString.setRequired(true);

    configurationRequirement.add(requirementString);

    return configurationRequirement;
  }

  @Override
  public void execute() throws AlgorithmConfigurationException {
    if (path != null) {
      try {
        fdResultReceiver.receiveResult(
            new FunctionalDependency(
                new ColumnCombination(
                    new ColumnIdentifier("WDC_planets.csv", "Name"),
                    new ColumnIdentifier("WDC_planets.csv", "Type")),
                new ColumnIdentifier("WDC_planets.csv", "Mass")
            )
        );
      } catch (CouldNotReceiveResultException e) {
        throw new AlgorithmConfigurationException("Could not write result.");
      } catch (ColumnNameMismatchException e) {
        throw new AlgorithmConfigurationException("Mismatch with column names!");
      }
      try {
        uccResultReceiver.receiveResult(new UniqueColumnCombination(
            new ColumnIdentifier("WDC_planets.csv", "Rings"),
            new ColumnIdentifier("WDC_planets.csv", "Name")));
      } catch (CouldNotReceiveResultException e) {
        throw new AlgorithmConfigurationException("Could not write result.");
      } catch (ColumnNameMismatchException e) {
        throw new AlgorithmConfigurationException("Mismatch with column names!");
      }
    }
  }

  @Override
  public void setResultReceiver(
      FunctionalDependencyResultReceiver resultReceiver) {
    this.fdResultReceiver = resultReceiver;
  }

  @Override
  public void setResultReceiver(
      UniqueColumnCombinationResultReceiver resultReceiver) {
    this.uccResultReceiver = resultReceiver;

  }

  @Override
  public void setStringConfigurationValue(String identifier, String... values)
      throws AlgorithmConfigurationException {
    if ((identifier.equals("pathToOutputFile")) && (values.length == 2)) {
      path = values[0];
    } else {
      throw new AlgorithmConfigurationException("Incorrect identifier or value list length.");
    }
  }

  @Override
  public String getAuthors() {
    return "Metanome Team";
  }

  @Override
  public String getDescription() {
    return "Example algorithm for unique column combinations and functional dependencies.";
  }

}
