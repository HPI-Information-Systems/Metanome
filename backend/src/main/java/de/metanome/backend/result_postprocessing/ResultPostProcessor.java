/**
 * Copyright 2015-2017 by Metanome Project
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
import de.metanome.algorithm_integration.results.*;
import de.metanome.backend.constants.Constants;
import de.metanome.backend.helper.InputToGeneratorConverter;
import de.metanome.backend.input.file.DefaultFileInputGenerator;
import de.metanome.backend.result_postprocessing.result_analyzer.*;
import de.metanome.backend.result_postprocessing.result_store.*;
import de.metanome.backend.result_postprocessing.results.*;
import de.metanome.backend.result_receiver.ResultReader;
import de.metanome.backend.results_db.*;
import de.metanome.backend.results_db.Result;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Starting point for the result post processing. The results are extracted from disk, analyzed and
 * hold in memory for further analyses.
 */
public class ResultPostProcessor {

  /**
   * Loads the results of an algorithm run from hard disk, analyze them without using the actual
   * data and stores them.
   *
   * @param execution Execution containing the algorithm results file path
   * @throws java.io.IOException if the result file could not be loaded
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if the inputs could not be converted to values
   * @throws de.metanome.algorithm_integration.input.InputGenerationException if no input generator could be created
   * @throws de.metanome.algorithm_integration.input.InputIterationException if the file could not be iterated
   */
  public static void extractAndStoreResultsDataIndependent(Execution execution)
    throws IOException, AlgorithmConfigurationException, InputGenerationException,
    InputIterationException, NullPointerException, IndexOutOfBoundsException {
    extractAndStoreResults(execution.getResults(), execution.getInputs(), true);
  }

  /**
   * Loads the results of an algorithm run from hard disk, analyze them using the actual data and
   * stores them.
   *
   * @param execution Execution containing the algorithm results file path
   * @throws java.io.IOException if the result file could not be loaded
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if the inputs could not be converted to values
   * @throws de.metanome.algorithm_integration.input.InputGenerationException if no input generator could be created
   * @throws de.metanome.algorithm_integration.input.InputIterationException if the file could not be iterated
   */
  public static void extractAndStoreResultsDataDependent(Execution execution)
    throws IOException, AlgorithmConfigurationException, InputGenerationException,
    InputIterationException, NullPointerException, IndexOutOfBoundsException {
    extractAndStoreResults(execution.getResults(), execution.getInputs(), false);
  }

  /**
   * Loads the results of an algorithm run from hard disk, analyzes them without using the actual
   * data and stores them.
   *
   * @param results the results
   * @param inputs  the inputs used by the algorithm
   * @throws java.io.IOException if the result file could not be loaded
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if the inputs could not be converted to values
   * @throws de.metanome.algorithm_integration.input.InputGenerationException if no input generator could be created
   * @throws de.metanome.algorithm_integration.input.InputIterationException if the file could not be iterated
   */
  public static void extractAndStoreResultsDataIndependent(Set<Result> results,
                                                           Collection<Input> inputs)
    throws AlgorithmConfigurationException, InputGenerationException, InputIterationException,
    IOException, NullPointerException, IndexOutOfBoundsException {
    extractAndStoreResults(results, inputs, true);
  }

  /**
   * Loads the results of an algorithm run from hard disk, analyzes them using the actual data and
   * stores them.
   *
   * @param results the results
   * @param inputs  the inputs used by the algorithm
   * @throws java.io.IOException if the result file could not be loaded
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if the inputs could not be converted to values
   * @throws de.metanome.algorithm_integration.input.InputGenerationException if no input generator could be created
   * @throws de.metanome.algorithm_integration.input.InputIterationException if the file could not be iterated
   */
  public static void extractAndStoreResultsDataDependent(Set<Result> results,
                                                         Collection<Input> inputs)
    throws AlgorithmConfigurationException, InputGenerationException, InputIterationException,
    IOException, NullPointerException, IndexOutOfBoundsException {
    extractAndStoreResults(results, inputs, false);
  }


