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

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link ConfigurationRequirementInteger}
 */
public class ConfigurationRequirementIntegerTest {

  /**
   * Test method for {@link ConfigurationRequirementInteger#ConfigurationRequirementInteger(String)}
   * <p/> The identifier should be set in the constructor and be retrievable through getIdentifier.
   * The numberOfValues should be set to 1.
   */
  @Test
  public void testConstructorGetOne() {
    // Setup
    // Expected values
    String expectedIdentifier = "parameter1";
    int expectedNumberOfValues = 1;
    ConfigurationRequirementInteger
        configSpec =
        new ConfigurationRequirementInteger(expectedIdentifier);

    // Execute functionality
    String actualIdentifier = configSpec.getIdentifier();
    int actualNumberOfValues = configSpec.getMaxNumberOfSettings();

    // Check result
    assertEquals(expectedIdentifier, actualIdentifier);
    assertEquals(expectedNumberOfValues, actualNumberOfValues);
    assertTrue(configSpec.hasFixNumberOfSettings());
  }

  /**
   * Test method for {@link ConfigurationRequirementInteger#ConfigurationRequirementInteger(String,
   * int)} <p/> The identifier should be set in the constructor and be retrievable through
   * getIdentifier. The numberOfValues should be set to 2.
   */
  @Test
  public void testConstructorGetTwo() {
    // Setup
    // Expected values
    String expectedIdentifier = "parameter1";
    int expectedNumberOfValues = 2;
    ConfigurationRequirementInteger
        configSpec =
        new ConfigurationRequirementInteger(expectedIdentifier, expectedNumberOfValues);

    // Execute functionality
    String actualIdentifier = configSpec.getIdentifier();
    int actualNumberOfValues = configSpec.getMinNumberOfSettings();

    // Check result
    assertEquals(expectedIdentifier, actualIdentifier);
    assertEquals(expectedNumberOfValues, actualNumberOfValues);
    assertTrue(configSpec.hasFixNumberOfSettings());
  }

  /**
   * Test method for {@link ConfigurationRequirementInteger#ConfigurationRequirementInteger(String,
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
    ConfigurationRequirementInteger
        configSpec =
        new ConfigurationRequirementInteger(expectedIdentifier, expectedMinNumberOfValues, expectedMaxNumberOfValues);

    // Execute functionality
    String actualIdentifier = configSpec.getIdentifier();
    int actualMinNumberOfValues = configSpec.getMinNumberOfSettings();
    int actualMaxNumberOfValues = configSpec.getMaxNumberOfSettings();

    // Check result
    assertEquals(expectedIdentifier, actualIdentifier);
    assertEquals(expectedMinNumberOfValues, actualMinNumberOfValues);
    assertEquals(expectedMaxNumberOfValues, actualMaxNumberOfValues);
    assertFalse(configSpec.hasFixNumberOfSettings());
  }

  /**
   * Test method for {@link ConfigurationRequirementInteger#getSettings()}
   * and {@link ConfigurationRequirementInteger#checkAndSetSettings(ConfigurationSettingInteger...)}
   */
  @Test
  public void testGetSetSpecification() throws AlgorithmConfigurationException {
    // Setup
    ConfigurationRequirementInteger
        specificationInteger =
        new ConfigurationRequirementInteger("parameter1", 2);
    // Expected values
    ConfigurationSettingInteger expectedSetting1 = new ConfigurationSettingInteger();
    ConfigurationSettingInteger expectedSetting2 = new ConfigurationSettingInteger();

    // Execute functionality
    specificationInteger.checkAndSetSettings(expectedSetting1, expectedSetting2);
    List<ConfigurationSettingInteger>
        actualSettings =
        Arrays.asList(specificationInteger.getSettings());

    // Check results
    assertThat(actualSettings, IsIterableContainingInAnyOrder
        .containsInAnyOrder(expectedSetting1, expectedSetting2));
  }

