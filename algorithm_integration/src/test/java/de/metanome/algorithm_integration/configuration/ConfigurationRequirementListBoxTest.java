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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for {@link ConfigurationRequirementListBox}
 */
public class ConfigurationRequirementListBoxTest {

  /**
   * Test method for {@link ConfigurationRequirementListBox#getSettings()} and {@link
   * ConfigurationRequirementListBox#checkAndSetSettings(ConfigurationSettingListBox...)}
   */
  @Test
  public void testGetSetSpecification() throws AlgorithmConfigurationException {
    // Setup
    ArrayList<String> expectedValues = new ArrayList<>();
    expectedValues.add("first");
    expectedValues.add("second");
    expectedValues.add("third");
    ConfigurationRequirementListBox
        specificationListBox =
        new ConfigurationRequirementListBox("parameter1", expectedValues, 2);

    // Expected values
    ConfigurationSettingListBox expectedSetting1 = new ConfigurationSettingListBox();
    ConfigurationSettingListBox expectedSetting2 = new ConfigurationSettingListBox();

    // Execute functionality
    specificationListBox.checkAndSetSettings(expectedSetting1, expectedSetting2);
    List<ConfigurationSettingListBox>
        actualSettings =
        Arrays.asList(specificationListBox.getSettings());

    // Check results
    assertThat(actualSettings, IsIterableContainingInAnyOrder
        .containsInAnyOrder(expectedSetting1, expectedSetting2));
  }

  /**
   * Test method for {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementDefaultValue<String>#checkAndSetDefaultValues(String...)}
   */
  @Test
  public void testSetDefaultValues() throws AlgorithmConfigurationException {
    // Setup
    ArrayList<String> listValues = new ArrayList<>();
    listValues.add("first");
    listValues.add("second");
    listValues.add("third");

    ConfigurationRequirementListBox
        specificationListBox =
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
    assertEquals(expectedString1, actualSettings[0].getSelectedValue());
    assertEquals(expectedString2, actualSettings[1].getSelectedValue());
  }

  /**
   * Test method for {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementListBox#checkAndSetSettings(ConfigurationSettingListBox...)}
   *
   * Setting a wrong number of settings should throw an Exception.
   */
  @Test(expected=AlgorithmExecutionException.class)
  public void testSetSettingsWithWrongNumber() throws AlgorithmConfigurationException {
    // Setup
    ConfigurationRequirementListBox
        configSpec =
        new ConfigurationRequirementListBox("parameter1", new ArrayList<String>(), 2);
    // Expected values
    ConfigurationSettingListBox expectedValue = mock(ConfigurationSettingListBox.class);

    // Execute functionality
    configSpec.checkAndSetSettings(expectedValue);
  }

  /**
   * Test method for {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementListBox#checkAndSetSettings(ConfigurationSettingListBox...)}
   *
   * Setting a wrong number of settings should throw an Exception.
   */
  @Test(expected=AlgorithmExecutionException.class)
  public void testSetSettingsWithWrongNumberRange() throws AlgorithmConfigurationException {
    // Setup
    ConfigurationRequirementListBox
        configSpec =
        new ConfigurationRequirementListBox("parameter1", new ArrayList<String>(), 2, 4);
    // Expected values
    ConfigurationSettingListBox expectedValue = mock(ConfigurationSettingListBox.class);

    // Execute functionality
    configSpec.checkAndSetSettings(expectedValue);
  }

  /**
   * Tests that the instances of {@link ConfigurationRequirementListBox}
   * are serializable in GWT.
   */
  @Test
  public void testGwtSerialization() {
    GwtSerializationTester.checkGwtSerializability(
        new ConfigurationRequirementListBox("some identifier", new ArrayList<String>(), 3));
  }
}