  /**
   * Loads the results of an algorithm run from hard disk, analyzes and stores them.
   *
   * @param results         the results
   * @param inputs          the inputs used by the algorithm
   * @param dataIndependent true, if the result analyzes should use the actual data, false
   *                        otherwise
   * @throws java.io.IOException if the result file could not be loaded
   * @throws de.metanome.algorithm_integration.AlgorithmConfigurationException if the inputs could not be converted to values
   * @throws de.metanome.algorithm_integration.input.InputGenerationException if no input generator could be created
   * @throws de.metanome.algorithm_integration.input.InputIterationException if the file could not be iterated
   */
  protected static void extractAndStoreResults(Set<Result> results, Collection<Input> inputs,
                                               boolean dataIndependent)
    throws IOException, AlgorithmConfigurationException, InputGenerationException,
    InputIterationException, NullPointerException, IndexOutOfBoundsException {
    ResultsStoreHolder.clearStores();

    // get input generators
    List<RelationalInputGenerator> inputGenerators = new ArrayList<>();
    for (Input input : inputs) {
      if (input instanceof FileInput) {
        File currFile = new File(input.getName());
        if (currFile.isFile()) {
          inputGenerators.add(InputToGeneratorConverter.convertInput(input));
        } else if (currFile.isDirectory()) {
          File[] filesInDirectory = currFile.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String name) {
              for (String fileEnding : Constants.ACCEPTED_FILE_ENDINGS_ARRAY) {
                if (name.endsWith(fileEnding)) {
                  return true;
                }
              }
              return false;
            }
          });
          for (File file : filesInDirectory) {
            inputGenerators.add(new DefaultFileInputGenerator(file, InputToGeneratorConverter.convertInputToSetting((FileInput) input)));
          }
        }
      } else {
        RelationalInputGenerator relInpGen = InputToGeneratorConverter.convertInput(input);
        if (relInpGen != null) {
          inputGenerators.add(relInpGen);
        }
      }
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
   * @param dataIndependent true, if the result analyzes should use the actual data, false
   *                        otherwise
   * @throws java.io.IOException if the result file could not be loaded
   * @throws de.metanome.algorithm_integration.input.InputGenerationException if no input generator could be created
   * @throws de.metanome.algorithm_integration.input.InputIterationException if the file could not be iterated
   */
  private static void analyzeAndStoreResults(String fileName, String name,
                                             List<RelationalInputGenerator> inputGenerators,
                                             boolean dataIndependent)
    throws IOException, InputGenerationException, InputIterationException, AlgorithmConfigurationException,
    NullPointerException, IndexOutOfBoundsException {

    if (name.equals(ResultType.CUCC.getName())) {
      // read results
      ResultReader<ConditionalUniqueColumnCombination> resultReader =
        new ResultReader<>(ResultType.CUCC);
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
        new ResultReader<>(ResultType.OD);
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
        new ResultReader<>(ResultType.IND);
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
        new ResultReader<>(ResultType.FD);
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
      
 
    } else if (name.equals(ResultType.CID.getName())) {
      // read results
      ResultReader<ConditionalInclusionDependency> resultReader =
        new ResultReader<>(ResultType.CID);
      List<ConditionalInclusionDependency>
        conditionalInclusionDependencies =
        resultReader.readResultsFromFile(fileName);
      // analyze results
      ResultAnalyzer<ConditionalInclusionDependency, ConditionalInclusionDependencyResult>
        resultAnalyzer =
        new ConditionalInclusionDependencyResultAnalyzer(inputGenerators, dataIndependent);
      List<ConditionalInclusionDependencyResult>
        rankingResults =
        resultAnalyzer.analyzeResults(conditionalInclusionDependencies);
      // store results
      ConditionalInclusionDependencyResultStore resultsStore = new ConditionalInclusionDependencyResultStore();
      resultsStore.store(rankingResults);
      ResultsStoreHolder.register(name, resultsStore);
 
      
    } else if (name.equals(ResultType.MD.getName())) {
      // read results
      ResultReader<MatchingDependency> resultReader =
          new ResultReader<>(ResultType.MD);
      List<MatchingDependency>
          matchingDependencies =
          resultReader.readResultsFromFile(fileName);
      // analyze results
      ResultAnalyzer<MatchingDependency, MatchingDependencyResult>
          resultAnalyzer =
          new MatchingDependencyResultAnalyzer(inputGenerators, dataIndependent);
      List<MatchingDependencyResult>
          rankingResults =
          resultAnalyzer.analyzeResults(matchingDependencies);
      // store results
      MatchingDependencyResultStore resultsStore = new MatchingDependencyResultStore();
      resultsStore.store(rankingResults);
      ResultsStoreHolder.register(name, resultsStore);

    } else if (name.equals(ResultType.CFD.getName())) {
      // read results
      ResultReader<ConditionalFunctionalDependency> resultReader =
              new ResultReader<>(ResultType.CFD);
      List<ConditionalFunctionalDependency>
              conditionalFunctionalDependencies =
              resultReader.readResultsFromFile(fileName);
      // analyze results
      ResultAnalyzer<ConditionalFunctionalDependency, ConditionalFunctionalDependencyResult>
              resultAnalyzer =
              new ConditionalFunctionalDependencyResultAnalyzer(inputGenerators, dataIndependent);
      List<ConditionalFunctionalDependencyResult>
              rankingResults =
              resultAnalyzer.analyzeResults(conditionalFunctionalDependencies);
      // store results
      ConditionalFunctionalDependencyResultStore resultsStore = new ConditionalFunctionalDependencyResultStore();
      resultsStore.store(rankingResults);
      ResultsStoreHolder.register(name, resultsStore);

    } else if (name.equals(ResultType.RFD.getName())) {
      // read results
      ResultReader<RelaxedFunctionalDependency> resultReader =
              new ResultReader<>(ResultType.RFD);
      List<RelaxedFunctionalDependency>
              relaxedFunctionalDependencies =
              resultReader.readResultsFromFile(fileName);
      // analyze results
      ResultAnalyzer<RelaxedFunctionalDependency, RelaxedFunctionalDependencyResult>
              resultAnalyzer =
              new RelaxedFunctionalDependencyResultAnalyzer(inputGenerators, dataIndependent);
      List<RelaxedFunctionalDependencyResult>
              rankingResults =
              resultAnalyzer.analyzeResults(relaxedFunctionalDependencies);
      // store results
      RelaxedFunctionalDependencyResultStore resultsStore = new RelaxedFunctionalDependencyResultStore();
      resultsStore.store(rankingResults);
      ResultsStoreHolder.register(name, resultsStore);

    } else if (name.equals(ResultType.RIND.getName())) {
      // read results
      ResultReader<RelaxedInclusionDependency> resultReader =
              new ResultReader<>(ResultType.RIND);
      List<RelaxedInclusionDependency>
              relaxedInclusionDependencies =
              resultReader.readResultsFromFile(fileName);
      // analyze results
      ResultAnalyzer<RelaxedInclusionDependency, RelaxedInclusionDependencyResult>
              resultAnalyzer =
              new RelaxedInclusionDependencyResultAnalyzer(inputGenerators, dataIndependent);
      List<RelaxedInclusionDependencyResult>
              rankingResults =
              resultAnalyzer.analyzeResults(relaxedInclusionDependencies);
      // store results
      RelaxedInclusionDependencyResultStore resultsStore = new RelaxedInclusionDependencyResultStore();
      resultsStore.store(rankingResults);
      ResultsStoreHolder.register(name, resultsStore);

    } else if (name.equals(ResultType.RUCC.getName())) {
      // read results
      ResultReader<RelaxedUniqueColumnCombination> resultReader =
              new ResultReader<>(ResultType.RUCC);
      List<RelaxedUniqueColumnCombination>
              relaxedUniqueColumnCombinations =
              resultReader.readResultsFromFile(fileName);
      // analyze results
      ResultAnalyzer<RelaxedUniqueColumnCombination, RelaxedUniqueColumnCombinationResult>
              resultAnalyzer =
              new RelaxedUniqueColumnCombinationResultAnalyzer(inputGenerators, dataIndependent);
      List<RelaxedUniqueColumnCombinationResult>
              rankingResults =
              resultAnalyzer.analyzeResults(relaxedUniqueColumnCombinations);
      // store results
      RelaxedUniqueColumnCombinationResultStore
              resultsStore =
              new RelaxedUniqueColumnCombinationResultStore();
      resultsStore.store(rankingResults);
      ResultsStoreHolder.register(name, resultsStore);

    } else if (name.equals(ResultType.MVD.getName())) {
      // read results
      ResultReader<MultivaluedDependency> resultReader =
        new ResultReader<>(ResultType.MVD);
      List<MultivaluedDependency>
        multivaluedDependencies =
        resultReader.readResultsFromFile(fileName);
      // analyze results
      ResultAnalyzer<MultivaluedDependency, MultivaluedDependencyResult>
        resultAnalyzer =
        new MultivaluedDependencyResultAnalyzer(inputGenerators, dataIndependent);
      List<MultivaluedDependencyResult>
        rankingResults =
        resultAnalyzer.analyzeResults(multivaluedDependencies);
      // store results
      MultivaluedDependencyResultStore resultsStore = new MultivaluedDependencyResultStore();
      resultsStore.store(rankingResults);
      ResultsStoreHolder.register(name, resultsStore);

    } else if (name.equals(ResultType.UCC.getName())) {
      // read results
      ResultReader<UniqueColumnCombination> resultReader =
        new ResultReader<>(ResultType.UCC);
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


    } else if (name.equals(ResultType.BASIC_STAT.getName())) {
      // read results
      ResultReader<BasicStatistic> resultReader =
        new ResultReader<>(ResultType.BASIC_STAT);
      List<BasicStatistic> basicStatistics = resultReader.readResultsFromFile(fileName);
      // analyze results
      ResultAnalyzer<BasicStatistic, BasicStatisticResult>
        resultAnalyzer = new BasicStatisticResultAnalyzer(inputGenerators, dataIndependent);
      List<BasicStatisticResult> rankingResults = resultAnalyzer.analyzeResults(basicStatistics);
      // store results
      BasicStatisticResultStore resultsStore = new BasicStatisticResultStore();
      resultsStore.store(rankingResults);
      ResultsStoreHolder.register(name, resultsStore);
      
    } else if (name.equals(ResultType.DC.getName())) {
      // read results
      ResultReader<DenialConstraint> resultReader =
        new ResultReader<>(ResultType.DC);
      List<DenialConstraint> denialConstraints = resultReader.readResultsFromFile(fileName);
      // analyze results
      ResultAnalyzer<DenialConstraint, DenialConstraintResult>
        resultAnalyzer = new DenialConstraintResultAnalyzer(inputGenerators, dataIndependent);
      List<DenialConstraintResult> rankingResults = resultAnalyzer.analyzeResults(denialConstraints);
      // store results
      DenialConstraintResultStore resultsStore = new DenialConstraintResultStore();
      resultsStore.store(rankingResults);
      ResultsStoreHolder.register(name, resultsStore);
    }
  }
}
