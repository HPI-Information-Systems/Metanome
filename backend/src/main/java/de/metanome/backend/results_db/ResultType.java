/**
 * Copyright 2015-2025 by Metanome Project
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

import de.metanome.algorithm_integration.results.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.stream.Stream;

public enum ResultType implements Serializable {

  BASIC_STAT("_stats", "Basic Statistic", BasicStatistic.class),
  FD("_fds", "Functional Dependency", FunctionalDependency.class),
  MD("_mds", "Matching Dependency", MatchingDependency.class),
  CFD("_cfd", "Conditional Functional Dependency", ConditionalFunctionalDependency.class),
  UCC("_uccs", "Unique Column Combination", UniqueColumnCombination.class),
  CUCC("_cuccs", "Conditional Unique Column Combination", ConditionalUniqueColumnCombination.class),
  IND("_inds", "Inclusion Dependency", InclusionDependency.class),
  OD("_ods", "Order Dependency", OrderDependency.class),
  MVD("_mvds", "Multivalued Dependency", MultivaluedDependency.class),
  DC("_dcs", "Denial Constraint", DenialConstraint.class),
  CID("_cids", "Conditional Inclusion Dependency", ConditionalInclusionDependency.class),
  RFD("_rfds", "Relaxed Functional Dependency", RelaxedFunctionalDependency.class),
  RIND("_rinds", "Relaxed Inclusion Dependency", RelaxedInclusionDependency.class),
  RUCC("_ruccs", "Relaxed Unique Column Combination", RelaxedUniqueColumnCombination.class);

  private String ending;
  private String name;
  private Class<?> resultClass;

  ResultType(String ending, String name, Class<?> resultClass) {
    this.name = name;
    this.ending = ending;
    this.resultClass = resultClass;
  }

  public String getEnding() {
    return this.ending;
  }

  public String getName() {
    return this.name;
  }
  
  public Class<?> getResultClass() {
    return this.resultClass;
  }

  public static Stream<ResultType> asStream() {
    return Arrays.asList(values()).stream();
  }
  
}

