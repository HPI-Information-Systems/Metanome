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
import de.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.metanome.algorithm_integration.algorithm_types.InclusionDependencyAlgorithm;
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

  private boolean functionalDependency = false;
  private boolean inclusionDependency = false;
  private boolean uniqueColumnCombination = false;
  private boolean conditionalUniqueColumnCombination = false;
  private boolean basicStatistic = false;

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

    setInterfaces();
  }

  private void setInterfaces() {
    Set<Class<?>> interfaces = getInterfaces(algorithm);

    if (interfaces.contains(FunctionalDependencyAlgorithm.class))
      functionalDependency = true;
    if (interfaces.contains(InclusionDependencyAlgorithm.class))
      inclusionDependency = true;
    if (interfaces.contains(UniqueColumnCombinationsAlgorithm.class))
      uniqueColumnCombination = true;
    if (interfaces.contains(ConditionalUniqueColumnCombinationAlgorithm.class))
      conditionalUniqueColumnCombination = true;
    if (interfaces.contains(BasicStatisticsAlgorithm.class))
      basicStatistic = true;
  }

  public boolean isFunctionalDependencyAlgorithm() {
    return functionalDependency;
  }

  public boolean isInclusionDependencyAlgorithm() {
    return inclusionDependency;
  }

  public boolean isUniqueColumnCombinationAlgorithm() {
    return uniqueColumnCombination;
  }

  public boolean isConditionalUniqueColumnCombinationAlgorithm() {
    return conditionalUniqueColumnCombination;
  }

  public boolean isBasicStatisticAlgorithm() {
    return basicStatistic;
  }

  public Algorithm getAlgorithm() {
    return algorithm;
  }

  protected Set<Class<?>> getInterfaces(Object object) {
    return new HashSet<>(ClassUtils.getAllInterfaces(object.getClass()));
  }

}
