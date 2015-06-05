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

package de.metanome.backend.result_postprocessing;


import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.results.BasicStatistic;
import de.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination;
import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.algorithm_integration.results.InclusionDependency;
import de.metanome.algorithm_integration.results.OrderDependency;
import de.metanome.algorithm_integration.results.UniqueColumnCombination;
import de.metanome.backend.result_postprocessing.helper.InputToGeneratorConverter;
import de.metanome.backend.result_postprocessing.result_analyzer.BasicStatisticResultAnalyzer;
import de.metanome.backend.result_postprocessing.result_analyzer.ConditionalUniqueColumnCombinationResultAnalyzer;
import de.metanome.backend.result_postprocessing.result_analyzer.FunctionalDependencyResultAnalyzer;
import de.metanome.backend.result_postprocessing.result_analyzer.InclusionDependencyResultAnalyzer;
import de.metanome.backend.result_postprocessing.result_analyzer.OrderDependencyResultAnalyzer;
import de.metanome.backend.result_postprocessing.result_analyzer.ResultAnalyzer;
import de.metanome.backend.result_postprocessing.result_analyzer.UniqueColumnCombinationResultAnalyzer;
import de.metanome.backend.result_postprocessing.result_store.BasicStatisticResultStore;
import de.metanome.backend.result_postprocessing.result_store.ConditionalUniqueColumnCombinationResultStore;
import de.metanome.backend.result_postprocessing.result_store.FunctionalDependencyResultStore;
import de.metanome.backend.result_postprocessing.result_store.InclusionDependencyResultsStore;
import de.metanome.backend.result_postprocessing.result_store.OrderDependencyResultStore;
import de.metanome.backend.result_postprocessing.result_store.ResultsStoreHolder;
import de.metanome.backend.result_postprocessing.result_store.UniqueColumnCombinationResultStore;
import de.metanome.backend.result_postprocessing.results.BasicStatisticResult;
import de.metanome.backend.result_postprocessing.results.ConditionalUniqueColumnCombinationResult;
import de.metanome.backend.result_postprocessing.results.FunctionalDependencyResult;
import de.metanome.backend.result_postprocessing.results.InclusionDependencyResult;
import de.metanome.backend.result_postprocessing.results.OrderDependencyResult;
import de.metanome.backend.result_postprocessing.results.UniqueColumnCombinationResult;
import de.metanome.backend.result_receiver.ResultReader;
import de.metanome.backend.results_db.Execution;
import de.metanome.backend.results_db.Input;
import de.metanome.backend.results_db.Result;
import de.metanome.backend.results_db.ResultType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Starting point for the result post processing. The results are extracted from disk, analyzed
 * and hold in memory for further analyses.
 */
public class ResultPostProcessor {

  /**
   * Loads the results of an algorithm run from hard disk, analyze them without using
   * the actual data and stores them.
   *
   * @param execution Execution containing the algorithm results file path
   */
  public static void extractAndStoreResultsDataIndependent(Execution execution)
      throws IOException, AlgorithmConfigurationException, InputGenerationException,
             InputIterationException {
    extractAndStoreResults(execution.getResults(), execution.getInputs(), true);
  }

  /**
   * Loads the results of an algorithm run from hard disk, analyze them using
   * the actual data and stores them.
   *
   * @param execution Execution containing the algorithm results file path
   */
  public static void extractAndStoreResultsDataDependent(Execution execution)
      throws IOException, AlgorithmConfigurationException, InputGenerationException,
             InputIterationException {
    extractAndStoreResults(execution.getResults(), execution.getInputs(), false);
  }

  /**
   * Loads the results of an algorithm run from hard disk, analyzes them without
   * using the actual data and stores them.
   *
   * @param results         the results
   * @param inputs          the inputs used by the algorithm
   */
  public static void extractAndStoreResultsDataIndependent(Set<Result> results, Collection<Input> inputs)
      throws AlgorithmConfigurationException, InputGenerationException, InputIterationException,
             IOException {
    extractAndStoreResults(results, inputs, true);
  }

  /**
   * Loads the results of an algorithm run from hard disk, analyzes them
   * using the actual data and stores them.
   *
   * @param results         the results
   * @param inputs          the inputs used by the algorithm
   */
  public static void extractAndStoreResultsDataDependent(Set<Result> results, Collection<Input> inputs)
      throws AlgorithmConfigurationException, InputGenerationException, InputIterationException,
             IOException {
    extractAndStoreResults(results, inputs, false);
  }



  /**
   * Loads the results of an algorithm run from hard disk, analyzes and stores them.
   *
   * @param results         the results
   * @param inputs          the inputs used by the algorithm
   * @param dataIndependent true, if the result analyzes should use the actual data, false otherwise
   */
  protected static void extractAndStoreResults(Set<Result> results, Collection<Input> inputs, boolean dataIndependent)
      throws IOException, AlgorithmConfigurationException, InputGenerationException,
             InputIterationException {
    ResultsStoreHolder.clearStores();

    // get input generators
    List<RelationalInputGenerator> inputGenerators = new ArrayList<>();
    for (Input input : inputs) {
      inputGenerators.add(InputToGeneratorConverter.convertInput(input));
    }

    // check if a database connection was used
    // if this is true, we can not compute ranking results based on column count etc.,
    // because we do not know which specific column was used for profiling
    boolean usedDatabaseConnection = inputGenerators.contains(null);
    inputGenerators =
        usedDatabaseConnection ? new ArrayList<RelationalInputGenerator>() : inputGenerators;

    for (de.metanome.backend.results_db.Result result : results) {
      String fileName = result.getFileName();
      String resultTypeName = result.getType().getName();

      analyzeAndStoreResults(fileName, resultTypeName, inputGenerators, dataIndependent);
    }
  }

