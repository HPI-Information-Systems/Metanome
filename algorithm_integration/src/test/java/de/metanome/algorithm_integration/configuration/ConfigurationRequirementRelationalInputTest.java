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
import de.metanome.test_helper.GwtSerializationTester;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link ConfigurationRequirementRelationalInput}
 *
 * @author Jakob Zwiener
 */
public class ConfigurationRequirementRelationalInputTest {

  /**
   * Test method for {@link ConfigurationRequirementRelationalInput#ConfigurationRequirementRelationalInput(String)}
   *
   * The identifier should be set in the constructor and be retrievable through getIdentifier. The
   * numberOfValues should be set to 1.
   */
  @Test
  public void testConstructorGetOne() {
    // Setup
    // Expected values
    String expectedIdentifier = "some identifier";
    int expectedNumberOfValues = 1;

    // Execute functionality
    ConfigurationRequirementRelationalInput
        configSpec =
        new ConfigurationRequirementRelationalInput(expectedIdentifier);
    String actualIdentifier = configSpec.getIdentifier();
    int actualNumberOfValues = configSpec.getNumberOfSettings();

    // Check result
    assertEquals(expectedIdentifier, actualIdentifier);
    assertEquals(expectedNumberOfValues, actualNumberOfValues);
  }

  /**
   * Test method for {@link ConfigurationRequirementRelationalInput#ConfigurationRequirementRelationalInput(String,
   * int)}
   *
   * The identifier should be set in the constructor and be retrievable through getIdentifier. The
   * numberOfValues should be set to 2.
   */
  @Test
  public void testConstructorGetTwo() {
    // Setup
    // Expected values
    String expectedIdentifier = "some identifier";
    int expectedNumberOfValues = 2;

    // Execute functionality
    ConfigurationRequirementRelationalInput
        configSpec =
        new ConfigurationRequirementRelationalInput(expectedIdentifier, expectedNumberOfValues);
    String actualIdentifier = configSpec.getIdentifier();
    int actualNumberOfValues = configSpec.getNumberOfSettings();

    // Check result
    assertEquals(expectedIdentifier, actualIdentifier);
    assertEquals(expectedNumberOfValues, actualNumberOfValues);
  }

  /**
   * Test method for {@link ConfigurationRequirementRelationalInput#getSettings()} and {@link
   * ConfigurationRequirementRelationalInput#setSettings(ConfigurationSettingRelationalInput...)
   */
  @Test
  public void testGetSetValues() throws AlgorithmConfigurationException {
    // Setup
    ConfigurationRequirementRelationalInput
        configSpec =
        new ConfigurationRequirementRelationalInput("parameter1", 2);
    // Expected values
    ConfigurationSettingRelationalInput
        expectedSetting0 =
        mock(ConfigurationSettingRelationalInput.class);
    ConfigurationSettingRelationalInput
        expectedSetting1 =
        mock(ConfigurationSettingRelationalInput.class);

    // Execute functionality
    configSpec.setSettings(expectedSetting0, expectedSetting1);
    List<ConfigurationSettingRelationalInput>
        actualSettings =
        Arrays.asList(configSpec.getSettings());

    // Check result
    assertThat(actualSettings, IsIterableContainingInAnyOrder
        .containsInAnyOrder(expectedSetting0, expectedSetting1));
  }

  /**
   * Test method for {@link ConfigurationRequirementRelationalInput#setSettings(ConfigurationSettingRelationalInput...)}
   *
   * When setting the wrong number of settings, false is returned.
   */
  @Test
  public void testSetValuesWrongNumberOfValues() {
    // Setup
    ConfigurationRequirementRelationalInput
        configSpec =
        new ConfigurationRequirementRelationalInput("parameter1", 2);
    // Expected values
    ConfigurationSettingRelationalInput
        expectedSetting0 =
        mock(ConfigurationSettingRelationalInput.class);
    ConfigurationSettingRelationalInput
        expectedSetting1 =
        mock(ConfigurationSettingRelationalInput.class);
    ConfigurationSettingRelationalInput
        expectedSetting2 =
        mock(ConfigurationSettingRelationalInput.class);

    // Execute functionality
    // Check result
    try {
      configSpec.setSettings(expectedSetting0);
    } catch (AlgorithmConfigurationException e) {
      // should trow an exception
    }

    try {
      configSpec.setSettings(expectedSetting0, expectedSetting1);
    } catch (AlgorithmConfigurationException e) {
      fail(); // number of settings is correct
    }

    try {
      configSpec.setSettings(expectedSetting0, expectedSetting1, expectedSetting2);
    } catch (AlgorithmConfigurationException e) {
      // should throw an exception
    }
  }


  /**
   * Tests that the instances of {@link ConfigurationRequirementRelationalInput}
   * are serializable in GWT.
   */
  @Test
  public void testGwtSerialization() {
    GwtSerializationTester
        .checkGwtSerializability(
            new ConfigurationRequirementRelationalInput("some identifier", 3));
  }
}
