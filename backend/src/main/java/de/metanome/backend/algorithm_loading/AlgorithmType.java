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

package de.metanome.backend.algorithm_loading;

import de.metanome.backend.result_receiver.ResultType;

public enum AlgorithmType {

  functionalDependency(ResultType.fd),
  inclusionDependency(ResultType.ind),
  uniqueColumnCombination(ResultType.ucc),
  conditionalUniqueColumnCombination(ResultType.cucc),
  orderDependency(ResultType.od),
  basicStatistic(ResultType.stat),
  tempFile(null),
  progressEstimating(null),
  relationalInput(null),
  fileInput(null),
  tableInput(null),
  databaseConnection(null);

  private ResultType resultType;

  AlgorithmType(ResultType resultType){
    this.resultType = resultType;
  }

  public ResultType getResultType(){ return this.resultType; }

}
