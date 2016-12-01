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
package de.metanome.algorithms.algorithm_template;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.RelationalInputParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.StringParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.TempFileAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;
import de.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

import java.util.ArrayList;

public class AlgorithmTemplate
    implements UniqueColumnCombinationsAlgorithm, FunctionalDependencyAlgorithm, TempFileAlgorithm,
               StringParameterAlgorithm, RelationalInputParameterAlgorithm {

  @Override
  public ArrayList<ConfigurationRequirement<?>> getConfigurationRequirements() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void execute() throws AlgorithmExecutionException {
    // TODO Auto-generated method stub

  }

  @Override
  public String getAuthors() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getDescription() {
    // TODO Auto-generated method stub
    return null;
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

  @Override
  public void setRelationalInputConfigurationValue(String identifier,
                                                   RelationalInputGenerator... values)
      throws AlgorithmConfigurationException {
    // TODO Auto-generated method stub
  }
}
