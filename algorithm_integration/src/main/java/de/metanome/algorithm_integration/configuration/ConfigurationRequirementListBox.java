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

import com.fasterxml.jackson.annotation.JsonTypeName;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;

import java.util.List;

import javax.xml.bind.annotation.XmlTransient;

/**
 * Concrete {@link ConfigurationRequirement} for list box of strings.
 *
 * @author Tanja Bergmann
 * @see ConfigurationRequirement
 */
@JsonTypeName("ConfigurationRequirementListBox")
public class ConfigurationRequirementListBox
    extends ConfigurationRequirementDefaultValue<String, ConfigurationSettingListBox> {

  // Needed for restful serialization
  public String type = "ConfigurationRequirementListBox";

  private List<String> values;

  public ConfigurationRequirementListBox() {
  }

  public ConfigurationRequirementListBox(String identifier, List<String> values) {
    super(identifier);
    this.values = values;
  }

  public ConfigurationRequirementListBox(String identifier, List<String> values,
                                         int numberOfSettings) {
    super(identifier, numberOfSettings);
    this.values = values;
  }

  public ConfigurationRequirementListBox(String identifier, List<String> values,
                                         int minNumberOfSetting, int maxNumberOfSetting) {
    super(identifier, minNumberOfSetting, maxNumberOfSetting);
    this.values = values;
  }

  public List<String> getValues() {
    return values;
  }

  public void setValues(List<String> values) {
    this.values = values;
  }

  /**
   * {@inheritDoc}
   */
  @XmlTransient
  @Override
  @GwtIncompatible("ConfigurationValues cannot be build on client side.")
  public ConfigurationValue build(ConfigurationFactory factory)
      throws AlgorithmConfigurationException {
    return factory.build(this);
  }

}
