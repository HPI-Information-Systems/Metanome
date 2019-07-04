/**
 * Copyright 2015-2019 by Metanome Project
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
package de.metanome.backend.results_db;

import de.metanome.algorithm_integration.algorithm_types.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Stream;

public enum AlgorithmType implements Serializable {

  FD("Functional Dependency Algorithm", ResultType.FD, FunctionalDependencyAlgorithm.class, "fd", true),
  CID("Conditional Inclusion Dependency Algorithm",  ResultType.CID, ConditionalInclusionDependencyAlgorithm.class, "cid", true),
  MD("Matching Dependency Algorithm", ResultType.MD, MatchingDependencyAlgorithm.class, "md", true),
  CFD("Conditional Functional Dependency Algorithm", ResultType.CFD, ConditionalFunctionalDependencyAlgorithm.class, "cfd", true),
  IND("Inclusion Dependency Algorithm", ResultType.IND, InclusionDependencyAlgorithm.class, "ind", true),
  UCC("Unique Column Combination Algorithm", ResultType.UCC, UniqueColumnCombinationsAlgorithm.class, "ucc", true),
  CUCC("Conditional Unique Column Combination Algorithm", ResultType.CUCC, ConditionalUniqueColumnCombinationAlgorithm.class, "cucc", true),
  OD("Order Dependency Algorithm", ResultType.OD, OrderDependencyAlgorithm.class, "od", true),
  MVD("Multivalued Dependency Algorithm", ResultType.MVD, MultivaluedDependencyAlgorithm.class, "mvd", true),
  BASIC_STAT("Basic Statistic Algorithm", ResultType.STAT, BasicStatisticsAlgorithm.class, "basicStat", true),
  DC("Denial Constraint Algorithm", ResultType.DC, DenialConstraintAlgorithm.class, "dc", true),
  
  TEMP_FILE("Temporary File Algorithm", null, TempFileAlgorithm.class, "tempFile", false),
  RELATIONAL_INPUT("Relational Input Algorithm", null, RelationalInputParameterAlgorithm.class, "relationalInput", false),
  FILE_INPUT("File Input Algorithm", null, FileInputParameterAlgorithm.class, "fileInput", false),
  TABLE_INPUT("Table Input Algorithm", null, TableInputParameterAlgorithm.class, "tableInput", false),
  DB_CONNECTION("Database Connection Algorithm", null, DatabaseConnectionParameterAlgorithm.class, "dbConnection", false);

  private String name;
  private ResultType resultType;
  private Class<?> algorithmClass;
  private String abbreviation;
  private boolean isExecutable;

  AlgorithmType(String name, ResultType resultType, Class<?> algorithmClass, String abbreviation, boolean isExecutable) {
    this.name = name;
    this.resultType = resultType;
    this.algorithmClass = algorithmClass;
    this.abbreviation = abbreviation;
    this.isExecutable = isExecutable;
  }

  public ResultType getResultType() {
    return this.resultType;
  }

  public String getName() {
    return this.name;
  }
  
  public Class<?> getAlgorithmClass() {
    return this.algorithmClass;
  }
  
  public boolean isIsExecutable() {
    return this.isExecutable;
  }
  
  public static Stream<AlgorithmType> asStream() {
    return Arrays.asList(values()).stream();
  }
  
  public String getAbbreviation() {
    return this.abbreviation;
  }
}