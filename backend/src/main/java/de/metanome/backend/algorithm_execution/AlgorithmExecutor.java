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

package de.metanome.backend.algorithm_execution;

import de.metanome.algorithm_integration.Algorithm;
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.metanome.algorithm_integration.algorithm_types.BasicStatisticsAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.ConditionalUniqueColumnCombinationAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.InclusionDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.OrderDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.ProgressEstimatingAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.TempFileAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationSetting;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput;
import de.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.metanome.backend.algorithm_loading.AlgorithmAnalyzer;
import de.metanome.backend.algorithm_loading.AlgorithmLoadingException;
import de.metanome.backend.configuration.DefaultConfigurationFactory;
import de.metanome.backend.helper.ExceptionParser;
import de.metanome.backend.resources.DatabaseConnectionResource;
import de.metanome.backend.resources.ExecutionResource;
import de.metanome.backend.resources.FileInputResource;
import de.metanome.backend.resources.TableInputResource;
import de.metanome.backend.result_postprocessing.result_analyzer.ResultsStoreHolder;
import de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies.FDResultAnalyzer;
import de.metanome.backend.result_postprocessing.result_analyzer.inclusion_dependencies.INDResultAnalyzer;
import de.metanome.backend.result_postprocessing.result_analyzer.ucc.UCCResultAnalyzer;
import de.metanome.backend.result_receiver.CloseableOmniscientResultReceiver;
import de.metanome.backend.results_db.AlgorithmType;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.Execution;
import de.metanome.backend.results_db.Input;
import de.metanome.backend.results_db.Result;
import de.metanome.backend.results_db.ResultType;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Executes given algorithms.
 */
public class AlgorithmExecutor implements Closeable {

  protected CloseableOmniscientResultReceiver resultReceiver;
  protected ProgressCache progressCache;

  protected FileGenerator fileGenerator;

  protected DefaultConfigurationFactory configurationFactory = new DefaultConfigurationFactory();

  protected String resultPathPrefix;

  /**
   * Constructs a new executor with new result receivers and generators.
   *
   * @param resultReceiver receives all of the algorithms results
   * @param fileGenerator  generates temp files
   */
  public AlgorithmExecutor(
      CloseableOmniscientResultReceiver resultReceiver,
      ProgressCache progressCache,
      FileGenerator fileGenerator) {
    this.resultReceiver = resultReceiver;
    this.progressCache = progressCache;
    this.fileGenerator = fileGenerator;
  }

  /**
   * Executes an algorithm. The algorithm is loaded from the jar, configured, by converting the
   * {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirement}s to {@link
   * de.metanome.algorithm_integration.configuration.ConfigurationValue}s and all receivers and
   * generators are set before execution. The execution containing the elapsed time while
   * executing the algorithm in nano seconds is returned.
   *
   * @param algorithm           the algorithm
   * @param requirements        list of configuration requirements
   * @param executionIdentifier the identifier for the execution
   * @return the execution
   */
  public Execution executeAlgorithm(de.metanome.backend.results_db.Algorithm algorithm,
                               List<ConfigurationRequirement> requirements,
                               String executionIdentifier)
      throws AlgorithmLoadingException, AlgorithmExecutionException {

    List<ConfigurationValue> parameterValues = new LinkedList<>();
    List<Input> inputs = new ArrayList<>();

    FileInputResource fileInputResource = new FileInputResource();
    TableInputResource tableInputResource = new TableInputResource();
    DatabaseConnectionResource databaseConnectionResource = new DatabaseConnectionResource();

    for (ConfigurationRequirement requirement : requirements) {
      parameterValues.add(requirement.build(configurationFactory));

      for (ConfigurationSetting setting : requirement.getSettings()) {
        if (setting instanceof ConfigurationSettingFileInput) {
          inputs.add(fileInputResource.get(((ConfigurationSettingFileInput) setting).getId()));
        } else if (setting instanceof ConfigurationSettingDatabaseConnection) {
          inputs.add(databaseConnectionResource
                         .get(((ConfigurationSettingDatabaseConnection) setting).getId()));
        } else if (setting instanceof ConfigurationSettingTableInput) {
          inputs.add(tableInputResource.get(((ConfigurationSettingTableInput) setting).getId()));
        }
      }
    }

    try {
      return executeAlgorithmWithValues(algorithm, parameterValues, inputs, executionIdentifier);
    } catch (IllegalArgumentException | SecurityException | IllegalAccessException | IOException |
        ClassNotFoundException | InstantiationException | InvocationTargetException |
        NoSuchMethodException e) {
      throw new AlgorithmLoadingException(ExceptionParser.parse(e), e);
    } catch (EntityStorageException e) {
      throw new AlgorithmLoadingException(
          ExceptionParser.parse(e, "Algorithm not found in database"), e);
    }
  }

