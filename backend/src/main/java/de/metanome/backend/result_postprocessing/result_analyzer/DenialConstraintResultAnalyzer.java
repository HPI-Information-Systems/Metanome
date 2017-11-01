/**
 * Copyright 2017 by Metanome Project
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

import java.util.ArrayList;
import java.util.List;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.DenialConstraint;
import de.metanome.backend.result_postprocessing.results.DenialConstraintResult;

public class DenialConstraintResultAnalyzer
    extends ResultAnalyzer<DenialConstraint, DenialConstraintResult> {


  public DenialConstraintResultAnalyzer(List<RelationalInputGenerator> inputGenerators,
      boolean useDataIndependentStatistics)
      throws InputGenerationException, InputIterationException, AlgorithmConfigurationException {
    super(inputGenerators, useDataIndependentStatistics);
  }

  @Override
  protected List<DenialConstraintResult> analyzeResultsDataIndependent(
      List<DenialConstraint> prevResults) {
    List<DenialConstraintResult> results = convertResults(prevResults);
    return results;
  }

  @Override
  protected List<DenialConstraintResult> analyzeResultsDataDependent(
      List<DenialConstraint> prevResults) {
    List<DenialConstraintResult> results = convertResults(prevResults);
    return results;
  }

  @Override
  protected List<DenialConstraintResult> convertResults(List<DenialConstraint> prevResults) {
    List<DenialConstraintResult> results = new ArrayList<>();

    for (DenialConstraint prevResult : prevResults) {
      DenialConstraintResult result = new DenialConstraintResult(prevResult);

      results.add(result);
    }
    return results;
  }
}
