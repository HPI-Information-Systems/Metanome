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

/**
 * A ConfigurationSettingPrimitive represents those settings, which store simple data types, such as
 * String, Boolean or Integer.
 */
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = ConfigurationSettingBoolean.class, name = "ConfigurationSettingBoolean"),
  @JsonSubTypes.Type(value = ConfigurationSettingInteger.class, name = "ConfigurationSettingInteger"),
  @JsonSubTypes.Type(value = ConfigurationSettingListBox.class, name = "ConfigurationSettingListBox"),
  @JsonSubTypes.Type(value = ConfigurationSettingString.class, name = "ConfigurationSettingString"),
  @JsonSubTypes.Type(value = ConfigurationSettingCheckBox.class, name = "ConfigurationSettingCheckBox")
})
public abstract class ConfigurationSettingPrimitive<T> extends ConfigurationSetting {

  private static final long serialVersionUID = 6622779246769767913L;

  public T value;

  public ConfigurationSettingPrimitive() {
  }

  public ConfigurationSettingPrimitive(T value) {
    this.value = value;
  }

  public T getValue() {
    return value;
  }

  public void setValue(T value) {
    this.value = value;
  }

}
