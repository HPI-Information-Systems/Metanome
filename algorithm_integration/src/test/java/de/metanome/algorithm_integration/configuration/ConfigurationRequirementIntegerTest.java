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
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link ConfigurationRequirementInteger}
 */
public class ConfigurationRequirementIntegerTest {

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
   * Test method for {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementDefaultValue<Integer>#checkAndSetDefaultValues(Integer...)}
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
