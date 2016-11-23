/**
 * Copyright 2014-2016 by Metanome Project
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
package de.metanome.backend.resources;


import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;

import java.util.List;

public class AlgorithmExecutionParams {

  private long algorithmId;
  private String executionIdentifier;
  private List<ConfigurationRequirement> requirements;
  private Boolean cacheResults = false;
  private Boolean writeResults = false;
  private Boolean countResults = false;
  private String memory;

  public long getAlgorithmId() {
    return algorithmId;
  }

  public AlgorithmExecutionParams setAlgorithmId(long algorithmId) {
    this.algorithmId = algorithmId;
    return this;
  }

  public String getExecutionIdentifier() {
    return executionIdentifier;
  }

  public AlgorithmExecutionParams setExecutionIdentifier(String executionIdentifier) {
    this.executionIdentifier = executionIdentifier;
    return this;
  }

  public List<ConfigurationRequirement> getRequirements() {
    return requirements;
  }

  public AlgorithmExecutionParams setRequirements(List<ConfigurationRequirement> requirements) {
    this.requirements = requirements;
    return this;
  }

  public Boolean getCacheResults() {
    return cacheResults;
  }

  public AlgorithmExecutionParams setCacheResults(Boolean cacheResults) {
    this.cacheResults = cacheResults;
    return this;
  }

  public Boolean getWriteResults() {
    return writeResults;
  }

  public AlgorithmExecutionParams setWriteResults(Boolean writeResults) {
    this.writeResults = writeResults;
    return this;
  }

  public Boolean getCountResults() {
    return countResults;
  }

  public AlgorithmExecutionParams setCountResults(Boolean countResults) {
    this.countResults = countResults;
    return this;
  }

  public String getMemory() {
    return memory;
  }

  public AlgorithmExecutionParams setMemory(String memory) {
    this.memory = memory;
    return this;
  }
}
