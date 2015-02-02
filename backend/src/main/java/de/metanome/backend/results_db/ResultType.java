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

import de.metanome.algorithm_integration.results.*;
import de.metanome.algorithm_integration.results.Result;
import de.metanome.backend.result_receiver.ResultHandler;

import java.io.Serializable;

public enum ResultType implements Serializable {

  STAT("_stats", "Basic Statistic", new ResultHandler() {
    @Override
    public Result convert(String str) {
      return BasicStatistic.fromString(str);
    }
  }),
  FD("_fds", "Functional Dependency", new ResultHandler() {
    @Override
    public Result convert(String str) {
      return FunctionalDependency.fromString(str);
    }
  }),
  UCC("_uccs", "Unique Column Combination", new ResultHandler() {
    @Override
    public Result convert(String str) {
      return UniqueColumnCombination.fromString(str);
    }
  }),
  CUCC("_cuccs", "Conditional Unique Column Combination", new ResultHandler() {
    @Override
    public Result convert(String str) {
      return ConditionalUniqueColumnCombination.fromString(str);
    }
  }),
  IND("_inds", "Inclusion Dependency", new ResultHandler() {
    @Override
    public Result convert(String str) {
      return InclusionDependency.fromString(str);
    }
  }),
  OD("_ods", "Order Dependency", new ResultHandler() {
    @Override
    public Result convert(String str) {
      return OrderDependency.fromString(str);
    }
  });

  private String ending;
  private String name;
  private ResultHandler resultHandler;

  ResultType() {
  } // For GWT

  ResultType(String ending, String name, ResultHandler resultHandler) {
    this.name = name;
    this.ending = ending;
    this.resultHandler = resultHandler;
  }

  public String getEnding() {
    return this.ending;
  }

  public String getName() {
    return this.name;
  }

  public ResultHandler getResultHandler() {
    return this.resultHandler;
  }

}