  /**
   * Test method for {@link ConfigurationRequirementInteger#checkAndSetDefaultValues(Integer...)}
   */
  @Test
  public void testSetDefaultValues() throws AlgorithmConfigurationException {
    // Setup
    ConfigurationRequirementInteger
        specificationInteger =
        new ConfigurationRequirementInteger("parameter1", 2);

    ConfigurationSettingInteger expectedSetting1 = new ConfigurationSettingInteger();
    ConfigurationSettingInteger expectedSetting2 = new ConfigurationSettingInteger();
    specificationInteger.checkAndSetSettings(expectedSetting1, expectedSetting2);

    // Expected values
    int expectedNumber1 = 8;
    int expectedNumber2 = 5;

    // Execute functionality
    specificationInteger.checkAndSetDefaultValues(expectedNumber1, expectedNumber2);
    ConfigurationSettingInteger[] actualSettings = specificationInteger.getSettings();

    // Check results
    assertEquals(expectedNumber1, actualSettings[0].getValue());
    assertEquals(expectedNumber2, actualSettings[1].getValue());
  }

  /**
   * Test method for {@link ConfigurationRequirementInteger#getDefaultValue(int)}
   *
   * The default values should be accessible via an index.
   */
  @Test
  public void testGetDefaultValues() {
    // Setup
    ConfigurationRequirementInteger
        configSpec =
        new ConfigurationRequirementInteger("parameter1", 2);
    Integer expectedValue = 5;

    // Execute functionality
    try {
      configSpec.checkAndSetDefaultValues(expectedValue, 9);
    } catch (AlgorithmConfigurationException e) {
      fail();
    }

    // Check result
    assertEquals(expectedValue, configSpec.getDefaultValue(0));
    assertEquals(null, configSpec.getDefaultValue(2));
  }

  /**
   * Test method for {@link ConfigurationRequirementInteger#getSettings()}
   * and {@link ConfigurationRequirementInteger#checkAndSetSettings(ConfigurationSettingInteger...)}
   */
  @Test
  public void testGetSetSpecificationRange() throws AlgorithmConfigurationException {
    // Setup
    ConfigurationRequirementInteger
        specificationInteger =
        new ConfigurationRequirementInteger("parameter1", 2, 4);
    // Expected values
    ConfigurationSettingInteger expectedSetting1 = new ConfigurationSettingInteger();
    ConfigurationSettingInteger expectedSetting2 = new ConfigurationSettingInteger();
    ConfigurationSettingInteger expectedSetting3 = new ConfigurationSettingInteger();

    // Execute functionality
    specificationInteger.checkAndSetSettings(expectedSetting1, expectedSetting2, expectedSetting3);
    List<ConfigurationSettingInteger>
        actualSettings =
        Arrays.asList(specificationInteger.getSettings());

    // Check results
    assertThat(actualSettings, IsIterableContainingInAnyOrder
        .containsInAnyOrder(expectedSetting1, expectedSetting2, expectedSetting3));
  }

  /**
   * Test method for {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementInteger#checkAndSetSettings(ConfigurationSettingInteger...)}
   *
   * Setting a wrong number of settings should throw an Exception.
   */
  @Test(expected=AlgorithmExecutionException.class)
  public void testSetSettingsWithWrongNumber() throws AlgorithmConfigurationException {
    // Setup
    ConfigurationRequirementInteger
        configSpec =
        new ConfigurationRequirementInteger("parameter1", 2);
    // Expected values
    ConfigurationSettingInteger expectedValue = mock(ConfigurationSettingInteger.class);

    // Execute functionality
    configSpec.checkAndSetSettings(expectedValue);
  }

  /**
   * Test method for {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementInteger#checkAndSetSettings(ConfigurationSettingInteger...)}
   *
   * Setting a wrong number of settings should throw an Exception.
   */
  @Test(expected=AlgorithmExecutionException.class)
  public void testSetSettingsWithWrongNumberRange() throws AlgorithmConfigurationException {
    // Setup
    ConfigurationRequirementInteger
        configSpec =
        new ConfigurationRequirementInteger("parameter1", 2, 4);
    // Expected values
    ConfigurationSettingInteger expectedValue = mock(ConfigurationSettingInteger.class);

    // Execute functionality
    configSpec.checkAndSetSettings(expectedValue);
  }

  /**
   * Tests that the instances of {@link ConfigurationRequirementInteger}
   * are serializable in GWT.
   */
  @Test
  public void testGwtSerialization() {
    GwtSerializationTester
        .checkGwtSerializability(new ConfigurationRequirementInteger("some identifier", 3));
  }
}
