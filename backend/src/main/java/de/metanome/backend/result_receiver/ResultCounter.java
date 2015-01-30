/*
 * Copyright 2015 by the Metanome project
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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ResultCounter extends ResultReceiver {
  
  private int statsCount = 0;
  private int indCount = 0;
  private int fdCount = 0;
  private int uccCount = 0;
  private int cuccCount = 0;
  private int odCount = 0;

  public ResultCounter(String algorithmExecutionIdentifier) throws FileNotFoundException {
    super(algorithmExecutionIdentifier);
  }
  protected ResultCounter(String algorithmExecutionIdentifier, Boolean test) throws FileNotFoundException {
    super(algorithmExecutionIdentifier, test);
  }

  @Override
  public void receiveResult(BasicStatistic statistic) throws CouldNotReceiveResultException {
    this.statsCount++;
  }

  @Override
  public void receiveResult(ConditionalUniqueColumnCombination conditionalUniqueColumnCombination)
      throws CouldNotReceiveResultException {
    this.cuccCount++;
  }

  @Override
  public void receiveResult(FunctionalDependency functionalDependency)
      throws CouldNotReceiveResultException {
    this.fdCount++;
  }

  @Override
  public void receiveResult(InclusionDependency inclusionDependency)
      throws CouldNotReceiveResultException {
    this.indCount++;
  }

  @Override
  public void receiveResult(OrderDependency orderDependency) throws CouldNotReceiveResultException {
    this.odCount++;
  }

  @Override
  public void receiveResult(UniqueColumnCombination uniqueColumnCombination)
      throws CouldNotReceiveResultException {
    this.uccCount++;
  }


  public List<Result> getResults() {
    return new ArrayList<Result>();
  }

  /**
   * When the result receiver is closed, the results are written to disk.
   * @throws IOException
   */
  @Override
  public void close() throws IOException {
    if (statsCount > 0)
      write(STATS_ENDING, "Basic Statistic", statsCount);
    if (uccCount > 0)
      write(UCC_ENDING, "Unique Column Combination", uccCount);
    if (fdCount > 0)
      write(FD_ENDING, "Functional Dependency", fdCount);
    if (indCount > 0)
      write(IND_ENDING, "Inclusion Dependency", indCount);
    if (odCount > 0)
      write(OD_ENDING, "Order Dependency", odCount);
    if (cuccCount > 0)
      write(CUCC_ENDING, "Conditional Unique Column Combination", cuccCount);
  }

  private void write(String fileSuffix, String name, int count) throws FileNotFoundException {
    PrintWriter writer = new PrintWriter(getOutputFilePathPrefix() + fileSuffix);
    writer.write("### VOID ###\n");
    writer.write(name + ": " + count);
    writer.close();
  }

  public int getStatsCount() {
    return statsCount;
  }

  public int getIndCount() {
    return indCount;
  }

  public int getFdCount() {
    return fdCount;
  }

  public int getUccCount() {
    return uccCount;
  }

  public int getCuccCount() {
    return cuccCount;
  }

  public int getOdCount() {
    return odCount;
  }
}
