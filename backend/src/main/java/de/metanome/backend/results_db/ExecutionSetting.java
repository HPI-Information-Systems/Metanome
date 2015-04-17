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

import com.fasterxml.jackson.core.JsonProcessingException;

import de.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.metanome.algorithm_integration.input.FileInputGenerator;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.algorithm_integration.input.TableInputGenerator;
import de.metanome.algorithm_integration.results.JsonConverter;
import de.metanome.backend.helper.FileInputGeneratorMixIn;
import de.metanome.backend.helper.RelationalInputGeneratorMixIn;
import de.metanome.backend.helper.TableInputGeneratorMixIn;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlTransient;

@Entity
public class ExecutionSetting{

  protected long id;
  protected List<String> parameterValuesJson;
  protected List<String> inputsJson;
  protected String executionIdentifier;

  //Todo:find better solution - no strict length limit or similiar?
  @XmlTransient
  @ElementCollection
  @Column(columnDefinition = "VARCHAR(10000)")
  public List<String> getParameterValuesJson() {
    return parameterValuesJson;
  }

  public void setParameterValuesJson(List<String> parameterValuesJson) {
    this.parameterValuesJson = parameterValuesJson;
  }

  @XmlTransient
  @ElementCollection
  @Column(columnDefinition = "VARCHAR(10000)")
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

  /**
   * Exists for hibernate serialization
   */
  protected ExecutionSetting(){}

  public ExecutionSetting(List<ConfigurationValue> parameterValues, List<Input> inputs, String executionIdentifier) {
    JsonConverter<ConfigurationValue> jsonConverter = new JsonConverter<ConfigurationValue>();
    jsonConverter.addMixIn(FileInputGenerator.class, FileInputGeneratorMixIn.class);
    jsonConverter.addMixIn(TableInputGenerator.class, TableInputGeneratorMixIn.class);
    jsonConverter.addMixIn(RelationalInputGenerator.class, RelationalInputGeneratorMixIn.class);
    List<String> parameterValuesJson = new ArrayList<String>();
    try {
      //Todo: findOut classes in List
      for(ConfigurationValue config : parameterValues){
        parameterValuesJson.add(jsonConverter.toJsonString(config));
      }
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    this.parameterValuesJson = parameterValuesJson;

    List<String> inputsJson = new ArrayList<String>();
    JsonConverter<Input> jsonConverterInput = new JsonConverter<Input>();
    for(Input input: inputs){
      try {
        inputsJson.add(jsonConverterInput.toJsonString(input));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
    }
    this.inputsJson = inputsJson;
    this.executionIdentifier = executionIdentifier;
  }

  @Id
  @GeneratedValue
  public long getId() { return id; }

  public ExecutionSetting setId(long id) {
    this.id = id;

    return this;
  }


}
