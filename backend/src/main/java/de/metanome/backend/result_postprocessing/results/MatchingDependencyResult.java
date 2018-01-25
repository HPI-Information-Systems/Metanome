/**
 * Copyright 2015-2016 by Metanome Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.metanome.backend.result_postprocessing.results;

import com.fasterxml.jackson.annotation.JsonTypeName;
import de.metanome.algorithm_integration.results.MatchingDependency;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents an matching dependency result with different ranking values.
 */
@JsonTypeName("MatchingDependencyResult")
public class MatchingDependencyResult implements RankingResult {

  // Original result
  protected MatchingDependency result;

  // Needed for serialization
  public MatchingDependencyResult() {
  }

  public MatchingDependencyResult(MatchingDependency result) {
    this.result = result;
  }

  public MatchingDependency getResult() {
    return this.result;
  }

  public void setResult(MatchingDependency result) {
    this.result = result;
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    MatchingDependencyResult other = (MatchingDependencyResult) obj;
    return this.result.equals(other.getResult());
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 11).
        append(this.result).
        toHashCode();
  }

}
