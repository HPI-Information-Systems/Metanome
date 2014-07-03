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

package de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.BasicStatisticsResultReceiver;

/**
 * An {@link Algorithm} that discovers "simple" statistics such as min/max values, data types etc.
 */
public interface BasicStatisticsAlgorithm extends Algorithm {

  /**
   * Sets a {@link BasicStatisticsResultReceiver} to send the results to.
   *
   * @param resultReceiver the result receiver that basic statistics are sent to
   */
  void setResultReceiver(BasicStatisticsResultReceiver resultReceiver);
}
