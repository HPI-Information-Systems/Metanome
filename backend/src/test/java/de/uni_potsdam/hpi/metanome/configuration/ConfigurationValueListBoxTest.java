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

package de.uni_potsdam.hpi.metanome.configuration;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.ListBoxParameterAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationListBox;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ConfigurationValueListBoxTest {

  /**
   * Test method for {@link ConfigurationValueListBox#triggerSetValue(de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm,
   * java.util.Set)} <p/> Parameters should be set on the algorithm through triggerSetValue. This is
   * the last call in a double dispatch call to determine the parameters type.
   */
  @Test
  public void testTriggerSetValue() throws AlgorithmConfigurationException {
    // Setup
    ListBoxParameterAlgorithm algorithm = mock(ListBoxParameterAlgorithm.class);
    Set<Class<?>> interfaces = new HashSet<>();
    interfaces.add(ListBoxParameterAlgorithm.class);

    // Expected values
    String expectedIdentifier = "configId1";
    String[] expectedConfigurationValue = new String[1];
    expectedConfigurationValue[0] = "column1";
    ArrayList<String> expectedValues = new ArrayList<String>();

    // Execute functionality
    ConfigurationValueListBox configValue = new ConfigurationValueListBox(
        new ConfigurationSpecificationListBox(expectedIdentifier, expectedValues).getIdentifier(),
        expectedConfigurationValue);
    configValue.triggerSetValue(algorithm, interfaces);

    // Check result
    verify(algorithm).setListBoxConfigurationValue(expectedIdentifier, expectedConfigurationValue);
  }

}
