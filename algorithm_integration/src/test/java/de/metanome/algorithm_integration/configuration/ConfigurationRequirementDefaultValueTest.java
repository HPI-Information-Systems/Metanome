/*
 * Copyright 2015 by the Metanome project
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

import de.metanome.algorithm_integration.AlgorithmConfigurationException;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ConfigurationRequirementDefaultValueTest {

  /**
   * Test method for {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementDefaultValue#getDefaultValue(int)}
   *
   * The default values should be accessible via an index.
   */
  @Test
  public void testGetDefaultValues() {
    // Setup
    ConfigurationRequirementDefaultValue<Boolean>
        configSpec =
        new ConfigurationRequirementBoolean("parameter1", 2);

    // Execute functionality
    try {
      configSpec.checkAndSetDefaultValues(true, false);
    } catch (AlgorithmConfigurationException e) {
      fail();
    }

    // Check result
    assertEquals(true, configSpec.getDefaultValue(0));
    assertEquals(null, configSpec.getDefaultValue(2));
  }

  /**
   * Test method for {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementDefaultValue#getDefaultValue(int)}
   *
   * The default values should be accessible via an index.
   */
  @Test(expected = AlgorithmConfigurationException.class)
  public void testGetDefaultValuesWithWrongNumber() throws AlgorithmConfigurationException {
    // Setup
    ConfigurationRequirementDefaultValue<Boolean>
        configSpec =
        new ConfigurationRequirementBoolean("parameter1", 2);

    // Execute functionality
    configSpec.checkAndSetDefaultValues(true);
  }
}
