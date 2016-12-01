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
package de.metanome.algorithm_integration.configuration;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

/**
 * A ConfigurationSetting represents the actual values of a {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirement}.
 * The values, the algorithm needed for the execution, are set in the frontend and stored in the
 * setting.
 */
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = ConfigurationSettingFileInput.class, name = "ConfigurationSettingFileInput"),
  @JsonSubTypes.Type(value = ConfigurationSettingTableInput.class, name = "ConfigurationSettingTableInput"),
  @JsonSubTypes.Type(value = ConfigurationSettingDatabaseConnection.class, name = "ConfigurationSettingDatabaseConnection"),
  @JsonSubTypes.Type(value = ConfigurationSettingBoolean.class, name = "ConfigurationSettingBoolean"),
  @JsonSubTypes.Type(value = ConfigurationSettingInteger.class, name = "ConfigurationSettingInteger"),
  @JsonSubTypes.Type(value = ConfigurationSettingListBox.class, name = "ConfigurationSettingListBox"),
  @JsonSubTypes.Type(value = ConfigurationSettingCheckBox.class, name = "ConfigurationSettingCheckBox"),
  @JsonSubTypes.Type(value = ConfigurationSettingString.class, name = "ConfigurationSettingString")
})
public abstract class ConfigurationSetting implements Serializable {

  private static final long serialVersionUID = 8033533258688078511L;

  public ConfigurationSetting() {
  }

}
