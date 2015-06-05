/*
 * Copyright 2015 by the Metanome project
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

package de.metanome.backend.result_postprocessing.result_analyzer;

import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.OrderDependency;
import de.metanome.backend.result_postprocessing.results.OrderDependencyResult;

import java.util.List;

/**
 * Analyzes Order Dependency Results.
 */
public class OrderDependencyResultAnalyzer
    extends ResultAnalyzer<OrderDependency, OrderDependencyResult> {

  public OrderDependencyResultAnalyzer(
      List<RelationalInputGenerator> inputGenerators,
      boolean useDataDependentStatistics) throws InputGenerationException, InputIterationException {
    super(inputGenerators, useDataDependentStatistics);
  }

  @Override
  protected List<OrderDependencyResult> analyzeResultsDataIndependent(
      List<OrderDependency> results) {
    return null;
  }

  @Override
  protected List<OrderDependencyResult> analyzeResultsDataDependent(List<OrderDependency> results) {
    return null;
  }

  @Override
  public void printResultsToFile() {

  }


}
