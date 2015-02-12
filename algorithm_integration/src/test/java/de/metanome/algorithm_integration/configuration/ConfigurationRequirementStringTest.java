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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link ConfigurationRequirementString}
 */
public class ConfigurationRequirementStringTest {

  /**
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {

  }

  /**
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {

  }

  /**
   * Test method for {@link ConfigurationRequirementString#ConfigurationRequirementString(String)}
   * <p/> The identifier should be set in the constructor and be retrievable through getIdentifier.
   * The numberOfValues should be set to 1.
   */
  @Test
  public void testConstructorGetOne() {
    // Setup
    // Expected values
    String expectedIdentifier = "parameter1";
    int expectedNumberOfValues = 1;
    ConfigurationRequirementString
        configSpec =
        new ConfigurationRequirementString(expectedIdentifier);

    // Execute functionality
    String actualIdentifier = configSpec.getIdentifier();
    int actualNumberOfValues = configSpec.getMinNumberOfSettings();

    // Check result
    assertEquals(expectedIdentifier, actualIdentifier);
    assertEquals(expectedNumberOfValues, actualNumberOfValues);
    assertTrue(configSpec.isFixNumberOfSettings());
  }

  /**
   * Test method for {@link ConfigurationRequirementString#ConfigurationRequirementString(String,
   * int)} <p/> The identifier should be set in the constructor and be retrievable through
   * getIdentifier. The numberOfValues should be set to 2.
   */
  @Test
  public void testConstructorGetTwo() {
    // Setup
    // Expected values
    String expectedIdentifier = "parameter1";
    int expectedNumberOfValues = 2;
    ConfigurationRequirementString
        configSpec =
        new ConfigurationRequirementString(expectedIdentifier, expectedNumberOfValues);

    // Execute functionality
    String actualIdentifier = configSpec.getIdentifier();
    int actualNumberOfValues = configSpec.getMinNumberOfSettings();

    // Check result
    assertEquals(expectedIdentifier, actualIdentifier);
    assertEquals(expectedNumberOfValues, actualNumberOfValues);
    assertTrue(configSpec.isFixNumberOfSettings());
  }


  /**
   * Test method for {@link ConfigurationRequirementString#ConfigurationRequirementString(String,
   * int, int)} <p/> The identifier should be set in the constructor and be retrievable through
   * getIdentifier. The numberOfValues should be set to the range (2, 4).
   */
  @Test
  public void testConstructorGetRange() {
    // Setup
    // Expected values
    String expectedIdentifier = "parameter1";
    int expectedMinNumberOfValues = 2;
    int expectedMaxNumberOfValues = 4;
    ConfigurationRequirementString
        configSpec =
        new ConfigurationRequirementString(expectedIdentifier, expectedMinNumberOfValues, expectedMaxNumberOfValues);

    // Execute functionality
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
   * Test method for {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementString#checkAndSetSettings(ConfigurationSettingString...)}
   *
   * Setting a wrong number of settings should throw an Exception.
   */
  @Test(expected=AlgorithmExecutionException.class)
  public void testSetSettingsWithWrongNumber() throws AlgorithmConfigurationException {
    // Setup
    ConfigurationRequirementString
        configSpec =
        new ConfigurationRequirementString("parameter1", 2);
    // Expected values
    ConfigurationSettingString expectedValue = mock(ConfigurationSettingString.class);

    // Execute functionality
    configSpec.checkAndSetSettings(expectedValue);
  }

  /**
   * Test method for {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementString#checkAndSetSettings(ConfigurationSettingString...)}
   *
   * Setting a wrong number of settings should throw an Exception.
   */
  @Test(expected=AlgorithmExecutionException.class)
  public void testSetSettingsWithWrongNumberRange() throws AlgorithmConfigurationException {
    // Setup
    ConfigurationRequirementString
        configSpec =
        new ConfigurationRequirementString("parameter1", 2, 4);
    // Expected values
    ConfigurationSettingString expectedValue = mock(ConfigurationSettingString.class);

    // Execute functionality
    configSpec.checkAndSetSettings(expectedValue);
  }

  /**
   * Test method for {@link ConfigurationRequirementString#checkAndSetDefaultValues(String...)}
   */
  @Test
  public void testSetDefaultValues() throws AlgorithmConfigurationException {
    // Setup
    ConfigurationRequirementString
        specificationString =
        new ConfigurationRequirementString("parameter1", 2);

    ConfigurationSettingString expectedSetting1 = new ConfigurationSettingString();
    ConfigurationSettingString expectedSetting2 = new ConfigurationSettingString();
    specificationString.checkAndSetSettings(expectedSetting1, expectedSetting2);

    // Expected values
    String expectedString1 = "first";
    String expectedString2 = "second";

    // Execute functionality
    specificationString.checkAndSetDefaultValues(expectedString1, expectedString2);
    ConfigurationSettingString[] actualSettings = specificationString.getSettings();

    // Check results
    assertEquals(expectedString1, actualSettings[0].getValue());
    assertEquals(expectedString2, actualSettings[1].getValue());
  }



  /**
   * Test method for {@link ConfigurationRequirementString#checkAndSetDefaultValues(String...)}
   */
  @Test(expected = AlgorithmConfigurationException.class)
  public void testSetDefaultValuesException() throws AlgorithmConfigurationException {
    // Setup
    ConfigurationRequirementString
        specificationString =
        new ConfigurationRequirementString("parameter1", 2);

    ConfigurationSettingString expectedSetting1 = new ConfigurationSettingString();
    ConfigurationSettingString expectedSetting2 = new ConfigurationSettingString();
    specificationString.checkAndSetSettings(expectedSetting1, expectedSetting2);

    // Execute functionality
    specificationString.checkAndSetDefaultValues("test");
  }

  /**
   * Test method for {@link ConfigurationRequirementString#getDefaultValue(int)}
   *
   * The default values should be accessible via an index.
   */
  @Test
  public void testGetDefaultValues() {
    // Setup
    ConfigurationRequirementString
        configSpec =
        new ConfigurationRequirementString("parameter1", 2);
    String expectedValue = "second";

    // Execute functionality
    try {
      configSpec.checkAndSetDefaultValues(expectedValue, "some");
    } catch (AlgorithmConfigurationException e) {
      fail();
    }

    // Check result
    assertEquals(expectedValue, configSpec.getDefaultValue(0));
    assertEquals(null, configSpec.getDefaultValue(2));
  }

  /**
   * Tests that the instances of {@link ConfigurationRequirementString}
   * are serializable in GWT.
   */
  @Test
  public void testGwtSerialization() {
    GwtSerializationTester
        .checkGwtSerializability(new ConfigurationRequirementString("some identifier", 3));
  }
}
