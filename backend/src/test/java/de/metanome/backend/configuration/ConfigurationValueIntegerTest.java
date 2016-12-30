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
package de.metanome.backend.configuration;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.algorithm_types.IntegerParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementInteger;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingInteger;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ConfigurationValueIntegerTest {

  /**
   * Test method for {@link ConfigurationValueInteger#triggerSetValue(de.metanome.algorithm_integration.Algorithm,
   * java.util.Set)} <p/> Parameters should be set on the algorithm through triggerSetValue. This is
   * the last call in a double dispatch call to determine the parameters type.
   */
  @Test
  public void testTriggerSetValue() throws AlgorithmConfigurationException {
    // Setup
    IntegerParameterAlgorithm algorithm = mock(IntegerParameterAlgorithm.class);
    Set<Class<?>> interfaces = new HashSet<>();
    interfaces.add(IntegerParameterAlgorithm.class);
    // Expected values
    String expectedIdentifier = "configId1";
    Integer[] expectedConfigurationValue = {1, 2};

    // Execute functionality
    ConfigurationValueInteger configValue = new ConfigurationValueInteger(
      new ConfigurationRequirementInteger(expectedIdentifier).getIdentifier(),
      expectedConfigurationValue);
    configValue.triggerSetValue(algorithm, interfaces);

    // Check result
    verify(algorithm).setIntegerConfigurationValue(expectedIdentifier, expectedConfigurationValue);
  }

  /**
   * Test method for {@link de.metanome.backend.configuration.ConfigurationValueInteger#ConfigurationValueInteger(de.metanome.algorithm_integration.configuration.ConfigurationRequirementInteger)}
   * <p/>
   * The integers in the requirement should be properly stored in the value.
   */
  @Test
  public void testConstructorRequirement() throws AlgorithmConfigurationException, FileNotFoundException {
    // Expected values
    Integer[] expectedValues = {3, 6, 12309478};
    String expectedIdentifier = "some identifier";
    ConfigurationRequirement<ConfigurationSettingInteger>
      requirement =
      new ConfigurationRequirementInteger(expectedIdentifier, 3);
    requirement.checkAndSetSettings(buildSettings(expectedValues));

    // Execute functionality
    ConfigurationValueInteger actualConfigValue = new ConfigurationValueInteger(
      (ConfigurationRequirementInteger) requirement);

    // Check result
    assertArrayEquals(actualConfigValue.values, expectedValues);
  }

  private ConfigurationSettingInteger[] buildSettings(Integer[] expectedValues) {
    ConfigurationSettingInteger[] settings = new ConfigurationSettingInteger[expectedValues.length];

    for (int i = 0; i < expectedValues.length; i++) {
      settings[i] = new ConfigurationSettingInteger(expectedValues[i]);
    }

    return settings;
  }

}
