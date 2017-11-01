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
package de.metanome.algorithm_integration.results;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import com.fasterxml.jackson.annotation.JsonIgnore;
import de.metanome.algorithm_integration.Predicate;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

public class DenialConstraint implements Result {

  private static final long serialVersionUID = -3940142594679991666L;
  public static final String NOT = "\u00AC";
  public static final String AND = "^";

  private final Set<Predicate> predicates;

  public DenialConstraint() {
    this.predicates = new HashSet<>();
  }

  public DenialConstraint(Predicate... predicates) {
    this.predicates = new HashSet<Predicate>(Arrays.asList(predicates));
  }

  @Override
  public void sendResultTo(OmniscientResultReceiver resultReceiver)
      throws CouldNotReceiveResultException, ColumnNameMismatchException {
    resultReceiver.receiveResult(this);
  }

  public Collection<Predicate> getPredicates() {
    return Collections.unmodifiableCollection(predicates);
  }
  
  @JsonIgnore
  public int getPredicateCount() {
    return predicates.size();
  }


  @Override
  public String toString() {
    return NOT + "("
        + predicates.stream().map(p -> p.toString()).sorted().collect(Collectors.joining(AND))
        + ")";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((predicates == null) ? 0 : predicates.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DenialConstraint other = (DenialConstraint) obj;
    if (predicates == null) {
      if (other.predicates != null)
        return false;
    } else if (!predicates.equals(other.predicates))
      return false;
    return true;
  }
}