  /**
   * Reads the results from the given file, analyzes them and stores them in a result store.
   *
   * @param fileName        the file name
   * @param name            the name of the result type
   * @param dataIndependent true, if the result analyzes should use the actual data, false otherwise
   */
  private static void analyzeAndStoreResults(String fileName, String name,
                                             List<RelationalInputGenerator> inputGenerators,
                                             boolean dataIndependent)
      throws IOException, InputGenerationException, InputIterationException {

    if (name.equals(ResultType.CUCC.getName())) {
      // read results
      ResultReader<ConditionalUniqueColumnCombination> resultReader =
          new ResultReader<>(ConditionalUniqueColumnCombination.class);
      List<ConditionalUniqueColumnCombination>
          conditionalUniqueColumnCombinations =
          resultReader.readResultsFromFile(fileName);
      // analyze results
      ResultAnalyzer<ConditionalUniqueColumnCombination, ConditionalUniqueColumnCombinationResult>
          resultAnalyzer =
          new ConditionalUniqueColumnCombinationResultAnalyzer(inputGenerators, dataIndependent);
      List<ConditionalUniqueColumnCombinationResult>
          rankingResults =
          resultAnalyzer.analyzeResults(conditionalUniqueColumnCombinations);
      // store results
      ConditionalUniqueColumnCombinationResultStore
          resultsStore =
          new ConditionalUniqueColumnCombinationResultStore();
      resultsStore.store(rankingResults);
      ResultsStoreHolder.register(name, resultsStore);


    } else if (name.equals(ResultType.OD.getName())) {
      // read results
      ResultReader<OrderDependency> resultReader =
          new ResultReader<>(OrderDependency.class);
      List<OrderDependency> orderDependencies = resultReader.readResultsFromFile(fileName);
      // analyze results
      ResultAnalyzer<OrderDependency, OrderDependencyResult>
          resultAnalyzer = new OrderDependencyResultAnalyzer(inputGenerators, dataIndependent);
      List<OrderDependencyResult> rankingResults = resultAnalyzer.analyzeResults(orderDependencies);
      // store results
      OrderDependencyResultStore resultsStore = new OrderDependencyResultStore();
      resultsStore.store(rankingResults);
      ResultsStoreHolder.register(name, resultsStore);


    } else if (name.equals(ResultType.IND.getName())) {
      // read results
      ResultReader<InclusionDependency> resultReader =
          new ResultReader<>(InclusionDependency.class);
      List<InclusionDependency> inclusionDependencies = resultReader.readResultsFromFile(fileName);
      // analyze results
      ResultAnalyzer<InclusionDependency, InclusionDependencyResult>
          resultAnalyzer = new InclusionDependencyResultAnalyzer(inputGenerators, dataIndependent);
      List<InclusionDependencyResult>
          rankingResults =
          resultAnalyzer.analyzeResults(inclusionDependencies);
      // store results
      InclusionDependencyResultsStore resultsStore = new InclusionDependencyResultsStore();
      resultsStore.store(rankingResults);
      ResultsStoreHolder.register(name, resultsStore);


    } else if (name.equals(ResultType.FD.getName())) {
      // read results
      ResultReader<FunctionalDependency> resultReader =
          new ResultReader<>(FunctionalDependency.class);
      List<FunctionalDependency>
          functionalDependencies =
          resultReader.readResultsFromFile(fileName);
      // analyze results
      ResultAnalyzer<FunctionalDependency, FunctionalDependencyResult>
          resultAnalyzer =
          new FunctionalDependencyResultAnalyzer(inputGenerators, dataIndependent);
      List<FunctionalDependencyResult>
          rankingResults =
          resultAnalyzer.analyzeResults(functionalDependencies);
      // store results
      FunctionalDependencyResultStore resultsStore = new FunctionalDependencyResultStore();
      resultsStore.store(rankingResults);
      ResultsStoreHolder.register(name, resultsStore);


    } else if (name.equals(ResultType.UCC.getName())) {
      // read results
      ResultReader<UniqueColumnCombination> resultReader =
          new ResultReader<>(UniqueColumnCombination.class);
      List<UniqueColumnCombination>
          uniqueColumnCombinations =
          resultReader.readResultsFromFile(fileName);
      // analyze results
      ResultAnalyzer<UniqueColumnCombination, UniqueColumnCombinationResult>
          resultAnalyzer =
          new UniqueColumnCombinationResultAnalyzer(inputGenerators, dataIndependent);
      List<UniqueColumnCombinationResult>
          rankingResult =
          resultAnalyzer.analyzeResults(uniqueColumnCombinations);
      // store results
      UniqueColumnCombinationResultStore resultsStore = new UniqueColumnCombinationResultStore();
      resultsStore.store(rankingResult);
      ResultsStoreHolder.register(name, resultsStore);


    } else if (name.equals(ResultType.STAT.getName())) {
      // read results
      ResultReader<BasicStatistic> resultReader =
          new ResultReader<>(BasicStatistic.class);
      List<BasicStatistic> basicStatistics = resultReader.readResultsFromFile(fileName);
      // analyze results
      ResultAnalyzer<BasicStatistic, BasicStatisticResult>
          resultAnalyzer = new BasicStatisticResultAnalyzer(inputGenerators, dataIndependent);
      List<BasicStatisticResult> rankingResults = resultAnalyzer.analyzeResults(basicStatistics);
      // store results
      BasicStatisticResultStore resultsStore = new BasicStatisticResultStore();
      resultsStore.store(rankingResults);
      ResultsStoreHolder.register(name, resultsStore);
    }
  }
}
