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
import de.metanome.algorithm_integration.results.*;
import de.metanome.backend.results_db.ResultType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ResultCounter extends ResultReceiver {

  protected EnumMap<ResultType, Integer> resultCounts;

  public ResultCounter(String algorithmExecutionIdentifier, List<String> acceptableColumnNames) throws FileNotFoundException {
    super(algorithmExecutionIdentifier, acceptableColumnNames);
    this.resultCounts = new EnumMap<>(ResultType.class);
  }

  protected ResultCounter(String algorithmExecutionIdentifier, List<String> acceptableColumnNames, Boolean test)
    throws FileNotFoundException {
    super(algorithmExecutionIdentifier, acceptableColumnNames, test);
    this.resultCounts = new EnumMap<>(ResultType.class);
  }

  @Override
  public void receiveResult(BasicStatistic statistic) throws CouldNotReceiveResultException {
    this.addCount(ResultType.STAT);
  }

  @Override
  public void receiveResult(ConditionalUniqueColumnCombination conditionalUniqueColumnCombination)
    throws CouldNotReceiveResultException {
    this.addCount(ResultType.CUCC);
  }

  @Override
  public void receiveResult(FunctionalDependency functionalDependency)
    throws CouldNotReceiveResultException {
    this.addCount(ResultType.FD);
  }

  @Override
  public void receiveResult(InclusionDependency inclusionDependency)
    throws CouldNotReceiveResultException {
    this.addCount(ResultType.IND);
  }

  @Override
  public void receiveResult(OrderDependency orderDependency) throws CouldNotReceiveResultException {
    this.addCount(ResultType.OD);
  }

  @Override
  public void receiveResult(UniqueColumnCombination uniqueColumnCombination)
    throws CouldNotReceiveResultException {
    this.addCount(ResultType.UCC);
  }

  protected void addCount(ResultType type) throws CouldNotReceiveResultException {
    if (!resultCounts.containsKey(type)) {
      resultCounts.put(type, 1);
    } else {
      resultCounts.put(type, resultCounts.get(type) + 1);
    }
  }

  /**
   * When the result receiver is closed, the results are written to disk.
   */
  @Override
  public void close() throws IOException {
    for (Map.Entry<ResultType, Integer> entry : resultCounts.entrySet()) {
      ResultType type = entry.getKey();
      Integer count = entry.getValue();
      write(type.getEnding(), type.getName(), count);
    }
  }

  private void write(String fileSuffix, String name, int count) throws FileNotFoundException {
    PrintWriter writer = new PrintWriter(getOutputFilePathPrefix() + fileSuffix);
    writer.write("### VOID ###\n");
    writer.write(name + ": " + count);
    writer.close();
  }

  public EnumMap<ResultType, Integer> getResults() {
    return resultCounts;
  }

}
