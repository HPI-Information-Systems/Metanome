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
import de.metanome.backend.helper.ExceptionParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Writes all received Results to disk. When all results were received, the results are
 * read again and returned.
 */
public class ResultPrinter extends ResultReceiver {

  protected PrintStream statStream;
  protected PrintStream fdStream;
  protected PrintStream uccStream;
  protected PrintStream cuccStream;
  protected PrintStream indStream;
  protected PrintStream odStream;

  public ResultPrinter(String algorithmExecutionIdentifier)
      throws FileNotFoundException {
    super(algorithmExecutionIdentifier);
  }
  protected ResultPrinter(String algorithmExecutionIdentifier, Boolean test)
      throws FileNotFoundException {
    super(algorithmExecutionIdentifier, test);
  }

  @Override
  public void receiveResult(BasicStatistic statistic)
      throws CouldNotReceiveResultException {
    getStatStream().println(statistic.toString());
  }

  @Override
  public void receiveResult(FunctionalDependency functionalDependency)
      throws CouldNotReceiveResultException {
    getFdStream().println(functionalDependency.toString());
  }

  @Override
  public void receiveResult(InclusionDependency inclusionDependency)
      throws CouldNotReceiveResultException {
    getIndStream().println(inclusionDependency.toString());
  }

  @Override
  public void receiveResult(UniqueColumnCombination uniqueColumnCombination)
      throws CouldNotReceiveResultException {
    getUccStream().println(uniqueColumnCombination.toString());
  }

  @Override
  public void receiveResult(ConditionalUniqueColumnCombination conditionalUniqueColumnCombination)
      throws CouldNotReceiveResultException {
    getCuccStream().println(conditionalUniqueColumnCombination.buildPatternTableau());
  }
  
  @Override
  public void receiveResult(OrderDependency orderDependency) throws CouldNotReceiveResultException {
    getOdStream().println(orderDependency.toString());
  }

  protected PrintStream getStatStream() throws CouldNotReceiveResultException {
    if (statStream == null) {
      statStream = openStream(STATS_ENDING);
    }

    return statStream;
  }

  protected PrintStream getFdStream() throws CouldNotReceiveResultException {
    if (fdStream == null) {
      fdStream = openStream(FD_ENDING);
    }

    return fdStream;
  }

  protected PrintStream getIndStream() throws CouldNotReceiveResultException {
    if (indStream == null) {
      indStream = openStream(IND_ENDING);
    }

    return indStream;
  }

  protected PrintStream getUccStream() throws CouldNotReceiveResultException {
    if (uccStream == null) {
      uccStream = openStream(UCC_ENDING);
    }

    return uccStream;
  }

  protected PrintStream getCuccStream() throws CouldNotReceiveResultException {
    if (cuccStream == null) {
      cuccStream = openStream(CUCC_ENDING);
    }

    return cuccStream;

  }
  
  protected PrintStream getOdStream() throws CouldNotReceiveResultException {
    if (odStream == null) {
      odStream = openStream(OD_ENDING);
    }

    return odStream;

  }

  protected PrintStream openStream(String fileSuffix) throws CouldNotReceiveResultException {
    try {
      return new PrintStream(new FileOutputStream(getOutputFilePathPrefix() + fileSuffix), true);
    } catch (FileNotFoundException e) {
      throw new CouldNotReceiveResultException(
          ExceptionParser.parse(e, "Could not open result file for writing"), e);
    }
  }

  @Override
  public void close() throws IOException {
    if (statStream != null) {
      statStream.close();
    }
    if (fdStream != null) {
      fdStream.close();
    }
    if (indStream != null) {
      indStream.close();
    }
    if (uccStream != null) {
      uccStream.close();
    }
    if (cuccStream != null) {
      cuccStream.close();
    }
    if (odStream != null) {
      odStream.close();
    }
  }

  /**
   * Reads the results from disk and returns them.
   * @return all results
   */
  public List<Result> getResults() throws IOException {
    List<Result> results = new ArrayList<>();

    if (existsFile(IND_ENDING)) {
      results.addAll(readResult(IND_ENDING, new ResultHandler() {
        @Override
        public Result convert(String str) {
          return InclusionDependency.fromString(str);
        }
      }));
    }
    if (existsFile(FD_ENDING)) {
      results.addAll(readResult(FD_ENDING, new ResultHandler() {
        @Override
        public Result convert(String str) {
          return FunctionalDependency.fromString(str);
        }
      }));
    }
    if (existsFile(UCC_ENDING)) {
      results.addAll(readResult(UCC_ENDING, new ResultHandler() {
        @Override
        public Result convert(String str) {
          return UniqueColumnCombination.fromString(str);
        }
      }));
    }
    if (existsFile(CUCC_ENDING)) {
      results.addAll(readResult(CUCC_ENDING, new ResultHandler() {
        @Override
        public Result convert(String str) {
          return ConditionalUniqueColumnCombination.fromString(str);
        }
      }));
    }
    if (existsFile(OD_ENDING)) {
      results.addAll(readResult(OD_ENDING, new ResultHandler() {
        @Override
        public Result convert(String str) {
          return OrderDependency.fromString(str);
        }
      }));
    }
    if (existsFile(STATS_ENDING)) {
      results.addAll(readResult(STATS_ENDING, new ResultHandler() {
        @Override
        public Result convert(String str) {
          return BasicStatistic.fromString(str);
        }
      }));
    }

    return results;
  }

  private Boolean existsFile(String fileSuffix) {
    return new File(getOutputFilePathPrefix() + fileSuffix).exists();
  }

  private List<Result> readResult(String fileSuffix, ResultHandler handler) throws IOException {
    List<Result> results = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(
        new FileReader(getOutputFilePathPrefix() + fileSuffix))) {
      String line = br.readLine();

      while (line != null) {
        results.add(handler.convert(line));
        line = br.readLine();
      }
    }
    return results;
  }

  public interface ResultHandler {
    public Result convert(String str);
  }
}
