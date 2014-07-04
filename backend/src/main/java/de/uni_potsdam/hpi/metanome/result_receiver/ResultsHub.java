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

package de.uni_potsdam.hpi.metanome.result_receiver;

import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.BasicStatistic;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.FunctionalDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.InclusionDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.UniqueColumnCombination;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ResultsHub implements CloseableOmniscientResultReceiver {

  protected Set<CloseableOmniscientResultReceiver> subscriber = new HashSet<>();

  /**
   * Adds an {@link CloseableOmniscientResultReceiver} to the set of subscribers.
   */
  public void addSubscriber(CloseableOmniscientResultReceiver resultReceiver) {
    subscriber.add(resultReceiver);
  }

  /**
   * @return subscriber
   */
  public Set<CloseableOmniscientResultReceiver> getSubscriber() {
    return subscriber;
  }

  /**
   * Removes an {@link CloseableOmniscientResultReceiver} from the subscriber set.
   */
  public void removeSubscriber(CloseableOmniscientResultReceiver resultReceiver) {
    subscriber.remove(resultReceiver);
  }

  @Override
  public void receiveResult(BasicStatistic statistic)
      throws CouldNotReceiveResultException {
    for (CloseableOmniscientResultReceiver resultReceiver : subscriber) {
      resultReceiver.receiveResult(statistic);
    }
  }

  @Override
  public void receiveResult(FunctionalDependency functionalDependency)
      throws CouldNotReceiveResultException {
    for (CloseableOmniscientResultReceiver resultReceiver : subscriber) {
      resultReceiver.receiveResult(functionalDependency);
    }
  }

  @Override
  public void receiveResult(InclusionDependency inclusionDependency)
      throws CouldNotReceiveResultException {
    for (CloseableOmniscientResultReceiver resultReceiver : subscriber) {
      resultReceiver.receiveResult(inclusionDependency);
    }

  }

  @Override
  public void receiveResult(UniqueColumnCombination uniqueColumnCombination)
      throws CouldNotReceiveResultException {
    for (CloseableOmniscientResultReceiver resultReceiver : subscriber) {
      resultReceiver.receiveResult(uniqueColumnCombination);
    }
  }

  @Override
  public void close() throws IOException {
    for (CloseableOmniscientResultReceiver resultReceiver : subscriber) {
      resultReceiver.close();
    }
  }

}
