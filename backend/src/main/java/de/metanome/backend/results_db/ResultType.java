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

public enum ResultType implements Serializable {

  STAT("_stats", "Basic Statistic"),
  FD("_fds", "Functional Dependency"),
  MD("_mds", "Matching Dependency"),
  CFD("_cfd", "Conditional Functional Dependency"),
  UCC("_uccs", "Unique Column Combination"),
  CUCC("_cuccs", "Conditional Unique Column Combination"),
  IND("_inds", "Inclusion Dependency"),
  OD("_ods", "Order Dependency"),
  MVD("_mvds", "Multivalued Dependency"),
  DC("_dcs", "Denial Constraint");

  private String ending;
  private String name;

  ResultType(String ending, String name) {
    this.name = name;
    this.ending = ending;
  }

  public String getEnding() {
    return this.ending;
  }

  public String getName() {
    return this.name;
  }

}

