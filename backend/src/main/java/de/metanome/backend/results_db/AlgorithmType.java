/*
 * Copyright 2014 by the Metanome project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
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

  FD(ResultType.FD), // Functional Dependency Algorithm
  IND(ResultType.IND), // Inclusion Dependency Algorithm
  UCC(ResultType.UCC), // Unique Column Combination Algorithm
  CUCC(ResultType.CUCC), //Conditional Unique Column Combination Algorithm
  OD(ResultType.OD), //Order Dependency Algorithm
  BASIC_STAT(ResultType.STAT), //Basic Statistic Algorithm
  TEMP_FILE(null), // Temporary File Algorithm
  PROGRESS_EST(null), // Progress Estimating Algorithm
  RELATIONAL_INPUT(null), // Relational Input Algorithm
  FILE_INPUT(null), // File Input Algorithm
  TABLE_INPUT(null), // Table Input Algorithm
  DB_CONNECTION(null); // Database Connection Algorithm

  private ResultType resultType;

  AlgorithmType() {
  } // For Gwt

  AlgorithmType(ResultType resultType) {
    this.resultType = resultType;
  }

  public ResultType getResultType() {
    return this.resultType;
  }

}
