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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link ConfigurationRequirementBoolean}
 */
public class ConfigurationRequirementBooleanTest {

  /**
   * Test method for {@link ConfigurationRequirementBoolean#ConfigurationRequirementBoolean(String)}
   * <p/> The identifier should be set in the constructor and be retrievable through getIdentifier.
   * The numberOfValues should be set to 1.
   */
  @Test
  public void testConstructorGetOne() {
    // Setup
    // Expected values
    String expectedIdentifier = "parameter1";
    int expectedNumberOfValues = 1;

    // Execute functionality
    ConfigurationRequirementBoolean
        configSpec =
        new ConfigurationRequirementBoolean(expectedIdentifier);
    String actualIdentifier = configSpec.getIdentifier();
    int actualNumberOfValues = configSpec.getMinNumberOfSettings();

    // Check result
    assertEquals(expectedIdentifier, actualIdentifier);
    assertEquals(expectedNumberOfValues, actualNumberOfValues);
    assertTrue(configSpec.isFixNumberOfSettings());
  }

  /**
   * Test method for {@link ConfigurationRequirementBoolean#ConfigurationRequirementBoolean(String,
   * int)} <p/> The identifier should be set in the constructor and be retrievable through
   * getIdentifier. The numberOfValues should be set to 2.
   */
  @Test
  public void testConstructorGetTwo() {
    // Setup
    // Expected values
    String expectedIdentifier = "parameter1";
    int expectedNumberOfValues = 2;

    // Execute functionality
    ConfigurationRequirementBoolean
        configSpec =
        new ConfigurationRequirementBoolean(expectedIdentifier, expectedNumberOfValues);
    String actualIdentifier = configSpec.getIdentifier();
    int actualNumberOfValues = configSpec.getMaxNumberOfSettings();

    // Check result
    assertEquals(expectedIdentifier, actualIdentifier);
    assertEquals(expectedNumberOfValues, actualNumberOfValues);
    assertTrue(configSpec.isFixNumberOfSettings());
  }


  /**
   * Test method for {@link ConfigurationRequirementBoolean#ConfigurationRequirementBoolean(String,
   * int)} <p/> The identifier should be set in the constructor and be retrievable through
   * getIdentifier. The numberOfValues should be set to the range (2, 4).
   */
  @Test
  public void testConstructorGetRange() {
    // Setup
    // Expected values
    String expectedIdentifier = "parameter1";
    int expectedMinNumberOfValues = 2;
    int expectedMaxNumberOfValues = 4;

    // Execute functionality
    ConfigurationRequirementBoolean
        configSpec =
        new ConfigurationRequirementBoolean(expectedIdentifier, expectedMinNumberOfValues, expectedMaxNumberOfValues);
    String actualIdentifier = configSpec.getIdentifier();
    int actualMinNumberOfValues = configSpec.getMinNumberOfSettings();
    int actualMaxNumberOfValues = configSpec.getMaxNumberOfSettings();

    // Check result
    assertEquals(expectedIdentifier, actualIdentifier);
    assertEquals(expectedMinNumberOfValues, actualMinNumberOfValues);
    assertEquals(expectedMaxNumberOfValues, actualMaxNumberOfValues);
    assertFalse(configSpec.isFixNumberOfSettings());
  }

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
   * Test method for {@link ConfigurationRequirementBoolean#checkAndSetDefaultValues(Boolean...)}
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
   * Test method for {@link ConfigurationRequirementBoolean#getDefaultValue(int)}
   *
   * The default values should be accessible via an index.
   */
  @Test
  public void testGetDefaultValues() {
    // Setup
    ConfigurationRequirementBoolean
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
