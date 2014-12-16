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
    int actualNumberOfValues = configSpec.getNumberOfSettings();

    // Check result
    assertEquals(expectedIdentifier, actualIdentifier);
    assertEquals(expectedNumberOfValues, actualNumberOfValues);
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
    int actualNumberOfValues = configSpec.getNumberOfSettings();

    // Check result
    assertEquals(expectedIdentifier, actualIdentifier);
    assertEquals(expectedNumberOfValues, actualNumberOfValues);
  }

  /**
   * Test method for {@link ConfigurationRequirementBoolean#chackAndSetSettings(ConfigurationSettingBoolean...)}
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
      configSpec.chackAndSetSettings(expectedValue0, expectedValue1);
    } catch (AlgorithmConfigurationException e) {
      fail();
    }

    // Check result
    assertEquals(expectedValue0, configSpec.settings[0]);
    assertEquals(expectedValue1, configSpec.settings[1]);
  }

  /**
   * Test method for {@link ConfigurationRequirementBoolean#chackAndSetSettings(ConfigurationSettingBoolean...)}
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
    configSpec.chackAndSetSettings(expectedValue);
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
