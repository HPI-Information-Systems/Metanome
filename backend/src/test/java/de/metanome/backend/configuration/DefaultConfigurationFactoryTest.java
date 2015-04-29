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

package de.metanome.backend.configuration;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementBoolean;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementInteger;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementListBox;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementRelationalInput;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementString;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingBoolean;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingInteger;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingListBox;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingString;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.backend.input.file.FileFixture;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests for {@link de.metanome.backend.configuration.DefaultConfigurationFactory}
 *
 * @author Jakob Zwiener
 */
public class DefaultConfigurationFactoryTest {

  private DefaultConfigurationFactory factory;

  @Before
  public void setUp() {
    factory = new DefaultConfigurationFactory();
  }

  /**
   * Test method for {@link de.metanome.backend.configuration.DefaultConfigurationFactory#build(de.metanome.algorithm_integration.configuration.ConfigurationRequirementBoolean)}
   *
   * {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementBoolean}s should
   * be correctly converted to {@link de.metanome.backend.configuration.ConfigurationValueBoolean}s.
   */
  @Test
  public void testBuildBoolean() throws AlgorithmConfigurationException {
    // Setup
    // Expected values
    String expectedIdentifier = "some identifier";
    boolean expectedValue1 = true;
    boolean expectedValue2 = false;
    ConfigurationRequirementBoolean
        requirement =
        new ConfigurationRequirementBoolean(expectedIdentifier, 2);
    requirement.checkAndSetSettings(new ConfigurationSettingBoolean(expectedValue1),
                                    new ConfigurationSettingBoolean(
                                        expectedValue2));

    // Execute functionality
    ConfigurationValueBoolean actualConfigValue =
        factory.build(requirement);

    // Check result
    assertEquals(expectedIdentifier, actualConfigValue.identifier);
    assertEquals(2, actualConfigValue.values.length);
    assertEquals(expectedValue1, actualConfigValue.values[0]);
    assertEquals(expectedValue2, actualConfigValue.values[1]);
  }

  /**
   * Test method for {@link de.metanome.backend.configuration.DefaultConfigurationFactory#build(de.metanome.algorithm_integration.configuration.ConfigurationRequirementInteger)}
   *
   * {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementInteger}s should
   * be correctly converted to {@link de.metanome.backend.configuration.ConfigurationValueInteger}s.
   */
  @Test
  public void testBuildInteger() throws AlgorithmConfigurationException {
    // Setup
    // Expected values
    String expectedIdentifier = "some identifier";
    Integer expectedValue1 = 42;
    Integer expectedValue2 = 23;
    ConfigurationRequirementInteger
        requirement =
        new ConfigurationRequirementInteger(expectedIdentifier, 2);
    requirement.checkAndSetSettings(new ConfigurationSettingInteger(expectedValue1),
                                    new ConfigurationSettingInteger(
                                        expectedValue2));

    // Execute functionality
    ConfigurationValueInteger actualConfigValue =
        factory.build(requirement);

    // Check result
    assertEquals(expectedIdentifier, actualConfigValue.identifier);
    assertEquals(2, actualConfigValue.values.length);
    assertEquals(expectedValue1, actualConfigValue.values[0]);
    assertEquals(expectedValue2, actualConfigValue.values[1]);
  }

  /**
   * Test method for {@link de.metanome.backend.configuration.DefaultConfigurationFactory#build(de.metanome.algorithm_integration.configuration.ConfigurationRequirementListBox)}
   *
   * {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementListBox}s should
   * be correctly converted to {@link de.metanome.backend.configuration.ConfigurationValueListBox}s.
   */
  @Test
  public void testBuildListBox() throws AlgorithmConfigurationException {
    // Setup
    String expectedIdentifier = "some identifier";
    ArrayList<String> possibleValues = new ArrayList<>();
    possibleValues.add("value1");
    possibleValues.add("value2");
    String expectedValue1 = possibleValues.get(0);
    String expectedValue2 = possibleValues.get(1);
    ConfigurationRequirementListBox
        requirement =
        new ConfigurationRequirementListBox(expectedIdentifier, possibleValues, 2);
    requirement.checkAndSetSettings(new ConfigurationSettingListBox(expectedValue1),
                                    new ConfigurationSettingListBox(expectedValue2));

    // Execute functionality
    ConfigurationValueListBox actualConfigValue = factory.build(requirement);

    // Check result
    assertEquals(expectedIdentifier, actualConfigValue.identifier);
    assertEquals(2, actualConfigValue.values.length);
    assertEquals(expectedValue1, actualConfigValue.values[0]);
    assertEquals(expectedValue2, actualConfigValue.values[1]);
  }

