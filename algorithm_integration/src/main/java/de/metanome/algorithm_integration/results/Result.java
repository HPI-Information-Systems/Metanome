/**
 * Copyright 2014-2017 by Metanome Project
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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlTransient;

/**
 * All Results need to be sendable to an {@link OmniscientResultReceiver}.
 *
 * @author Jakob Zwiener
 */
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = BasicStatistic.class, name = "BasicStatistic"),
  @JsonSubTypes.Type(value = ConditionalUniqueColumnCombination.class, name = "ConditionalUniqueColumnCombination"),
  @JsonSubTypes.Type(value = FunctionalDependency.class, name = "FunctionalDependency"),
  @JsonSubTypes.Type(value = ConditionalInclusionDependency.class, name = "ConditionalInclusionDependency"),
  @JsonSubTypes.Type(value = InclusionDependency.class, name = "InclusionDependency"),
  @JsonSubTypes.Type(value = OrderDependency.class, name = "OrderDependency"),
  @JsonSubTypes.Type(value = UniqueColumnCombination.class, name = "UniqueColumnCombination"),
  @JsonSubTypes.Type(value = MultivaluedDependency.class, name = "MultivaluedDependency"),
  @JsonSubTypes.Type(value = DenialConstraint.class, name = "DenialConstraint")
})
public interface Result extends Serializable {

  /**
   * Sends a result to an {@link OmniscientResultReceiver}.
   *
   * @param resultReceiver the {@link de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver}
   *                       the result should send itself to
   * @throws CouldNotReceiveResultException if the result could not be received
   * @throws ColumnNameMismatchException if the column names of the result does not match with the column names from the input
   */
  @XmlTransient
  public void sendResultTo(OmniscientResultReceiver resultReceiver)
    throws CouldNotReceiveResultException, ColumnNameMismatchException;

}
