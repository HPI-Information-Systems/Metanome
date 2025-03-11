/**
 * Copyright 2015-2017 by Metanome Project
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
import java.util.Map;

/**
 * Counts all received results. When all results are received, the counts are written to disk.
 */
public class ResultCounter extends ResultReceiver {

  protected EnumMap<ResultType, Integer> resultCounts;

  public static final String HEADER = "### VOID ###";


  public ResultCounter(String algorithmExecutionIdentifier) throws FileNotFoundException {
    super(algorithmExecutionIdentifier, null);
    this.resultCounts = new EnumMap<>(ResultType.class);
  }

  public ResultCounter(String algorithmExecutionIdentifier, Boolean test) throws FileNotFoundException {
    super(algorithmExecutionIdentifier, null, test);
    this.resultCounts = new EnumMap<>(ResultType.class);
  }

  @Override
  public void receiveResult(BasicStatistic statistic) throws CouldNotReceiveResultException {
    this.addCount(ResultType.BASIC_STAT);
  }

  @Override
  public void receiveResult(ConditionalUniqueColumnCombination conditionalUniqueColumnCombination)
    throws CouldNotReceiveResultException {
    this.addCount(ResultType.CUCC);
  }

  @Override
  public void receiveResult(RelaxedUniqueColumnCombination relaxedUniqueColumnCombination)
          throws CouldNotReceiveResultException {
    this.addCount(ResultType.RUCC);
  }

  @Override
  public void receiveResult(FunctionalDependency functionalDependency)
    throws CouldNotReceiveResultException {
    this.addCount(ResultType.FD);
  }
  
  @Override
  public void receiveResult(ConditionalInclusionDependency conditionalInclusionDependency)
    throws CouldNotReceiveResultException {
    this.addCount(ResultType.CID);
  }

  @Override
  public void receiveResult(RelaxedInclusionDependency relaxedInclusionDependency)
          throws CouldNotReceiveResultException {
    this.addCount(ResultType.RIND);
  }

  @Override
  public void receiveResult(MatchingDependency matchingDependency)
      throws CouldNotReceiveResultException {
    this.addCount(ResultType.MD);
  }

  @Override
  public void receiveResult(ConditionalFunctionalDependency conditionalFunctionalDependency)
          throws CouldNotReceiveResultException {
    this.addCount(ResultType.CFD);
  }

  @Override
  public void receiveResult(RelaxedFunctionalDependency relaxedFunctionalDependency)
          throws CouldNotReceiveResultException {
    this.addCount(ResultType.RFD);
  }
  
  @Override
  public void receiveResult(MultivaluedDependency multivaluedDependency)
    throws CouldNotReceiveResultException {
    this.addCount(ResultType.MVD);
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

  @Override
  public void receiveResult(DenialConstraint denialConstraint)
      throws CouldNotReceiveResultException {
    this.addCount(ResultType.DC);
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
    writer.write(HEADER + "\n");
    writer.write(name + ": " + count);
    writer.close();
  }

  public EnumMap<ResultType, Integer> getResults() {
    return resultCounts;
  }

}