  /**
   * Executes an algorithm. The algorithm is loaded from the jar, configured and all receivers and
   * generators are set before execution. The execution containing the elapsed time while
   * executing the algorithm in nano seconds is returned.
   *
   * @param storedAlgorithm         the algorithm
   * @param parameters              list of configuration values
   * @param executionIdentifier the identifier for the execution
   * @return the execution
   */
  public Execution executeAlgorithmWithValues(de.metanome.backend.results_db.Algorithm storedAlgorithm,
                                         List<ConfigurationValue> parameters,
                                         List<Input> inputs,
                                         String executionIdentifier)
      throws IllegalArgumentException, SecurityException, IOException, ClassNotFoundException,
             InstantiationException, IllegalAccessException, InvocationTargetException,
             NoSuchMethodException, AlgorithmExecutionException, EntityStorageException {

    AlgorithmAnalyzer analyzer = new AlgorithmAnalyzer(storedAlgorithm.getFileName());
    Algorithm algorithm = analyzer.getAlgorithm();

    Set<Result> results = new HashSet<>();

    for (ConfigurationValue configValue : parameters) {
      configValue.triggerSetValue(algorithm, analyzer.getInterfaces());
    }

    //Todo: for AlgorithmType type : AlgorithmType.values() ...

    if (analyzer.hasType(AlgorithmType.FD)) {
      FunctionalDependencyAlgorithm fdAlgorithm = (FunctionalDependencyAlgorithm) algorithm;
      fdAlgorithm.setResultReceiver(resultReceiver);

      results.add(new Result(resultPathPrefix, ResultType.FD));
    }

    if (analyzer.hasType(AlgorithmType.IND)) {
      InclusionDependencyAlgorithm indAlgorithm = (InclusionDependencyAlgorithm) algorithm;
      indAlgorithm.setResultReceiver(resultReceiver);

      results.add(new Result(resultPathPrefix, ResultType.IND));
    }

    if (analyzer.hasType(AlgorithmType.UCC)) {
      UniqueColumnCombinationsAlgorithm
          uccAlgorithm =
          (UniqueColumnCombinationsAlgorithm) algorithm;
      uccAlgorithm.setResultReceiver(resultReceiver);

      results.add(new Result(resultPathPrefix, ResultType.UCC));
    }

    if (analyzer.hasType(AlgorithmType.CUCC)) {
      ConditionalUniqueColumnCombinationAlgorithm
          cuccAlgorithm =
          (ConditionalUniqueColumnCombinationAlgorithm) algorithm;
      cuccAlgorithm.setResultReceiver(resultReceiver);

      results.add(new Result(resultPathPrefix, ResultType.CUCC));
    }

    if (analyzer.hasType(AlgorithmType.OD)) {
      OrderDependencyAlgorithm odAlgorithm = (OrderDependencyAlgorithm) algorithm;
      odAlgorithm.setResultReceiver(resultReceiver);

      results.add(new Result(resultPathPrefix, ResultType.OD));
    }

    if (analyzer.hasType(AlgorithmType.BASIC_STAT)) {
      BasicStatisticsAlgorithm basicStatAlgorithm = (BasicStatisticsAlgorithm) algorithm;
      basicStatAlgorithm.setResultReceiver(resultReceiver);

      results.add(new Result(resultPathPrefix, ResultType.STAT));
    }

    if (analyzer.hasType(AlgorithmType.TEMP_FILE)) {
      TempFileAlgorithm tempFileAlgorithm = (TempFileAlgorithm) algorithm;
      tempFileAlgorithm.setTempFileGenerator(fileGenerator);
    }

    if (analyzer.hasType(AlgorithmType.PROGRESS_EST)) {
      ProgressEstimatingAlgorithm
          progressEstimatingAlgorithm =
          (ProgressEstimatingAlgorithm) algorithm;
      progressEstimatingAlgorithm.setProgressReceiver(progressCache);
    }

    long beforeWallClockTime = new Date().getTime(); // milliseconds
    long before = System.nanoTime(); // nanoseconds
    algorithm.execute();
    long after = System.nanoTime(); // nanoseconds
    long executionTimeInNanos = after - before;
    long executionTimeInMs = executionTimeInNanos / 1000000; // milliseconds

    ExecutionResource executionResource = new ExecutionResource();
    Execution execution = new Execution(storedAlgorithm, beforeWallClockTime)
        .setEnd(beforeWallClockTime + executionTimeInMs)
        .setInputs(inputs)
        .setIdentifier(executionIdentifier)
        .setResults(results);

    for (Result result : results) {
      result.setExecution(execution);
    }

    executionResource.store(execution);

    return execution;
  }

  public void setResultPathPrefix(String prefix) {
    this.resultPathPrefix = prefix;
  }

  @Override
  public void close() throws IOException {
    resultReceiver.close();
  }

  public void executeResultPostProcessing(Execution execution){
    // Result post-processing
    // Clear the results stores
    ResultsStoreHolder.clearStores();
    // Analyze the results without data usage
    if(execution.getAlgorithm().isFd()){
      (new FDResultAnalyzer()).analyzeResults(execution, false);
    }
    if(execution.getAlgorithm().isUcc()){
      (new UCCResultAnalyzer()).analyzeResults(execution, false);
    }
    if(execution.getAlgorithm().isInd()){
      (new INDResultAnalyzer()).analyzeResults(execution, false);
    }
  }
}
