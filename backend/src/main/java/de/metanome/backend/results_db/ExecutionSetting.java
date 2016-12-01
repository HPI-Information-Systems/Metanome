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
package de.metanome.backend.results_db;

import com.google.common.annotations.GwtCompatible;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.List;

/**
 * Represents ExecutionSetting in Database containing configurationValues, inputs and Execution Identifier
 */
@Entity
@GwtCompatible
public class ExecutionSetting implements Serializable {

  private static final long serialVersionUID = -3361537753189471431L;

  protected long id;
  protected List<String> parameterValuesJson;
  protected List<String> inputsJson;
  protected String executionIdentifier;
  private Boolean cacheResults = false;
  private Boolean writeResults = false;
  private Boolean countResults = false;

  /**
   * Exists for hibernate serialization
   */
  protected ExecutionSetting() {
  }

  public ExecutionSetting(List<String> parameterValuesJson, List<String> inputsJson,
                          String executionIdentifier) {
    this.parameterValuesJson = parameterValuesJson;
    this.inputsJson = inputsJson;
    this.executionIdentifier = executionIdentifier;
  }

  @XmlTransient
  @ElementCollection
  @Column(columnDefinition = "LONGVARCHAR")
  public List<String> getParameterValuesJson() {
    return parameterValuesJson;
  }

  public void setParameterValuesJson(List<String> parameterValuesJson) {
    this.parameterValuesJson = parameterValuesJson;
  }

  @XmlTransient
  @ElementCollection
  @Column(columnDefinition = "LONGVARCHAR")
  public List<String> getInputsJson() {
    return inputsJson;
  }

  public void setInputsJson(List<String> inputs) {
    this.inputsJson = inputs;
  }

  public String getExecutionIdentifier() {
    return executionIdentifier;
  }

  public void setExecutionIdentifier(String executionIdentifier) {
    this.executionIdentifier = executionIdentifier;
  }

  public Boolean getCacheResults() {
    return cacheResults;
  }

  public ExecutionSetting setCacheResults(Boolean cacheResults) {
    this.cacheResults = cacheResults;
    return this;
  }

  public Boolean getWriteResults() {
    return writeResults;
  }

  public ExecutionSetting setWriteResults(Boolean writeResults) {
    this.writeResults = writeResults;
    return this;
  }

  public Boolean getCountResults() {
    return countResults;
  }

  public ExecutionSetting setCountResults(Boolean countResults) {
    this.countResults = countResults;
    return this;
  }

  @Id
  @GeneratedValue
  public long getId() {
    return id;
  }

  public ExecutionSetting setId(long id) {
    this.id = id;

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ExecutionSetting)) {
      return false;
    }

    ExecutionSetting input = (ExecutionSetting) o;

    if (id != input.id) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }


}
