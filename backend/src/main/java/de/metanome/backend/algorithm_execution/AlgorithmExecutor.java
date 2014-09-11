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
import de.metanome.algorithm_integration.algorithm_types.ProgressEstimatingAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.TempFileAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.metanome.backend.algorithm_loading.AlgorithmJarLoader;
import de.metanome.backend.algorithm_loading.AlgorithmLoadingException;
import de.metanome.backend.configuration.DefaultConfigurationFactory;
import de.metanome.backend.result_receiver.CloseableOmniscientResultReceiver;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.Execution;

import org.apache.commons.lang3.ClassUtils;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class AlgorithmExecutor implements Closeable {

  protected CloseableOmniscientResultReceiver resultReceiver;
  protected ProgressCache progressCache;

  protected FileGenerator fileGenerator;

  protected DefaultConfigurationFactory configurationFactory = new DefaultConfigurationFactory();

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
   * generators are set before execution. The elapsed time while executing the algorithm in nano
   * seconds is returned as long.
   *
   * @param algorithmFileName the algorithm's file name
   * @param parameters        list of configuration specifications
   * @return elapsed time in ns
   */
  public long executeAlgorithm(String algorithmFileName,
                               List<ConfigurationRequirement> parameters)
      throws AlgorithmLoadingException, AlgorithmExecutionException {

    List<ConfigurationValue> parameterValues = new LinkedList<>();

    for (ConfigurationRequirement requirement : parameters) {
      parameterValues.add(configurationFactory.build(requirement));
    }

    try {
      return executeAlgorithmWithValues(algorithmFileName, parameterValues);
    } catch (IllegalArgumentException | SecurityException | IllegalAccessException e) {
      throw new AlgorithmLoadingException();
    } catch (IOException e) {
      throw new AlgorithmLoadingException("IO Exception");
    } catch (ClassNotFoundException e) {
      throw new AlgorithmLoadingException("Class not found.");
    } catch (InstantiationException e) {
      throw new AlgorithmLoadingException("Could not instantiate.");
    } catch (InvocationTargetException e) {
      throw new AlgorithmLoadingException("Could not invoke.");
    } catch (NoSuchMethodException e) {
      throw new AlgorithmLoadingException("No such method.");
    } catch (EntityStorageException e) {
      throw new AlgorithmLoadingException("Algorithm not found in database.");
    }
  }

  /**
   * Executes an algorithm. The algorithm is loaded from the jar, configured and all receivers and
   * generators are set before execution. The elapsed time while executing the algorithm in nano
   * seconds is returned as long.
   *
   * @param algorithmFileName the algorithm's file name
   * @param parameters        list of configuration values
   * @return elapsed time in ns
   */
  public long executeAlgorithmWithValues(String algorithmFileName,
                                         List<ConfigurationValue> parameters)
      throws IllegalArgumentException, SecurityException, IOException, ClassNotFoundException,
             InstantiationException, IllegalAccessException, InvocationTargetException,
             NoSuchMethodException, AlgorithmExecutionException, EntityStorageException {
    AlgorithmJarLoader loader = new AlgorithmJarLoader();
    Algorithm algorithm;

    algorithm = loader.loadAlgorithm(algorithmFileName);

    Set<Class<?>> interfaces = getInterfaces(algorithm);

    for (ConfigurationValue configValue : parameters) {
      configValue.triggerSetValue(algorithm, interfaces);
    }

    if (interfaces.contains(FunctionalDependencyAlgorithm.class)) {
      FunctionalDependencyAlgorithm fdAlgorithm = (FunctionalDependencyAlgorithm) algorithm;
      fdAlgorithm.setResultReceiver(resultReceiver);
    }

    if (interfaces.contains(InclusionDependencyAlgorithm.class)) {
      InclusionDependencyAlgorithm indAlgorithm = (InclusionDependencyAlgorithm) algorithm;
      indAlgorithm.setResultReceiver(resultReceiver);
    }

    if (interfaces.contains(UniqueColumnCombinationsAlgorithm.class)) {
      UniqueColumnCombinationsAlgorithm
          uccAlgorithm =
          (UniqueColumnCombinationsAlgorithm) algorithm;
      uccAlgorithm.setResultReceiver(resultReceiver);
    }

    if (interfaces.contains(ConditionalUniqueColumnCombinationAlgorithm.class)) {
      ConditionalUniqueColumnCombinationAlgorithm
          cuccAlgorithm =
          (ConditionalUniqueColumnCombinationAlgorithm) algorithm;
      cuccAlgorithm.setResultReceiver(resultReceiver);
    }

    if (interfaces.contains(BasicStatisticsAlgorithm.class)) {
      BasicStatisticsAlgorithm basicStatAlgorithm = (BasicStatisticsAlgorithm) algorithm;
      basicStatAlgorithm.setResultReceiver(resultReceiver);
    }

    if (interfaces.contains(TempFileAlgorithm.class)) {
      TempFileAlgorithm tempFileAlgorithm = (TempFileAlgorithm) algorithm;
      tempFileAlgorithm.setTempFileGenerator(fileGenerator);
    }

    if (interfaces.contains(ProgressEstimatingAlgorithm.class)) {
      ProgressEstimatingAlgorithm
          progressEstimatingAlgorithm =
          (ProgressEstimatingAlgorithm) algorithm;
      progressEstimatingAlgorithm.setProgressReceiver(progressCache);
    }

    long beforeWallClockTime = new Date().getTime();
    long before = System.nanoTime();
    algorithm.execute();
    long after = System.nanoTime();
    long elapsedNanos = after - before;

    new Execution(de.metanome.backend.results_db.Algorithm.retrieve(algorithmFileName),
                  new Timestamp(beforeWallClockTime))
        .setEnd(new Timestamp(beforeWallClockTime + (elapsedNanos / 1000)))
        .store();

    return elapsedNanos;
  }

  protected Set<Class<?>> getInterfaces(Object object) {
    return new HashSet<>(ClassUtils.getAllInterfaces(object.getClass()));
  }

  @Override
  public void close() throws IOException {
    resultReceiver.close();
  }
}
