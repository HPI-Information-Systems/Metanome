/**
 * Copyright 2014, 2016 by Metanome Project
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
package de.metanome.algorithm_integration.algorithm_types;

import de.metanome.algorithm_integration.Algorithm;
import de.metanome.algorithm_integration.result_receiver.ConditionalInclusionDependencyResultReceiver;

/**
 * An {@link Algorithm} that discovers conditional inclusion dependencies.
 *
 * @author Joana Bergsiek
 */
public interface ConditionalInclusionDependencyAlgorithm extends Algorithm {

  /**
   * Sets a {@link ConditionalInclusionDependencyResultReceiver} to send the results to.
   *
   * @param resultReceiver the result receiver the algorithm sents found {@link
   *                       de.metanome.algorithm_integration.results.ConditionalInclusionDependency}s to.
   */
  void setResultReceiver(ConditionalInclusionDependencyResultReceiver resultReceiver);

}
