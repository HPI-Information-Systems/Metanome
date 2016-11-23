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
package de.metanome.algorithm_integration.configuration;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.input.RelationalInputGeneratorInitializer;

import javax.xml.bind.annotation.XmlTransient;


/**
 * Allows initialization of the input through double dispatch. The input can be generated from both
 * a file or database table.
 *
 * @author Jakob Zwiener
 */
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = ConfigurationSettingFileInput.class, name = "ConfigurationSettingFileInput"),
  @JsonSubTypes.Type(value = ConfigurationSettingTableInput.class, name = "ConfigurationSettingTableInput")
})
public abstract class ConfigurationSettingRelationalInput extends ConfigurationSettingDataSource {

  private static final long serialVersionUID = 1594413104605417301L;

  /**
   * Sends itself back to the initializer (double dispatch).
   *
   * @param initializer the initializer to send the setting to
   * @throws AlgorithmConfigurationException if the input cannot be initialized
   */
  @XmlTransient
  public abstract void generate(RelationalInputGeneratorInitializer initializer)
    throws AlgorithmConfigurationException;

}
