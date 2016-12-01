/**
 * Copyright 2015-2016 by Metanome Project
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
package de.metanome.backend.result_postprocessing.results;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = BasicStatisticResult.class, name = "BasicStatisticResult"),
  @JsonSubTypes.Type(value = ConditionalUniqueColumnCombinationResult.class, name = "ConditionalUniqueColumnCombinationResult"),
  @JsonSubTypes.Type(value = FunctionalDependencyResult.class, name = "FunctionalDependencyResult"),
  @JsonSubTypes.Type(value = InclusionDependencyResult.class, name = "InclusionDependencyResult"),
  @JsonSubTypes.Type(value = OrderDependencyResult.class, name = "OrderDependencyResult"),
  @JsonSubTypes.Type(value = UniqueColumnCombinationResult.class, name = "UniqueColumnCombinationResult")
})
public interface RankingResult {

}
