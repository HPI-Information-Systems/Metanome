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
import de.metanome.algorithm_integration.results.Result;
import de.metanome.backend.result_postprocessing.helper.TableInformation;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The results of the algorithm are analyzed. Different statistics and metrics are calculated to
 * allow sorting, ordering and filtering.
 */
public abstract class ResultAnalyzer<T extends Result, R> {

  protected boolean useDataIndependentStatistics = true;
  protected List<RelationalInputGenerator> inputGenerators = new ArrayList<>();
  protected Map<String, TableInformation> tableInformationMap;

  public ResultAnalyzer(List<RelationalInputGenerator> inputGenerators,
                        boolean useDataIndependentStatistics)
      throws InputGenerationException, InputIterationException {
    this.inputGenerators = inputGenerators;
    this.useDataIndependentStatistics = useDataIndependentStatistics;
    this.tableInformationMap = new HashMap<>();

    int index = 0;
    for (RelationalInputGenerator relationalInputGenerator : inputGenerators) {
      BitSet bitSet = new BitSet(inputGenerators.size());
      bitSet.set(index);
      TableInformation
          tableInformation =
          new TableInformation(relationalInputGenerator, useDataIndependentStatistics, bitSet);
      this.tableInformationMap.put(tableInformation.getTableName(), tableInformation);
    }
  }

  /**
   * Analyzes the results.
   *
   * @param results Results of the algorithm
   */
  public List<R> analyzeResults(List<T> results)
      throws InputGenerationException, InputIterationException {
    if (useDataIndependentStatistics) {
      return analyzeResultsDataIndependent(results);
    } else {
      return analyzeResultsDataDependent(results);
    }
  }

  /**
   * Analyzes the results without using the raw data from the inputs.
   *
   * @param prevResults Results of the algorithm
   */
  protected abstract List<R> analyzeResultsDataIndependent(List<T> prevResults);

  /**
   * Analyzes the results using the raw data from the inputs.
   *
   * @param prevResults Results of the algorithm
   */
  protected abstract List<R> analyzeResultsDataDependent(List<T> prevResults)
      throws InputGenerationException, InputIterationException;

  /**
   * Prints the results of postprocessing to file
   */
  public abstract void printResultsToFile();

  /**
   * Converts a list of results into a list of ranking results. The ranking results contain
   * additional information like different ranking values.
   *
   * @param prevResults the list of results
   * @return a list of ranking results
   */
  protected abstract List<R> convertResults(List<T> prevResults);
}
