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

package de.metanome.frontend.client.parameter;

import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementBoolean;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementInteger;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementListBox;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementString;
import de.metanome.frontend.client.TabWrapper;

public class WidgetFactory {

  /**
   * Returns an InputParameterWidget depending on the given ConfigurationSpecification
   *
   * @param config          the configuration specification
   * @param messageReceiver the tab wrapper
   * @return the corresponding widget to the configuration specification
   */
  public static InputParameterWidget buildWidget(ConfigurationRequirement config,
                                                 TabWrapper messageReceiver) {
    InputParameterWidget widget = null;
    if (config instanceof ConfigurationRequirementBoolean) {
      widget =
          new InputParameterBooleanWidget((ConfigurationRequirementBoolean) config,
                                          messageReceiver);
    } else if (config instanceof ConfigurationRequirementString) {
      widget =
          new InputParameterStringWidget((ConfigurationRequirementString) config,
                                         messageReceiver);
    } else if (config instanceof ConfigurationRequirementFileInput) {
      widget =
          new InputParameterCsvFileWidget((ConfigurationRequirementFileInput) config,
                                          messageReceiver);
    } else if (config instanceof ConfigurationRequirementDatabaseConnection) {
      widget =
          new InputParameterSqlIteratorWidget((ConfigurationRequirementDatabaseConnection) config,
                                              messageReceiver);
    } else if (config instanceof ConfigurationRequirementInteger) {
      widget =
          new InputParameterIntegerWidget((ConfigurationRequirementInteger) config,
                                          messageReceiver);
    } else if (config instanceof ConfigurationRequirementListBox) {
      widget =
          new InputParameterListBoxWidget((ConfigurationRequirementListBox) config,
                                          messageReceiver);
    }
    return widget;
  }
}
