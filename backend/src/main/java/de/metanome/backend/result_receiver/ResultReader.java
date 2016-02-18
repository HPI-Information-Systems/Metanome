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

import de.metanome.algorithm_integration.results.*;
import de.metanome.backend.results_db.ResultType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultReader<T extends Result> {

  private ResultType type;

  public ResultReader(ResultType type) {
    this.type = type;
  }

  public static List<Result> readResultsFromFile(String fileName, String type)
    throws IOException, NullPointerException, IndexOutOfBoundsException {
    List<Result> results = new ArrayList<>();
    Map<String, String> tableMapping = new HashMap<>();
    Map<String, String> columnMapping = new HashMap<>();

    Boolean isTableMapping = false;
    Boolean isColumnMapping = false;

    File resultFile = new File(fileName);
    if (!resultFile.exists()) {
      resultFile.createNewFile();
    }

    BufferedReader br = new BufferedReader(new FileReader(resultFile));
    String line;
    while ((line = br.readLine()) != null) {
      if (line.startsWith(ResultPrinter.TABLE_MARKER)) {
        isTableMapping = true;
        isColumnMapping = false;
        continue;
      } else if (line.startsWith(ResultPrinter.COLUMN_MARKER)) {
        isTableMapping = false;
        isColumnMapping = true;
        continue;
      } else if (line.startsWith(ResultPrinter.RESULT_MARKER)) {
        isTableMapping = false;
        isColumnMapping = false;
        continue;
      }

      if (isTableMapping) {
        String[] parts = line.split(ResultReceiver.MAPPING_SEPARATOR);
        tableMapping.put(parts[1], parts[0]);
      } else if (isColumnMapping) {
        String[] parts = line.split(ResultReceiver.MAPPING_SEPARATOR);
        columnMapping.put(parts[1], parts[0]);
      } else {
        results.add(ResultReader.convertStringToResult(line, type, tableMapping, columnMapping));
      }
    }

    br.close();
    return results;
  }

  protected static Result convertStringToResult(String str, String name, Map<String, String> tableMapping,
                                             Map<String, String> columnMapping)
    throws IOException, NullPointerException, IndexOutOfBoundsException {
    if (name.equals(ResultType.CUCC.getName())) {
      JsonConverter<ConditionalUniqueColumnCombination> jsonConverter = new JsonConverter<>();
      return jsonConverter.fromJsonString(str, ConditionalUniqueColumnCombination.class);

    } else if (name.equals(ResultType.OD.getName())) {
      if (tableMapping.isEmpty() && columnMapping.isEmpty()) {
        JsonConverter<OrderDependency> jsonConverter = new JsonConverter<>();
        return jsonConverter.fromJsonString(str, OrderDependency.class);
      } else {
        return OrderDependency.fromString(tableMapping, columnMapping, str);
      }

    } else if (name.equals(ResultType.IND.getName())) {
      if (tableMapping.isEmpty() && columnMapping.isEmpty()) {
        JsonConverter<InclusionDependency> jsonConverter = new JsonConverter<>();
        return jsonConverter.fromJsonString(str, InclusionDependency.class);
      } else {
        return InclusionDependency.fromString(tableMapping, columnMapping, str);
      }

    } else if (name.equals(ResultType.FD.getName())) {
      if (tableMapping.isEmpty() && columnMapping.isEmpty()) {
        JsonConverter<FunctionalDependency> jsonConverter = new JsonConverter<>();
        return jsonConverter.fromJsonString(str, FunctionalDependency.class);
      } else {
        return FunctionalDependency.fromString(tableMapping, columnMapping, str);
      }

    } else if (name.equals(ResultType.UCC.getName())) {
      if (tableMapping.isEmpty() && columnMapping.isEmpty()) {
        JsonConverter<UniqueColumnCombination> jsonConverter = new JsonConverter<>();
        return jsonConverter.fromJsonString(str, UniqueColumnCombination.class);
      } else {
        return UniqueColumnCombination.fromString(tableMapping, columnMapping, str);
      }

    } else if (name.equals(ResultType.STAT.getName())) {
      JsonConverter<BasicStatistic> jsonConverter = new JsonConverter<>();
      return jsonConverter.fromJsonString(str, BasicStatistic.class);

    }

    return null;
  }

  public static Integer readCounterResultFromFile(String fileName)
    throws IOException {
    File resultFile = new File(fileName);

    BufferedReader br = new BufferedReader(new FileReader(resultFile));
    String line;
    while ((line = br.readLine()) != null) {
      if (line.startsWith(ResultCounter.HEADER)) {
        continue;
      }

      return Integer.valueOf(line.split(": ")[1]);
    }
    return 0;
  }

  public List<T> readResultsFromFile(String fileName)
    throws IOException, NullPointerException, IndexOutOfBoundsException {
    File resultFile = new File(fileName);
    if (!resultFile.exists()) {
      resultFile.createNewFile();
    }
    return (List<T>) ResultReader.readResultsFromFile(fileName, this.type.getName());
  }

}
