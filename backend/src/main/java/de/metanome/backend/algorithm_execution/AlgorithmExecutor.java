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
import de.metanome.algorithm_integration.input.FileInputGenerator;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.input.TableInputGenerator;
import de.metanome.algorithm_integration.results.JsonConverter;
import de.metanome.backend.algorithm_loading.AlgorithmAnalyzer;
import de.metanome.backend.configuration.ConfigurationValue;
import de.metanome.backend.configuration.DefaultConfigurationFactory;
import de.metanome.backend.helper.FileInputGeneratorMixIn;
import de.metanome.backend.helper.RelationalInputGeneratorMixIn;
import de.metanome.backend.helper.TableInputGeneratorMixIn;
import de.metanome.backend.resources.AlgorithmResource;
import de.metanome.backend.resources.ExecutionResource;
import de.metanome.backend.result_receiver.CloseableOmniscientResultReceiver;
import de.metanome.backend.result_receiver.ResultCache;
import de.metanome.backend.result_receiver.ResultCounter;
import de.metanome.backend.result_receiver.ResultPrinter;
import de.metanome.backend.result_receiver.ResultReceiver;
import de.metanome.backend.results_db.AlgorithmType;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.Execution;
import de.metanome.backend.results_db.ExecutionSetting;
import de.metanome.backend.results_db.HibernateUtil;
import de.metanome.backend.results_db.Input;
import de.metanome.backend.results_db.Result;
import de.metanome.backend.results_db.ResultType;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
   * Builds an {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor} with stacked {@link
   * de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver}s to write result
   * files and cache results for the frontend.
   *
   * @param executionSetting executionSetting
   * @return an {@link de.metanome.backend.algorithm_execution.AlgorithmExecutor}
   * @throws java.io.FileNotFoundException        when the result files cannot be opened
   * @throws java.io.UnsupportedEncodingException when the temp files cannot be opened
   */
  protected static AlgorithmExecutor buildExecutor(ExecutionSetting executionSetting)
      throws FileNotFoundException, UnsupportedEncodingException {
    FileGenerator fileGenerator = new TempFileGenerator();
    ProgressCache progressCache = new ProgressCache();
    String identifier = executionSetting.getExecutionIdentifier();

    ResultReceiver resultReceiver = null;
    if (executionSetting.getCacheResults()) {
      resultReceiver = new ResultCache(identifier);
    } else if (executionSetting.getCountResults()) {
      resultReceiver = new ResultCounter(identifier);
    } else {
      resultReceiver = new ResultPrinter(identifier);
    }
    AlgorithmExecutor
        executor =
        new AlgorithmExecutor(resultReceiver, progressCache, fileGenerator);
    executor.setResultPathPrefix(resultReceiver.getOutputFilePathPrefix());
    return executor;
  }

  public static List<ConfigurationValue> parseConfigurationValues(ExecutionSetting setting) {
    JsonConverter<ConfigurationValue> jsonConverter = new JsonConverter<ConfigurationValue>();
    jsonConverter.addMixIn(FileInputGenerator.class, FileInputGeneratorMixIn.class);
    jsonConverter.addMixIn(TableInputGenerator.class, TableInputGeneratorMixIn.class);
    jsonConverter.addMixIn(RelationalInputGenerator.class, RelationalInputGeneratorMixIn.class);
    List<ConfigurationValue> parameterValues = new ArrayList<ConfigurationValue>();
    for (String json : setting.getParameterValuesJson()) {
      try {
        parameterValues.add(jsonConverter.fromJsonString(json, ConfigurationValue.class));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return parameterValues;
  }

  public static List<Input> parseInputs(ExecutionSetting setting) {
    JsonConverter<Input> jsonConverterInput = new JsonConverter<Input>();
    List<Input> inputs = new ArrayList<Input>();
    for (String json : setting.getInputsJson()) {
      try {
        inputs.add(jsonConverterInput.fromJsonString(json, Input.class));
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
    return inputs;
  }

  public static void main(String args[])
      throws FileNotFoundException, UnsupportedEncodingException {

    Long algorithmId = Long.valueOf(args[0]);
    String executionIdentifier = args[1];
    boolean countResult = args[2].equals("true");
    AlgorithmResource algorithmResource = new AlgorithmResource();
    de.metanome.backend.results_db.Algorithm algorithm = algorithmResource.get(algorithmId);
    ExecutionSetting executionSetting = null;
    Session session = HibernateUtil.getSessionFactory().openSession();
    Criteria cr2 = session.createCriteria(ExecutionSetting.class);
    cr2.add(Restrictions.eq("executionIdentifier", executionIdentifier));
    executionSetting = (ExecutionSetting) cr2.list().get(0);
    List<ConfigurationValue> parameters = parseConfigurationValues(executionSetting);
    List<Input> inputs = parseInputs(executionSetting);
    session.close();
    Execution execution = null;
    AlgorithmExecutor executor = buildExecutor(executionSetting);
    try {
      execution =
          executor
              .executeAlgorithm(algorithm, parameters, inputs, executionIdentifier, countResult);
      execution.setExecutionSetting(executionSetting);
      ExecutionResource executionResource = new ExecutionResource();
      executionResource.store(execution);
      executor.close();
    } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException
        | InvocationTargetException | NoSuchMethodException | AlgorithmExecutionException
        | EntityStorageException e) {
      e.printStackTrace();
    }
    System.exit(0);
    //have method create ExecutionSetting and do parsing that is currently being done in execute algorithm
    //goto Hibernate find infos with id - execute found jar ... write results to file/db -> finish
  }

  /**
   * Executes an algorithm. The algorithm is loaded from the jar, configured and all receivers and
   * generators are set before execution. The execution containing the elapsed time while executing
   * the algorithm in nano seconds is returned.
   *
   * @param storedAlgorithm     the algorithm
   * @param parameters          parameters for algorithm execution
   * @param inputs              inputs for algorithm execution
   * @param executionIdentifier identifier for execution
   * @param countResult         true, if the results of the execution are just count results
   * @return the execution
   */

  public Execution executeAlgorithm(
      de.metanome.backend.results_db.Algorithm storedAlgorithm,
      List<ConfigurationValue> parameters,
      List<Input> inputs,
      String executionIdentifier,
      Boolean countResult)
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

    Execution execution = new Execution(storedAlgorithm, beforeWallClockTime)
        .setEnd(beforeWallClockTime + executionTimeInMs)
        .setInputs(inputs)
        .setIdentifier(executionIdentifier)
        .setResults(results)
        .setCountResult(countResult);

    for (Result result : results) {
      result.setExecution(execution);
    }
    return execution;
  }

  public void setResultPathPrefix(String prefix) {
    this.resultPathPrefix = prefix;
  }

  @Override
  public void close() throws IOException {
    resultReceiver.close();
  }
}