  /**
   * Test method for {@link de.metanome.backend.configuration.DefaultConfigurationFactory#build(de.metanome.algorithm_integration.configuration.ConfigurationRequirementRelationalInput)}
   *
   * A {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementRelationalInput}
   * should be converted into a {@link de.metanome.backend.configuration.ConfigurationValueRelationalInputGenerator}.
   * The underlying data source is a file.
   */
  @Test
  public void testBuildRelationalInput()
      throws FileNotFoundException, AlgorithmConfigurationException, InputGenerationException,
             InputIterationException, UnsupportedEncodingException {
    // Setup
    // Expected values
    String expectedIdentifier = "some identifier";
    String expectedPath = new FileFixture("fileContent").getTestData("some file name").getPath();
    ConfigurationRequirementRelationalInput
        configRequirement =
        new ConfigurationRequirementRelationalInput(
            expectedIdentifier);

    configRequirement.checkAndSetSettings(new ConfigurationSettingFileInput(expectedPath));

    // Execute functionality
    ConfigurationValueRelationalInputGenerator
        actualConfigurationValue = factory.build(configRequirement);

    // Check result
    assertEquals(expectedIdentifier, actualConfigurationValue.identifier);
    assertFalse(actualConfigurationValue.values[0].generateNewCopy().hasNext());
  }

  /**
   * Test method for{@link de.metanome.backend.configuration.DefaultConfigurationFactory#build(de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput)}
   *
   * {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput}s
   * should be correctly converted to {@link de.metanome.backend.configuration.ConfigurationValueFileInputGenerator}s.
   */
  @Test
  public void testBuildFileInput()
      throws AlgorithmConfigurationException, FileNotFoundException, UnsupportedEncodingException,
             InputGenerationException, InputIterationException {
    // Setup
    // Expected values
    String expectedIdentifier = "some identifier";
    String expectedPath = new FileFixture("fileContent").getTestData("some file name").getPath();
    ConfigurationRequirementFileInput
        configRequirement =
        new ConfigurationRequirementFileInput(expectedIdentifier);
    configRequirement.checkAndSetSettings(new ConfigurationSettingFileInput(expectedPath));

    // Execute functionality
    ConfigurationValueFileInputGenerator
        actualConfigurationValue =
        factory.build(configRequirement);

    // Check result
    assertEquals(expectedIdentifier, actualConfigurationValue.identifier);
    assertEquals(1, actualConfigurationValue.values.length);
    assertFalse(actualConfigurationValue.values[0].generateNewCopy().hasNext());
  }

  /**
   * Test method for {@link de.metanome.backend.configuration.DefaultConfigurationFactory#build(de.metanome.algorithm_integration.configuration.ConfigurationRequirementString)}
   *
   * {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementString}s should
   * be correctly converted to {@link de.metanome.backend.configuration.ConfigurationValueString}s.
   */
  @Test
  public void testBuildString() throws AlgorithmConfigurationException {
    // Setup
    // Expected values
    String expectedIdentifier = "some identifier";
    String expectedValue1 = "some string";
    String expectedValue2 = "some other string";
    ConfigurationRequirementString
        requirement =
        new ConfigurationRequirementString(expectedIdentifier, 2);
    requirement
        .checkAndSetSettings(new ConfigurationSettingString(expectedValue1),
                             new ConfigurationSettingString(
                                 expectedValue2));

    // Execute functionality
    ConfigurationValueString actualConfigValue = factory.build(requirement);

    // Check result
    assertEquals(expectedIdentifier, actualConfigValue.identifier);
    assertEquals(2, actualConfigValue.values.length);
    assertEquals(expectedValue1, actualConfigValue.values[0]);
    assertEquals(expectedValue2, actualConfigValue.values[1]);
  }

}
