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
import de.metanome.algorithm_integration.AlgorithmExecutionException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class ConfigurationRequirementDefaultValueTest {

  /**
   * Test method for {@link ConfigurationRequirementDefaultValue#getDefaultValue(int)}
   * <p/>
   * The default values should be accessible via an index.
   */
  @Test
  public void testGetDefaultValues() {
    // Setup
    ConfigurationRequirementBoolean configSpec =
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
   * Test method for {@link ConfigurationRequirementDefaultValue<String>#checkAndSetDefaultValues(String...)}
   * <p/>
   * If the number of default values does not match the number of settings an exception is thrown.
   */
  @Test(expected = AlgorithmConfigurationException.class)
  public void testCheckAndSetDefaultValuesException() throws AlgorithmConfigurationException {
    // Setup
    ConfigurationRequirementString specificationString =
      new ConfigurationRequirementString("parameter1", 2);

    ConfigurationSettingString expectedSetting1 = new ConfigurationSettingString();
    ConfigurationSettingString expectedSetting2 = new ConfigurationSettingString();
    specificationString.checkAndSetSettings(expectedSetting1, expectedSetting2);

    // Execute functionality
    specificationString.checkAndSetDefaultValues("test");
  }

  /**
   * Test method for {@link ConfigurationRequirementDefaultValue<String[]>#checkAndSetDefaultValues(String[]...)}
   * <p/>
   * The default values should be correctly settable on the specification.
   */
  @Test
  public void testSetDefaultArrayValues() throws AlgorithmConfigurationException {
    // Setup
    String[] checkBoxValues = {"first", "second", "third"};

    ConfigurationRequirementCheckBox specificationCheckBox =
            new ConfigurationRequirementCheckBox("parameter1", checkBoxValues, 2);

    ConfigurationSettingCheckBox expectedSetting1 = new ConfigurationSettingCheckBox();
    ConfigurationSettingCheckBox expectedSetting2 = new ConfigurationSettingCheckBox();
    specificationCheckBox.checkAndSetSettings(expectedSetting1, expectedSetting2);

    // Expected values
    String[] expectedString1 = {"second"};
    String[] expectedString2 = {"third"};

    // Execute functionality
    specificationCheckBox.checkAndSetDefaultValues(expectedString1, expectedString2);
    ConfigurationSettingCheckBox[] actualSettings = specificationCheckBox.getSettings();

    // Check results
    assertEquals(expectedString1[0], actualSettings[0].getValue()[0]);
    assertEquals(expectedString2[0], actualSettings[1].getValue()[0]);
  }



  /**
   * Test method for {@link ConfigurationRequirementDefaultValue<String>#checkAndSetDefaultValues(String...)}
   * <p/>
   * The default values should be correctly settable on the specification.
   */
  @Test
  public void testSetDefaultValues() throws AlgorithmConfigurationException {
    // Setup
    ArrayList<String> listValues = new ArrayList<>();
    listValues.add("first");
    listValues.add("second");
    listValues.add("third");

    ConfigurationRequirementListBox specificationListBox =
      new ConfigurationRequirementListBox("parameter1", listValues, 2);

    ConfigurationSettingListBox expectedSetting1 = new ConfigurationSettingListBox();
    ConfigurationSettingListBox expectedSetting2 = new ConfigurationSettingListBox();
    specificationListBox.checkAndSetSettings(expectedSetting1, expectedSetting2);

    // Expected values
    String expectedString1 = "second";
    String expectedString2 = "third";

    // Execute functionality
    specificationListBox.checkAndSetDefaultValues(expectedString1, expectedString2);
    ConfigurationSettingListBox[] actualSettings = specificationListBox.getSettings();

    // Check results
    assertEquals(expectedString1, actualSettings[0].getValue());
    assertEquals(expectedString2, actualSettings[1].getValue());
  }

  /**
   * Test method for {@link ConfigurationRequirement<de.metanome.algorithm_integration.configuration.ConfigurationSettingBoolean>#checkAndSetSettings(ConfigurationSettingBoolean...)}
   * <p/>
   * The values should be correctly settable on the specification.
   */
  @Test
  public void testCheckAndSetSettings() {
    // Setup
    ConfigurationRequirementBoolean configSpec =
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
   * Test method for {@link ConfigurationRequirement<de.metanome.algorithm_integration.configuration.ConfigurationSettingBoolean>#checkAndSetSettings(ConfigurationSettingBoolean...)}
   * <p/>
   * The values should be correctly settable on the specification.
   */
  @Test
  public void testCheckAndSetSettingsWithRange() {
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
      configSpec
        .checkAndSetSettings(expectedValue0, expectedValue1, expectedValue2, expectedValue3);
    } catch (AlgorithmConfigurationException e) {
      fail();
    }

    // Check result
    assertEquals(expectedValue0, configSpec.settings[0]);
  }

  /**
   * Test method for {@link ConfigurationRequirement<de.metanome.algorithm_integration.configuration.ConfigurationSettingBoolean>#checkAndSetSettings(ConfigurationSettingBoolean...)}
   * <p/>
   * Setting a wrong number of settings should throw an Exception.
   */
  @Test(expected = AlgorithmExecutionException.class)
  public void testCheckAndSetSettingsWithWrongNumber() throws AlgorithmConfigurationException {
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
   * Test method for {@link ConfigurationRequirement<de.metanome.algorithm_integration.configuration.ConfigurationSettingInteger>#checkAndSetSettings(ConfigurationSettingInteger...)}
   * <p/>
   * Setting a wrong number of settings should throw an Exception.
   */
  @Test(expected = AlgorithmExecutionException.class)
  public void testCheckAndSetSettingsWithWrongNumberRange() throws AlgorithmConfigurationException {
    // Setup
    ConfigurationRequirementInteger configSpec =
      new ConfigurationRequirementInteger("parameter1", 2, 4);
    // Expected values
    ConfigurationSettingInteger expectedValue = mock(ConfigurationSettingInteger.class);

    // Execute functionality
    configSpec.checkAndSetSettings(expectedValue);
  }

}
