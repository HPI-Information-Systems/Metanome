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

package de.uni_potsdam.hpi.metanome.algorithms.algorithm_template;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.StringParameterAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.TempFileAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

import java.util.List;

public class AlgorithmTemplate
    implements UniqueColumnCombinationsAlgorithm, FunctionalDependencyAlgorithm, TempFileAlgorithm,
               StringParameterAlgorithm {

  @Override
  public List<ConfigurationSpecification> getConfigurationRequirements() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void execute() throws AlgorithmExecutionException {
    // TODO Auto-generated method stub

  }

  @Override
  public void setStringConfigurationValue(String identifier, String... values)
      throws AlgorithmConfigurationException {
    // TODO Auto-generated method stub

  }

  @Override
  public void setTempFileGenerator(FileGenerator tempFileGenerator) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setResultReceiver(
      FunctionalDependencyResultReceiver resultReceiver) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setResultReceiver(
      UniqueColumnCombinationResultReceiver resultReceiver) {
    // TODO Auto-generated method stub

  }

}
