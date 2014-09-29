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

  private boolean functionalDependencyAlgorithm = false;
  private boolean inclusionDependencyAlgorithm = false;
  private boolean uniqueColumnCombinationAlgorithm = false;
  private boolean conditionalUniqueColumnCombinationAlgorithm = false;
  private boolean orderDependencyAlgorithm = false;
  private boolean basicStatisticAlgorithm = false;
  private boolean tempFileAlgorithm = false;
  private boolean progressEstimatingAlgorithm = false;

  private boolean relationalInputAlgorithm = false;
  private boolean fileInputAlgorithm = false;
  private boolean tableInputAlgorithm = false;
  private boolean databaseConnectionAlgorithm = false;

  Set<Class<?>> interfaces;

  /**
   *
   * @param algorithmPath the algorithm file, which should be analyzed.
   * @throws IllegalAccessException     if loading fails
   * @throws IOException                if loading fails
   * @throws InstantiationException     if loading fails
   * @throws NoSuchMethodException      if loading fails
   * @throws InvocationTargetException  if loading fails
   * @throws ClassNotFoundException     if loading fails
   */
  public AlgorithmAnalyzer(String algorithmPath)
      throws IllegalAccessException, IOException, InstantiationException, NoSuchMethodException,
             InvocationTargetException, ClassNotFoundException {
    AlgorithmJarLoader loader = new AlgorithmJarLoader();

    this.algorithm = loader.loadAlgorithm(algorithmPath);

    analyzerInterfaces();
  }

  private void analyzerInterfaces() {
    this.interfaces = extractInterfaces(algorithm);

    if (interfaces.contains(FunctionalDependencyAlgorithm.class))
      functionalDependencyAlgorithm = true;
    if (interfaces.contains(InclusionDependencyAlgorithm.class))
      inclusionDependencyAlgorithm = true;
    if (interfaces.contains(UniqueColumnCombinationsAlgorithm.class))
      uniqueColumnCombinationAlgorithm = true;
    if (interfaces.contains(ConditionalUniqueColumnCombinationAlgorithm.class))
      conditionalUniqueColumnCombinationAlgorithm = true;
    if (interfaces.contains(OrderDependencyAlgorithm.class))
      orderDependencyAlgorithm = true;
    if (interfaces.contains(BasicStatisticsAlgorithm.class))
      basicStatisticAlgorithm = true;
    if (interfaces.contains(TempFileAlgorithm.class))
      tempFileAlgorithm = true;
    if (interfaces.contains(ProgressEstimatingAlgorithm.class))
      progressEstimatingAlgorithm = true;
    if (interfaces.contains(RelationalInputParameterAlgorithm.class))
      relationalInputAlgorithm = true;
    if (interfaces.contains(FileInputParameterAlgorithm.class))
      fileInputAlgorithm = true;
    if (interfaces.contains(TableInputParameterAlgorithm.class))
      tableInputAlgorithm = true;
    if (interfaces.contains(DatabaseConnectionParameterAlgorithm.class))
      databaseConnectionAlgorithm = true;
  }

  public boolean isFunctionalDependencyAlgorithm() {
    return functionalDependencyAlgorithm;
  }

  public boolean isInclusionDependencyAlgorithm() {
    return inclusionDependencyAlgorithm;
  }

  public boolean isUniqueColumnCombinationAlgorithm() {
    return uniqueColumnCombinationAlgorithm;
  }

  public boolean isConditionalUniqueColumnCombinationAlgorithm() {
    return conditionalUniqueColumnCombinationAlgorithm;
  }

  public boolean isOrderDependencyAlgorithm() {
    return orderDependencyAlgorithm;
  }
  
  public boolean isBasicStatisticAlgorithm() {
    return basicStatisticAlgorithm;
  }

  public boolean isTempFileAlgorithm() {
    return tempFileAlgorithm;
  }

  public boolean isProgressEstimatingAlgorithm() {
    return progressEstimatingAlgorithm;
  }

  public boolean isRelationalInputAlgorithm() { return relationalInputAlgorithm; }

  public boolean isFileInputAlgorithm() { return fileInputAlgorithm; }

  public boolean isTableInputAlgorithm() { return tableInputAlgorithm; }

  public boolean isDatabaseConnectionAlgorithm() { return databaseConnectionAlgorithm; }

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
