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

import java.io.Serializable;

/**
 * The setting of a {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementListBox}
 *
 * @author Tanja Bergmann
 */
public class ConfigurationSettingListBox implements Serializable {

  public String selectedValue;

  /**
   * Exists for serialization.
   */
  public ConfigurationSettingListBox() {
  }

  public ConfigurationSettingListBox(String selectedValue) {
    this.selectedValue = selectedValue;
  }

  public String getSelectedValue() {
    return selectedValue;
  }

  public void setSelectedValue(String selectedValue) {
    this.selectedValue = selectedValue;
  }
}
