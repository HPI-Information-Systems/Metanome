/*
 * Copyright 2014 by the Metanome project
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

import com.google.common.annotations.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.input.RelationalInputGeneratorInitializer;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * Allows initialization of the input through double dispatch. The input can be generated from bot a file or databse table.
 *
 * @author Jakob Zwiener
 */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
                  @JsonSubTypes.Type(value = ConfigurationSettingFileInput.class, name = "configurationSettingFileInput"),
                  @JsonSubTypes.Type(value = ConfigurationSettingTableInput.class, name = "configurationSettingTableInput")
              })
public interface ConfigurationSettingRelationalInput extends ConfigurationSettingDataSource, IsSerializable {

  /**
   * Sends itself back to the initializer (double dispatch).
   *
   * @param initializer the initializer to send the setting to
   * @throws AlgorithmConfigurationException if the input cannot be initialized
   */
  @GwtIncompatible("Can only be called from backend.")
  public abstract void generate(RelationalInputGeneratorInitializer initializer)
      throws AlgorithmConfigurationException;

}
