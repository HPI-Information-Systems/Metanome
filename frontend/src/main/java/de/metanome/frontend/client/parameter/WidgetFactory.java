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

import de.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.metanome.algorithm_integration.configuration.ConfigurationSpecificationBoolean;
import de.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.metanome.algorithm_integration.configuration.ConfigurationSpecificationInteger;
import de.metanome.algorithm_integration.configuration.ConfigurationSpecificationListBox;
import de.metanome.algorithm_integration.configuration.ConfigurationSpecificationSqlIterator;
import de.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;
import de.metanome.frontend.client.TabWrapper;

public class WidgetFactory {

  /**
   * Returns an InputParameterWidget depending on the given ConfigurationSpecification
   *
   * @param config          the configuration specification
   * @param messageReceiver the tab wrapper
   * @return the corresponding widget to the configuration specification
   */
  public static InputParameterWidget buildWidget(ConfigurationSpecification config,
                                                 TabWrapper messageReceiver) {
    InputParameterWidget widget = null;
    if (config instanceof ConfigurationSpecificationBoolean) {
      widget =
          new InputParameterBooleanWidget((ConfigurationSpecificationBoolean) config,
                                          messageReceiver);
    } else if (config instanceof ConfigurationSpecificationString) {
      widget =
          new InputParameterStringWidget((ConfigurationSpecificationString) config,
                                         messageReceiver);
    } else if (config instanceof ConfigurationSpecificationCsvFile) {
      widget =
          new InputParameterCsvFileWidget((ConfigurationSpecificationCsvFile) config,
                                          messageReceiver);
    } else if (config instanceof ConfigurationSpecificationSqlIterator) {
      widget =
          new InputParameterSqlIteratorWidget((ConfigurationSpecificationSqlIterator) config,
                                              messageReceiver);
    } else if (config instanceof ConfigurationSpecificationInteger) {
      widget =
          new InputParameterIntegerWidget((ConfigurationSpecificationInteger) config,
                                          messageReceiver);
    } else if (config instanceof ConfigurationSpecificationListBox) {
      widget =
          new InputParameterListBoxWidget((ConfigurationSpecificationListBox) config,
                                          messageReceiver);
    }
    return widget;
  }
}
