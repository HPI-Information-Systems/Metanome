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
package de.metanome.backend.results_db;

import java.io.Serializable;

public enum AlgorithmType implements Serializable {

  FD("Functional Dependency Algorithm", ResultType.FD),
  MD("Matching Dependency Algorithm", ResultType.MD),
  CFD("Conditional Functional Dependency Algorithm", ResultType.CFD),
  IND("Inclusion Dependency Algorithm", ResultType.IND),
  UCC("Unique Column Combination Algorithm", ResultType.UCC),
  CUCC("Conditional Unique Column Combination Algorithm", ResultType.CUCC),
  OD("Order Dependency Algorithm", ResultType.OD),
  MVD("Multivalued Dependency Algorithm", ResultType.MVD),
  BASIC_STAT("Basic Statistic Algorithm", ResultType.STAT),
  DC("Denial Constraint Algorithm", ResultType.DC),
  TEMP_FILE("Temporary File Algorithm", null),
  RELATIONAL_INPUT("Relational Input Algorithm", null),
  FILE_INPUT("File Input Algorithm", null),
  TABLE_INPUT("Table Input Algorithm", null),
  DB_CONNECTION("Database Connection Algorithm", null);

  private String name;
  private ResultType resultType;

  AlgorithmType(String name, ResultType resultType) {
    this.name = name;
    this.resultType = resultType;
  }

  public ResultType getResultType() {
    return this.resultType;
  }

  public String getName() {
    return this.name;
  }

}
