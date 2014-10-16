/*
 * Copyright 2014 by the Metanome project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package de.metanome.algorithms.testing.example_od_algorithm;

import java.util.ArrayList;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.algorithm_types.OrderDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.StringParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementString;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OrderDependencyResultReceiver;
import de.metanome.algorithm_integration.results.OrderDependency;
import de.metanome.algorithm_integration.results.OrderDependency.ComparisonOperator;
import de.metanome.algorithm_integration.results.OrderDependency.OrderType;

/**
 * @author Philipp Langer
 */
public class ExampleAlgorithm implements OrderDependencyAlgorithm, StringParameterAlgorithm {

  protected static final String FILE_NAME = "pathToFile";

  protected String fileName = null;
  protected OrderDependencyResultReceiver resultReceiver;

  @Override
  public void execute() {
    if (fileName != null) {
      System.out.println("Order Dependency Algorithm executing ...");
      try {
        resultReceiver.receiveResult(new OrderDependency(new ColumnPermutation(
            new ColumnIdentifier("table1", "column1")), new ColumnPermutation(new ColumnIdentifier(
                "table1", "column2")), OrderType.LEXICOGRAPHICAL, ComparisonOperator.SMALLER_EQUAL));

      } catch (final CouldNotReceiveResultException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

  }

  @Override
  public ArrayList<ConfigurationRequirement> getConfigurationRequirements() {
    final ArrayList<ConfigurationRequirement> configurationRequirement = new ArrayList<>();

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
}
