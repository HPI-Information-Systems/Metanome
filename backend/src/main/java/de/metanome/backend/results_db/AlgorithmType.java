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

  FD("Functional Dependency Algorithm", ResultType.FD, FunctionalDependencyAlgorithm.class, "fd"),
  CID("Conditional Inclusion Dependency Algorithm",  ResultType.CID, ConditionalInclusionDependencyAlgorithm.class, "cid"),
  MD("Matching Dependency Algorithm", ResultType.MD, MatchingDependencyAlgorithm.class, "md"),
  CFD("Conditional Functional Dependency Algorithm", ResultType.CFD, ConditionalFunctionalDependencyAlgorithm.class, "cfd"),
  IND("Inclusion Dependency Algorithm", ResultType.IND, InclusionDependencyAlgorithm.class, "ind"),
  UCC("Unique Column Combination Algorithm", ResultType.UCC, UniqueColumnCombinationsAlgorithm.class, "ucc"),
  CUCC("Conditional Unique Column Combination Algorithm", ResultType.CUCC, ConditionalUniqueColumnCombinationAlgorithm.class, "cucc"),
  OD("Order Dependency Algorithm", ResultType.OD, OrderDependencyAlgorithm.class, "od"),
  MVD("Multivalued Dependency Algorithm", ResultType.MVD, MultivaluedDependencyAlgorithm.class, "mvd"),
  BASIC_STAT("Basic Statistic Algorithm", ResultType.BASIC_STAT, BasicStatisticsAlgorithm.class, "basicStat"),
  DC("Denial Constraint Algorithm", ResultType.DC, DenialConstraintAlgorithm.class, "dc"),
  RFD("Relaxed Functional Dependency Algorithm", ResultType.RFD, RelaxedFunctionalDependencyAlgorithm.class, "rfd"),
  RIND("Relaxed Inclusion Dependency Algorithm",  ResultType.RIND, RelaxedInclusionDependencyAlgorithm.class, "rind"),
  RUCC("Relaxed Unique Column Combination Algorithm", ResultType.RUCC, RelaxedUniqueColumnCombinationAlgorithm.class, "rucc"),

  TEMP_FILE("Temporary File Algorithm", null, TempFileAlgorithm.class, "tempFile"),
  RELATIONAL_INPUT("Relational Input Algorithm", null, RelationalInputParameterAlgorithm.class, "relationalInput"),
  FILE_INPUT("File Input Algorithm", null, FileInputParameterAlgorithm.class, "fileInput"),
  TABLE_INPUT("Table Input Algorithm", null, TableInputParameterAlgorithm.class, "tableInput"),
  DB_CONNECTION("Database Connection Algorithm", null, DatabaseConnectionParameterAlgorithm.class, "dbConnection");

  private String name;
  private ResultType resultType;
  private Class<?> algorithmClass;
  private String abbreviation;

  AlgorithmType(String name, ResultType resultType, Class<?> algorithmClass, String abbreviation) {
    this.name = name;
    this.resultType = resultType;
    this.algorithmClass = algorithmClass;
    this.abbreviation = abbreviation;
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
  
  public boolean hasResult() {
    return this.resultType != null;
  }
  
  public static Stream<AlgorithmType> asStream() {
    return Arrays.asList(values()).stream();
  }
  
  public String getAbbreviation() {
    return this.abbreviation;
  }
}