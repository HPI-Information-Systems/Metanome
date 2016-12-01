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
package de.metanome.algorithms.testing.example_od_algorithm;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.algorithm_types.OrderDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.StringParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementString;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OrderDependencyResultReceiver;
import de.metanome.algorithm_integration.results.OrderDependency;
import de.metanome.algorithm_integration.results.OrderDependency.ComparisonOperator;
import de.metanome.algorithm_integration.results.OrderDependency.OrderType;

import java.util.ArrayList;

/**
 * @author Philipp Langer
 */
public class ExampleAlgorithm implements OrderDependencyAlgorithm, StringParameterAlgorithm {

  public static final String FILE_NAME = "pathToFile";

  protected String fileName = null;
  protected OrderDependencyResultReceiver resultReceiver;

  @Override
  public void execute() throws AlgorithmExecutionException {
    if (fileName != null) {
      try {
        resultReceiver.receiveResult(new OrderDependency(new ColumnPermutation(
            new ColumnIdentifier("WDC_planets.csv", "Name")), new ColumnPermutation(new ColumnIdentifier(
            "WDC_planets.csv", "Type")), OrderType.LEXICOGRAPHICAL, ComparisonOperator.SMALLER_EQUAL));

      } catch (final CouldNotReceiveResultException | ColumnNameMismatchException e) {
        e.printStackTrace();
        throw e;
      }
    }

  }

  @Override
  public ArrayList<ConfigurationRequirement<?>> getConfigurationRequirements() {
    final ArrayList<ConfigurationRequirement<?>> configurationRequirement = new ArrayList<>();

    configurationRequirement.add(new ConfigurationRequirementString(FILE_NAME));

    return configurationRequirement;
  }

  @Override
  public void setResultReceiver(final OrderDependencyResultReceiver resultReceiver) {
    this.resultReceiver = resultReceiver;
  }

  @Override
  public void setStringConfigurationValue(final String identifier, final String... values)
      throws AlgorithmConfigurationException {
    if (identifier.equals(FILE_NAME)) {
      fileName = values[0];
    } else {
      throw new AlgorithmConfigurationException("Incorrect identifier: " + identifier);
    }
  }

  @Override
  public String getAuthors() {
    return "Metanome Team";
  }

  @Override
  public String getDescription() {
    return "Example algorithm for order dependencies.";
  }
}
