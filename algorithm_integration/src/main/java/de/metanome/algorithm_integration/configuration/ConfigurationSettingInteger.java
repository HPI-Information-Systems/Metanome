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

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * The setting of a {@link ConfigurationRequirementInteger}
 */
@JsonTypeName("ConfigurationSettingInteger")
public class ConfigurationSettingInteger extends ConfigurationSettingPrimitive<Integer> {

  private static final long serialVersionUID = 2660565657446501400L;

  // Needed for restful serialization
  public String type = "ConfigurationSettingInteger";

  public ConfigurationSettingInteger() {
  }

  public ConfigurationSettingInteger(Integer value) {
    super(value);
  }

}

