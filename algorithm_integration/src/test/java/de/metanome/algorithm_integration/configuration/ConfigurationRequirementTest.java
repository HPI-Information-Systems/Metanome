/**
 * Copyright 2015-2016 by Metanome Project
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
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ConfigurationRequirementTest {

  /**
   * Test method for {@link ConfigurationRequirementBoolean#ConfigurationRequirementBoolean(String)}
   * The identifier should be set in the constructor and be retrievable through getIdentifier. The
   * numberOfValues should be set to 1.
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
   * Test method for {@link ConfigurationRequirementInteger#ConfigurationRequirementInteger(String,
   * int)} The identifier should be set in the constructor and be retrievable through getIdentifier.
   * The numberOfValues should be set to 2.
   */
  @Test
  public void testConstructorGetTwo() {
    // Setup
    // Expected values
    String expectedIdentifier = "parameter1";
    int expectedNumberOfValues = 2;

    // Execute functionality
    ConfigurationRequirementInteger
      configSpec =
      new ConfigurationRequirementInteger(expectedIdentifier, expectedNumberOfValues);
    String actualIdentifier = configSpec.getIdentifier();
    int actualNumberOfValues = configSpec.getMaxNumberOfSettings();

    // Check result
    assertEquals(expectedIdentifier, actualIdentifier);
    assertEquals(expectedNumberOfValues, actualNumberOfValues);
    assertTrue(configSpec.isFixNumberOfSettings());
  }


  /**
   * Test method for {@link ConfigurationRequirementString#ConfigurationRequirementString(String,
   * int)} The identifier should be set in the constructor and be retrievable through getIdentifier.
   * The numberOfValues should be set to the range (2, 4).
   */
  @Test
  public void testConstructorGetRange() {
    // Setup
    // Expected values
    String expectedIdentifier = "parameter1";
    int expectedMinNumberOfValues = 2;
    int expectedMaxNumberOfValues = 4;

    // Execute functionality
    ConfigurationRequirementString
      configSpec =
      new ConfigurationRequirementString(expectedIdentifier, expectedMinNumberOfValues,
        expectedMaxNumberOfValues);
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
   * Test method for {@link ConfigurationRequirementRelationalInput#getSettings()} and {@link
   * ConfigurationRequirement<de.metanome.algorithm_integration.configuration.ConfigurationSettingRelationalInput>#checkAndSetSettings(ConfigurationSettingRelationalInput...)
   */
  @Test
  public void testGetSetSettings() throws AlgorithmConfigurationException {
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
    configSpec.checkAndSetSettings(expectedSetting0, expectedSetting1);
    List<ConfigurationSettingRelationalInput>
      actualSettings =
      Arrays.asList(configSpec.getSettings());

    // Check result
    assertThat(actualSettings, IsIterableContainingInAnyOrder
      .containsInAnyOrder(expectedSetting0, expectedSetting1));
  }

  /**
   * Test method for {@link ConfigurationRequirementRelationalInput#getSettings()} and {@link
   * ConfigurationRequirement<de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput>#checkAndSetSettings(ConfigurationSettingTableInput...)
   */
  @Test
  public void testGetSetSettingsAsRange() throws AlgorithmConfigurationException {
    // Setup
    ConfigurationRequirementTableInput
      configSpec =
      new ConfigurationRequirementTableInput("parameter1", 2, 4);
    // Expected values
    ConfigurationSettingTableInput expectedSetting0 = mock(ConfigurationSettingTableInput.class);
    ConfigurationSettingTableInput expectedSetting1 = mock(ConfigurationSettingTableInput.class);
    ConfigurationSettingTableInput expectedSetting2 = mock(ConfigurationSettingTableInput.class);

    // Execute functionality
    configSpec.checkAndSetSettings(expectedSetting0, expectedSetting1, expectedSetting2);
    List<ConfigurationSettingTableInput> actualSettings = Arrays.asList(configSpec.getSettings());

    // Check result
    assertThat(actualSettings, IsIterableContainingInAnyOrder
      .containsInAnyOrder(expectedSetting0, expectedSetting1, expectedSetting2));
  }

  /**
   * Test method for {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirement<de.metanome.algorithm_integration.configuration.ConfigurationSettingCheckBox>#checkAndSetSettings(ConfigurationSettingCheckBox...)}
   * <p/>
   * When setting the wrong number of settings, false is returned.
   */
  @Test
  public void testCheckAndSetSettingsWithWrongArray() {
    // Setup
    ConfigurationRequirementCheckBox
      configSpec =
      new ConfigurationRequirementCheckBox("parameter1", new String[3], 2);
    // Expected values
    ConfigurationSettingCheckBox expectedSetting0 = mock(ConfigurationSettingCheckBox.class);
    ConfigurationSettingCheckBox expectedSetting1 = mock(ConfigurationSettingCheckBox.class);
    ConfigurationSettingCheckBox expectedSetting2 = mock(ConfigurationSettingCheckBox.class);

    // Execute functionality
    // Check result
    try {
      configSpec.checkAndSetSettings(expectedSetting0);
    } catch (AlgorithmConfigurationException e) {
      // should throw an exception
    }

    try {
      configSpec.checkAndSetSettings(expectedSetting0, expectedSetting1);
    } catch (AlgorithmConfigurationException e) {
      fail(); // number of settings is correct
    }

    try {
      configSpec.checkAndSetSettings(expectedSetting0, expectedSetting1, expectedSetting2);
    } catch (AlgorithmConfigurationException e) {
      // should throw an exception
    }
  }

  /**
   * Test method for {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirement<de.metanome.algorithm_integration.configuration.ConfigurationSettingListBox>#checkAndSetSettings(ConfigurationSettingListBox...)}
   * <p/>
   * When setting the wrong number of settings, false is returned.
   */
  @Test
  public void testCheckAndSetSettingsWithWrongNumber() {
    // Setup
    ConfigurationRequirementListBox
            configSpec =
            new ConfigurationRequirementListBox("parameter1", new ArrayList<String>(), 2);
    // Expected values
    ConfigurationSettingListBox expectedSetting0 = mock(ConfigurationSettingListBox.class);
    ConfigurationSettingListBox expectedSetting1 = mock(ConfigurationSettingListBox.class);
    ConfigurationSettingListBox expectedSetting2 = mock(ConfigurationSettingListBox.class);

    // Execute functionality
    // Check result
    try {
      configSpec.checkAndSetSettings(expectedSetting0);
    } catch (AlgorithmConfigurationException e) {
      // should throw an exception
    }

    try {
      configSpec.checkAndSetSettings(expectedSetting0, expectedSetting1);
    } catch (AlgorithmConfigurationException e) {
      fail(); // number of settings is correct
    }

    try {
      configSpec.checkAndSetSettings(expectedSetting0, expectedSetting1, expectedSetting2);
    } catch (AlgorithmConfigurationException e) {
      // should throw an exception
    }
  }



  /**
   * Test method for {@link ConfigurationRequirement<de.metanome.algorithm_integration.configuration.ConfigurationSettingRelationalInput>#checkAndSetSettings(ConfigurationSettingRelationalInput...)}
   * <p/>
   * When setting the wrong number of settings, false is returned.
   */
  @Test
  public void testCheckAndSetSettingsWithWrongNumberRange() {
    // Setup
    ConfigurationRequirementRelationalInput
      configSpec =
      new ConfigurationRequirementRelationalInput("parameter1", 2, 4);
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
    ConfigurationSettingRelationalInput
      expectedSetting3 =
      mock(ConfigurationSettingRelationalInput.class);

    // Execute functionality
    // Check result
    try {
      configSpec.checkAndSetSettings(expectedSetting0);
    } catch (AlgorithmConfigurationException e) {
      // should trow an exception
    }

    try {
      configSpec.checkAndSetSettings(expectedSetting0, expectedSetting1);
    } catch (AlgorithmConfigurationException e) {
      fail(); // number of settings is correct
    }

    try {
      configSpec.checkAndSetSettings(expectedSetting0, expectedSetting1, expectedSetting2);
    } catch (AlgorithmConfigurationException e) {
      fail(); // number of settings is correct
    }

    try {
      configSpec.checkAndSetSettings(expectedSetting0, expectedSetting1, expectedSetting2,
        expectedSetting3);
    } catch (AlgorithmConfigurationException e) {
      // should throw an exception
    }
  }

}
