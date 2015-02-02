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

package de.metanome.backend.result_receiver;

import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.results.BasicStatistic;
import de.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination;
import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.algorithm_integration.results.InclusionDependency;
import de.metanome.algorithm_integration.results.OrderDependency;
import de.metanome.algorithm_integration.results.Result;
import de.metanome.algorithm_integration.results.UniqueColumnCombination;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Stores all received Results in a list and returns the new results on call to {@link
 * ResultCache#fetchNewResults()}. When all results were received, they are written to disk.
 *
 * @author Jakob Zwiener
 */
public class ResultCache extends ResultReceiver {

  protected List<Result> results = new LinkedList<>();
  protected int fromIndex = 0;

  public ResultCache(String algorithmExecutionIdentifier)
      throws FileNotFoundException {
    super(algorithmExecutionIdentifier);
  }
  protected ResultCache(String algorithmExecutionIdentifier, Boolean test)
      throws FileNotFoundException {
    super(algorithmExecutionIdentifier, test);
  }

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

  @Override
  public void receiveResult(OrderDependency orderDependency) throws CouldNotReceiveResultException {
    results.add(orderDependency);
  }

  /**
   * Should return all results once. Copies the new received results and returns them.
   *
   * @return new results
   */
  public List<Result> fetchNewResults() {
    int toIndex = this.results.size();
    List<Result> newResults = results.subList(this.fromIndex, toIndex);
    this.fromIndex = toIndex;
    return newResults;
  }

  /**
   * When the result receiver is closed, the results are written to disk.
   * @throws IOException
   */
  @Override
  public void close() throws IOException {
    ResultPrinter printer = new ResultPrinter(this.algorithmExecutionIdentifier, this.testDirectory);
    for (Result result: results) {
      try {
        if (result instanceof FunctionalDependency)
          printer.receiveResult((FunctionalDependency) result);
        else if (result instanceof InclusionDependency)
          printer.receiveResult((InclusionDependency) result);
        else if (result instanceof UniqueColumnCombination)
          printer.receiveResult((UniqueColumnCombination) result);
        else if (result instanceof ConditionalUniqueColumnCombination)
          printer.receiveResult((ConditionalUniqueColumnCombination) result);
        else if (result instanceof OrderDependency)
          printer.receiveResult((OrderDependency) result);
        else if (result instanceof BasicStatistic)
          printer.receiveResult((BasicStatistic) result);
      } catch (CouldNotReceiveResultException ignored) {

      }
    }
    printer.close();
  }

}
