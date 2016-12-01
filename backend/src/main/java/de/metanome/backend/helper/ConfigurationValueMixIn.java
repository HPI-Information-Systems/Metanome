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
package de.metanome.backend.helper;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.metanome.backend.configuration.*;


@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = ConfigurationValueBoolean.class, name = "configurationValueBoolean"),
  @JsonSubTypes.Type(value = ConfigurationValueDatabaseConnectionGenerator.class, name = "configurationValueDatabaseConnectionGenerator"),
  @JsonSubTypes.Type(value = ConfigurationValueFileInputGenerator.class, name = "configurationValueFileInputGenerator"),
  @JsonSubTypes.Type(value = ConfigurationValueInteger.class, name = "configurationValueInteger"),
  @JsonSubTypes.Type(value = ConfigurationValueListBox.class, name = "configurationValueListBox"),
  @JsonSubTypes.Type(value = ConfigurationValueCheckBox.class, name = "configurationValueCheckBox"),
  @JsonSubTypes.Type(value = ConfigurationValueRelationalInputGenerator.class, name = "configurationValueRelationalInputGenerator"),
  @JsonSubTypes.Type(value = ConfigurationValueString.class, name = "configurationValueString"),
  @JsonSubTypes.Type(value = ConfigurationValueTableInputGenerator.class, name = "configurationValueTableInputGenerator")
})
public class ConfigurationValueMixIn {

}
