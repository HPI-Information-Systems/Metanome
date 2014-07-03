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

package de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver;

import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.BasicStatisticsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.BasicStatistic;

/**
 * Receives the results of a {@link BasicStatisticsAlgorithm}.
 */
public interface BasicStatisticsResultReceiver {

  /**
   * Receives a basic statistic, consisting of the concerned columns, a name and a value of the
   * statistic
   *
   * @param statistic the calculated value of the statistic on the columns
   * @throws CouldNotReceiveResultException if the statistic could not be received
   */
  void receiveResult(BasicStatistic statistic) throws CouldNotReceiveResultException;
}
