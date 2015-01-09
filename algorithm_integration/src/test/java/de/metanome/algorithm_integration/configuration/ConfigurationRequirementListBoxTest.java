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
   * Test method for {@link ConfigurationRequirementListBox#ConfigurationRequirementListBox(String, java.util.List)}
   * The identifier should be set in the constructor and be retrievable through getIdentifier.
   * The numberOfValues should be set to 1.
   */
  @Test
  public void testConstructorGetOne() {
    // Setup
    // Expected values
    String expectedIdentifier = "parameter1";
    int expectedNumberOfValues = 1;
    ArrayList<String> expectedValues = new ArrayList<>();
    expectedValues.add("first");
    expectedValues.add("second");
    expectedValues.add("third");

    ConfigurationRequirementListBox
        configSpec =
        new ConfigurationRequirementListBox(expectedIdentifier, expectedValues);

    // Execute functionality
    String actualIdentifier = configSpec.getIdentifier();
    int actualNumberOfValues = configSpec.getNumberOfSettings();
    List<String> actualValues = configSpec.getValues();

    // Check result
    assertEquals(expectedIdentifier, actualIdentifier);
    assertEquals(expectedNumberOfValues, actualNumberOfValues);
    assertEquals(expectedValues, actualValues);
  }

  /**
   * Test method for {@link ConfigurationRequirementListBox#ConfigurationRequirementListBox(String, java.util.List, int)}
   * The identifier should be set in the constructor and be retrievable through getIdentifier.
   * The numberOfValues should be set to 2.
   */
  @Test
  public void testConstructorGetTwo() {
    // Setup
    // Expected values
    String expectedIdentifier = "parameter1";
    int expectedNumberOfValues = 2;
    ArrayList<String> expectedValues = new ArrayList<>();
    expectedValues.add("first");
    expectedValues.add("second");
    expectedValues.add("third");

    ConfigurationRequirementListBox
        configSpec =
        new ConfigurationRequirementListBox(expectedIdentifier, expectedValues,
                                              expectedNumberOfValues);

    // Execute functionality
    String actualIdentifier = configSpec.getIdentifier();
    int actualNumberOfValues = configSpec.getNumberOfSettings();
    List<String> actualValues = configSpec.getValues();

    // Check result
    assertEquals(expectedIdentifier, actualIdentifier);
    assertEquals(expectedNumberOfValues, actualNumberOfValues);
    assertEquals(expectedValues, actualValues);
  }

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
   * Tests that the instances of {@link ConfigurationRequirementListBox}
   * are serializable in GWT.
   */
  @Test
  public void testGwtSerialization() {
    GwtSerializationTester.checkGwtSerializability(
        new ConfigurationRequirementListBox("some identifier", new ArrayList<String>(), 3));
  }
}
