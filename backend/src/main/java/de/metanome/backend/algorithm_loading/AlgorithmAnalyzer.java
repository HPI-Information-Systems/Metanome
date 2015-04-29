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

package de.metanome.backend.algorithm_loading;


import de.metanome.algorithm_integration.Algorithm;
import de.metanome.algorithm_integration.algorithm_types.BasicStatisticsAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.ConditionalUniqueColumnCombinationAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.DatabaseConnectionParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.FileInputParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.InclusionDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.OrderDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.ProgressEstimatingAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.RelationalInputParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.TableInputParameterAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.TempFileAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.metanome.backend.results_db.AlgorithmType;

import org.apache.commons.lang3.ClassUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

/**
 * Loads the algorithm and checks which algorithm types are supported.
 */
public class AlgorithmAnalyzer {

  private Algorithm algorithm;

  private HashSet<AlgorithmType> types;

  Set<Class<?>> interfaces;

  /**
   * @param algorithmPath the algorithm file, which should be analyzed.
   * @throws IllegalAccessException    if loading fails
   * @throws IOException               if loading fails
   * @throws InstantiationException    if loading fails
   * @throws NoSuchMethodException     if loading fails
   * @throws InvocationTargetException if loading fails
   * @throws ClassNotFoundException    if loading fails
   */
  public AlgorithmAnalyzer(String algorithmPath)
      throws IllegalAccessException, IOException, InstantiationException, NoSuchMethodException,
             InvocationTargetException, ClassNotFoundException {
    AlgorithmJarLoader loader = new AlgorithmJarLoader();

    this.algorithm = loader.loadAlgorithm(algorithmPath);

    this.types = new HashSet<>();

    analyzerInterfaces();
  }

  private void analyzerInterfaces() {
    this.interfaces = extractInterfaces(algorithm);

    if (interfaces.contains(FunctionalDependencyAlgorithm.class)) {
      types.add(AlgorithmType.FD);
    }
    if (interfaces.contains(InclusionDependencyAlgorithm.class)) {
      types.add(AlgorithmType.IND);
    }
    if (interfaces.contains(UniqueColumnCombinationsAlgorithm.class)) {
      types.add(AlgorithmType.UCC);
    }
    if (interfaces.contains(ConditionalUniqueColumnCombinationAlgorithm.class)) {
      types.add(AlgorithmType.CUCC);
    }
    if (interfaces.contains(OrderDependencyAlgorithm.class)) {
      types.add(AlgorithmType.OD);
    }
    if (interfaces.contains(BasicStatisticsAlgorithm.class)) {
      types.add(AlgorithmType.BASIC_STAT);
    }
    if (interfaces.contains(TempFileAlgorithm.class)) {
      types.add(AlgorithmType.TEMP_FILE);
    }
    if (interfaces.contains(ProgressEstimatingAlgorithm.class)) {
      types.add(AlgorithmType.PROGRESS_EST);
    }
    if (interfaces.contains(RelationalInputParameterAlgorithm.class)) {
      types.add(AlgorithmType.RELATIONAL_INPUT);
    }
    if (interfaces.contains(FileInputParameterAlgorithm.class)) {
      types.add(AlgorithmType.FILE_INPUT);
    }
    if (interfaces.contains(TableInputParameterAlgorithm.class)) {
      types.add(AlgorithmType.TABLE_INPUT);
    }
    if (interfaces.contains(DatabaseConnectionParameterAlgorithm.class)) {
      types.add(AlgorithmType.DB_CONNECTION);
    }
  }

  public boolean hasType(AlgorithmType type) {
    return types.contains(type);
  }

  public HashSet getTypes() {
    return this.types;
  }

  public Algorithm getAlgorithm() {
    return algorithm;
  }

  public Set<Class<?>> getInterfaces() {
    return this.interfaces;
  }

  protected Set<Class<?>> extractInterfaces(Object object) {
    return new HashSet<>(ClassUtils.getAllInterfaces(object.getClass()));
  }

}
