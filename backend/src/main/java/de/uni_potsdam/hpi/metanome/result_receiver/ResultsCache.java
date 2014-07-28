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

import de.uni_potsdam.hpi.metanome.algorithm_integration.results.BasicStatistic;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.FunctionalDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.InclusionDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.Result;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.UniqueColumnCombination;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Stores all received Results in a list and returns the new results on call to {@link
 * ResultsCache#getNewResults()}.
 *
 * @author Jakob Zwiener
 */
public class ResultsCache implements CloseableOmniscientResultReceiver {

  protected List<Result> results = new LinkedList<>();

  @Override
  public void receiveResult(BasicStatistic statistic) {
    results.add(statistic);
  }

  @Override
  public void receiveResult(FunctionalDependency functionalDependency) {
    results.add(functionalDependency);
  }

  @Override
  public void receiveResult(InclusionDependency inclusionDependency) {
    results.add(inclusionDependency);
  }

  @Override
  public void receiveResult(UniqueColumnCombination uniqueColumnCombination) {
    results.add(uniqueColumnCombination);
  }

  public void receiveResult(ConditionalUniqueColumnCombination conditionalUniqueColumnCombination) {
    results.add(conditionalUniqueColumnCombination);
  }

  /**
   * Should return all results once. After receiving the list it should be cleared and only filled
   * by new results.
   *
   * @return new results
   */
  public ArrayList<Result> getNewResults() {
    // FIXME synchronization
    ArrayList<Result> currentResults = new ArrayList<>(results);
    results.clear();
    return currentResults;
  }

  @Override
  public void close() throws IOException {

  }

}
