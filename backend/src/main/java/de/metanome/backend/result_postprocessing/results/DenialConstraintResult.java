/**
 * Copyright 2017 by Metanome Project
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

import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import de.metanome.algorithm_integration.results.DenialConstraint;

@JsonTypeName("DenialConstraintResult")
public class DenialConstraintResult implements RankingResult {
  // Original result
  protected DenialConstraint result;


  // Needed for serialization
  public DenialConstraintResult() {}

  public DenialConstraintResult(DenialConstraint result) {
    this.result = result;
  }

  public DenialConstraint getResult() {
    return result;
  }

  public void setResult(DenialConstraint result) {
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
    DenialConstraintResult other = (DenialConstraintResult) obj;
    return this.result.equals(other.getResult());
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 11).append(this.result).toHashCode();
  }
}
