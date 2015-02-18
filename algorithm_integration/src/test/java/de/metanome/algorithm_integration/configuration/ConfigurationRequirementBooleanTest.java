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

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.test_helper.GwtSerializationTester;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link ConfigurationRequirementBoolean}
 */
public class ConfigurationRequirementBooleanTest {

  /**
   * Test method for {@link ConfigurationRequirementBoolean#checkAndSetSettings(ConfigurationSettingBoolean...)}
   *
   * The values should be correctly settable on the specification.
   */
  @Test
  public void testSetValues() {
    // Setup
    ConfigurationRequirementBoolean
        configSpec =
        new ConfigurationRequirementBoolean("parameter1", 2);
    // Expected values
    ConfigurationSettingBoolean expectedValue0 = mock(ConfigurationSettingBoolean.class);
    ConfigurationSettingBoolean expectedValue1 = mock(ConfigurationSettingBoolean.class);

    // Execute functionality
    try {
      configSpec.checkAndSetSettings(expectedValue0, expectedValue1);
    } catch (AlgorithmConfigurationException e) {
      fail();
    }

    // Check result
    assertEquals(expectedValue0, configSpec.settings[0]);
    assertEquals(expectedValue1, configSpec.settings[1]);
  }

  /**
   * Test method for {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementDefaultValue<Boolean>#checkAndSetDefaultValues(Boolean...)}
   *
   * The default values should be correctly settable on the specification.
   */
  @Test
  public void testSetDefaultValues() {
    // Setup
    ConfigurationRequirementBoolean
        configSpec =
        new ConfigurationRequirementBoolean("parameter1", 2);
    // Expected values
    ConfigurationSettingBoolean expectedValue0 = new ConfigurationSettingBoolean();
    ConfigurationSettingBoolean expectedValue1 = new ConfigurationSettingBoolean();

    try {
      configSpec.checkAndSetSettings(expectedValue0, expectedValue1);
    } catch (AlgorithmConfigurationException e) {
      fail();
    }

    // Execute functionality
    try {
      configSpec.checkAndSetDefaultValues(true, false);
    } catch (AlgorithmConfigurationException e) {
      fail();
    }

    // Check result
    assertEquals(true, configSpec.settings[0].value);
    assertEquals(false, configSpec.settings[1].value);
  }

  /**
   * Test method for {@link ConfigurationRequirementBoolean#checkAndSetSettings(ConfigurationSettingBoolean...)}
   *
   * The values should be correctly settable on the specification.
   */
  @Test
  public void testSetValuesRange() {
    // Setup
    ConfigurationRequirementBoolean
        configSpec =
        new ConfigurationRequirementBoolean("parameter1", 2, 4);
    // Expected values
    ConfigurationSettingBoolean expectedValue0 = mock(ConfigurationSettingBoolean.class);
    ConfigurationSettingBoolean expectedValue1 = mock(ConfigurationSettingBoolean.class);
    ConfigurationSettingBoolean expectedValue2 = mock(ConfigurationSettingBoolean.class);
    ConfigurationSettingBoolean expectedValue3 = mock(ConfigurationSettingBoolean.class);

    // Execute functionality
    try {
      configSpec.checkAndSetSettings(expectedValue0, expectedValue1, expectedValue2, expectedValue3);
    } catch (AlgorithmConfigurationException e) {
      fail();
    }

    // Check result
    assertEquals(expectedValue0, configSpec.settings[0]);
  }

  /**
   * Test method for {@link ConfigurationRequirementBoolean#checkAndSetSettings(ConfigurationSettingBoolean...)}
   *
   * Setting a wrong number of settings should throw an Exception.
   */
  @Test(expected=AlgorithmExecutionException.class)
  public void testSetValuesWithWrongNumber() throws AlgorithmConfigurationException {
    // Setup
    ConfigurationRequirementBoolean
        configSpec =
        new ConfigurationRequirementBoolean("parameter1", 2);
    // Expected values
    ConfigurationSettingBoolean expectedValue = mock(ConfigurationSettingBoolean.class);

    // Execute functionality
    configSpec.checkAndSetSettings(expectedValue);
  }

  /**
   * Test method for {@link ConfigurationRequirementBoolean#checkAndSetSettings(ConfigurationSettingBoolean...)}
   *
   * Setting a wrong number of settings should throw an Exception.
   */
  @Test(expected=AlgorithmExecutionException.class)
  public void testSetValuesWithWrongNumberRange() throws AlgorithmConfigurationException {
    // Setup
    ConfigurationRequirementBoolean
        configSpec =
        new ConfigurationRequirementBoolean("parameter1", 2, 4);
    // Expected values
    ConfigurationSettingBoolean expectedValue = mock(ConfigurationSettingBoolean.class);

    // Execute functionality
    configSpec.checkAndSetSettings(expectedValue);
  }

  /**
   * Tests that the instances of {@link ConfigurationRequirementBoolean}
   * are serializable in GWT.
   */
  @Test
  public void testGwtSerialization() {
    GwtSerializationTester
        .checkGwtSerializability(new ConfigurationRequirementBoolean("some identifier", 3));
  }
}
