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
import java.util.List;

public class ResultReader<T> {

  final Class<T> typeParameterClass;

  public ResultReader(Class<T> typeParameterClass) {
    this.typeParameterClass = typeParameterClass;
  }

  public static List<Result> readResultsFromFile(String fileName, String type) throws IOException {
    List<Result> results = new ArrayList<>();

    File resultFile = new File(fileName);

    BufferedReader br = new BufferedReader(new FileReader(resultFile));
    String line;
    while ((line = br.readLine()) != null) {
      results.add(convertStringToResult(line, type));
    }

    return results;
  }

  public static Result convertStringToResult(String str, String name) throws IOException {
    if (name.equals(ResultType.CUCC.getName())) {
      JsonConverter<ConditionalUniqueColumnCombination> jsonConverter = new JsonConverter<>();
      return jsonConverter.fromJsonString(str, ConditionalUniqueColumnCombination.class);

    } else if (name.equals(ResultType.OD.getName())) {
      JsonConverter<OrderDependency> jsonConverter = new JsonConverter<>();
      return jsonConverter.fromJsonString(str, OrderDependency.class);

    } else if (name.equals(ResultType.IND.getName())) {
      JsonConverter<InclusionDependency> jsonConverter = new JsonConverter<>();
      return jsonConverter.fromJsonString(str, InclusionDependency.class);

    } else if (name.equals(ResultType.FD.getName())) {
      JsonConverter<FunctionalDependency> jsonConverter = new JsonConverter<>();
      return jsonConverter.fromJsonString(str, FunctionalDependency.class);

    } else if (name.equals(ResultType.UCC.getName())) {
      JsonConverter<UniqueColumnCombination> jsonConverter = new JsonConverter<>();
      return jsonConverter.fromJsonString(str, UniqueColumnCombination.class);

    } else if (name.equals(ResultType.STAT.getName())) {
      JsonConverter<BasicStatistic> jsonConverter = new JsonConverter<>();
      return jsonConverter.fromJsonString(str, BasicStatistic.class);

    }

    return null;
  }

  public List<T> readResultsFromFile(String fileName)
    throws IOException {
    List<T> results = new ArrayList<>();

    File resultFile = new File(fileName);

    if (!resultFile.exists()) {
      resultFile.createNewFile();
    }

    BufferedReader br = new BufferedReader(new FileReader(resultFile));
    String line;
    while ((line = br.readLine()) != null) {
      JsonConverter<T> jsonConverter = new JsonConverter<>();
      results.add(jsonConverter.fromJsonString(line, this.typeParameterClass));
    }

    return results;
  }

  public static Integer readCounterResultFromFile(String fileName)
    throws IOException {
    File resultFile = new File(fileName);

    BufferedReader br = new BufferedReader(new FileReader(resultFile));
    String line;
    while ((line = br.readLine()) != null) {
      if (line.startsWith("###")) {
        continue;
      }

      return Integer.valueOf(line.split(": ")[1]);
    }
    return 0;
  }

}
