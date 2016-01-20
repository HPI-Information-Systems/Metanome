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

import com.fasterxml.jackson.core.JsonProcessingException;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.results.*;
import de.metanome.backend.results_db.ResultType;

import java.io.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

/**
 * Writes all received Results to disk. When all results were received, the results are read again
 * and returned.
 */
public class ResultPrinter extends ResultReceiver {

  protected EnumMap<ResultType, PrintStream> openStreams;

  public ResultPrinter(String algorithmExecutionIdentifier, List<String> acceptableColumnNames)
    throws FileNotFoundException {
    super(algorithmExecutionIdentifier, acceptableColumnNames);
    this.openStreams = new EnumMap<>(ResultType.class);
  }

  protected ResultPrinter(String algorithmExecutionIdentifier, List<String> acceptableColumnNames, Boolean test)
    throws FileNotFoundException {
    super(algorithmExecutionIdentifier, acceptableColumnNames, test);
    this.openStreams = new EnumMap<>(ResultType.class);
  }

  @Override
  public void receiveResult(BasicStatistic statistic)
    throws CouldNotReceiveResultException, ColumnNameMismatchException {
    if (this.acceptedResult(statistic)) {
      try {
        JsonConverter<BasicStatistic> jsonConverter = new JsonConverter<>();
        getStream(ResultType.STAT).println(jsonConverter.toJsonString(statistic));
      } catch (JsonProcessingException e) {
        throw new CouldNotReceiveResultException("Could not convert the result to JSON!");
      }
    } else {
      throw new ColumnNameMismatchException("The table/column name does not match the names in the input!");
    }
  }

  @Override
  public void receiveResult(FunctionalDependency functionalDependency)
    throws CouldNotReceiveResultException, ColumnNameMismatchException {
    if (this.acceptedResult(functionalDependency)) {
      try {
        JsonConverter<FunctionalDependency> jsonConverter = new JsonConverter<>();
        getStream(ResultType.FD).println(jsonConverter.toJsonString(functionalDependency));
      } catch (JsonProcessingException e) {
        throw new CouldNotReceiveResultException("Could not convert the result to JSON!");
      }
    } else {
      throw new ColumnNameMismatchException("The table/column name does not match the names in the input!");
    }
  }

  @Override
  public void receiveResult(InclusionDependency inclusionDependency)
    throws CouldNotReceiveResultException, ColumnNameMismatchException {
    if (this.acceptedResult(inclusionDependency)) {
      try {
        JsonConverter<InclusionDependency> jsonConverter = new JsonConverter<>();
        getStream(ResultType.IND).println(jsonConverter.toJsonString(inclusionDependency));
      } catch (JsonProcessingException e) {
        throw new CouldNotReceiveResultException("Could not convert the result to JSON!");
      }
    } else {
      throw new ColumnNameMismatchException("The table/column name does not match the names in the input!");
    }
  }

  @Override
  public void receiveResult(UniqueColumnCombination uniqueColumnCombination)
    throws CouldNotReceiveResultException, ColumnNameMismatchException {
    if (this.acceptedResult(uniqueColumnCombination)) {
      try {
        JsonConverter<UniqueColumnCombination> jsonConverter = new JsonConverter<>();
        getStream(ResultType.UCC).println(jsonConverter.toJsonString(uniqueColumnCombination));
      } catch (JsonProcessingException e) {
        throw new CouldNotReceiveResultException("Could not convert the result to JSON!");
      }
    } else {
      throw new ColumnNameMismatchException("The table/column name does not match the names in the input!");
    }
  }

  @Override
  public void receiveResult(ConditionalUniqueColumnCombination conditionalUniqueColumnCombination)
    throws CouldNotReceiveResultException, ColumnNameMismatchException {
    if (this.acceptedResult(conditionalUniqueColumnCombination)) {
      try {
        JsonConverter<ConditionalUniqueColumnCombination> jsonConverter = new JsonConverter<>();
        getStream(ResultType.CUCC)
          .println(jsonConverter.toJsonString(conditionalUniqueColumnCombination));
      } catch (JsonProcessingException e) {
        throw new CouldNotReceiveResultException("Could not convert the result to JSON!");
      }
    } else {
      throw new ColumnNameMismatchException("The table/column name does not match the names in the input!");
    }
  }

  @Override
  public void receiveResult(OrderDependency orderDependency)
    throws CouldNotReceiveResultException, ColumnNameMismatchException {
    if (this.acceptedResult(orderDependency)) {
      try {
        JsonConverter<OrderDependency> jsonConverter = new JsonConverter<>();
        getStream(ResultType.OD).println(jsonConverter.toJsonString(orderDependency));
      } catch (JsonProcessingException e) {
        throw new CouldNotReceiveResultException("Could not convert the result to JSON!");
      }
    } else {
      throw new ColumnNameMismatchException("The table/column name does not match the names in the input!");
    }
  }

  protected PrintStream getStream(ResultType type) throws CouldNotReceiveResultException {
    if (!openStreams.containsKey(type)) {
      openStreams.put(type, openStream(type.getEnding()));
    }
    return openStreams.get(type);
  }

  protected PrintStream openStream(String fileSuffix) throws CouldNotReceiveResultException {
    try {
      return new PrintStream(new FileOutputStream(getOutputFilePathPrefix() + fileSuffix), true);
    } catch (FileNotFoundException e) {
      throw new CouldNotReceiveResultException("Could not open result file for writing", e);
    }
  }

  @Override
  public void close() throws IOException {
    for (PrintStream stream : openStreams.values()) {
      stream.close();
    }
  }

  /**
   * Reads the results from disk and returns them.
   *
   * @return all results
   * @throws java.io.IOException if file could not be read
   */
  public List<Result> getResults() throws IOException {
    List<Result> results = new ArrayList<>();

    for (ResultType type : openStreams.keySet()) {
      if (existsFile(type.getEnding())) {
        results.addAll(readResult(type));
      }
    }

    return results;
  }

  private Boolean existsFile(String fileSuffix) {
    return new File(getOutputFilePathPrefix() + fileSuffix).exists();
  }

  private List<Result> readResult(ResultType type) throws IOException {
    List<Result> results = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(
      new FileReader(getOutputFilePathPrefix() + type.getEnding()))) {
      String line = br.readLine();

      while (line != null) {
        results.add(ResultReader.convertStringToResult(line, type.getName()));
        line = br.readLine();
      }
    }
    return results;
  }

}
