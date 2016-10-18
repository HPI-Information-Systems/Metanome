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
import de.metanome.algorithm_integration.algorithm_types.FileInputParameterAlgorithm;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.input.FileInputGenerator;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link de.metanome.backend.configuration.ConfigurationValueFileInputGenerator}
 *
 * @author Jakob Zwiener
 */
public class ConfigurationValueFileInputGeneratorTest {

  /**
   * Test method for {@link de.metanome.backend.configuration.ConfigurationValueFileInputGenerator#triggerSetValue(de.metanome.algorithm_integration.Algorithm,
   * java.util.Set)} <p/> Parameters should be set on the algorithm through triggerSetValue. This is
   * the last call in a double dispatch call to determine the parameters type.
   */
  @Test
  public void testTriggerSetValue() throws AlgorithmConfigurationException {
    // Setup
    FileInputParameterAlgorithm algorithm = mock(FileInputParameterAlgorithm.class);
    Set<Class<?>> interfaces = new HashSet<>();
    interfaces.add(FileInputParameterAlgorithm.class);
    // Expected values
    String expectedIdentifier = "configId1";
    FileInputGenerator[]
      expectedConfigurationValue =
      {mock(FileInputGenerator.class), mock(FileInputGenerator.class)};

    // Execute functionality
    ConfigurationValueFileInputGenerator
      configValue =
      new ConfigurationValueFileInputGenerator(expectedIdentifier,
        expectedConfigurationValue);
    configValue.triggerSetValue(algorithm, interfaces);

    // Check result
    verify(algorithm)
      .setFileInputConfigurationValue(expectedIdentifier, expectedConfigurationValue);
  }

  /**
   * Test method for {@link de.metanome.backend.configuration.ConfigurationValueFileInputGenerator#triggerSetValue(de.metanome.algorithm_integration.Algorithm,
   * java.util.Set)} <p/> When the correct algorithm interface is missing an exception should be
   * thrown.
   */
  @Test
  public void testTriggerSetValueMissingInterface() {
    // Setup
    FileInputParameterAlgorithm algorithm = mock(FileInputParameterAlgorithm.class);
    // The file input parameter algorithm interface is missing.
    Set<Class<?>> interfaces = new HashSet<>();
    // Expected values
    String expectedIdentifier = "configId1";
    FileInputGenerator[]
      expectedConfigurationValues =
      {mock(FileInputGenerator.class), mock(FileInputGenerator.class)};

    // Execute functionality
    ConfigurationValueFileInputGenerator
      configValue =
      new ConfigurationValueFileInputGenerator(expectedIdentifier,
        expectedConfigurationValues);
    try {
      configValue.triggerSetValue(algorithm, interfaces);
      fail("No exception was thrown.");
    } catch (AlgorithmConfigurationException e) {
      // Intentionally left blank
    }
  }

  /**
   * Test method for {@link de.metanome.backend.configuration.ConfigurationValueFileInputGenerator#ConfigurationValueFileInputGenerator(de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput)}
   * <p/>
   * Tests that the constructor correctly converts the {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput}s
   * to {@link de.metanome.backend.configuration.ConfigurationValueFileInputGenerator}s.
   */
  @Test
  public void testConstructorRequirement() throws AlgorithmConfigurationException, FileNotFoundException {
    // Setup
    // Expected values
    String expectedIdentifier = "some identifier";
    int expectedNumberOfValues = 2;
    String expectedFirstFileName = Thread.currentThread().getContextClassLoader().getResource(
      "inputData/inputA.csv").getPath();
    String expectedSecondFileName = Thread.currentThread().getContextClassLoader().getResource(
      "inputData/inputB.csv").getPath();
    ConfigurationRequirementFileInput requirement = new ConfigurationRequirementFileInput(
      expectedIdentifier, expectedNumberOfValues);
    requirement.checkAndSetSettings(new ConfigurationSettingFileInput(expectedFirstFileName),
      new ConfigurationSettingFileInput(expectedSecondFileName));

    // Execute functionality
    ConfigurationValueFileInputGenerator
      actualConfigurationValue = new ConfigurationValueFileInputGenerator(requirement);

    // Check result
    assertEquals(expectedIdentifier, actualConfigurationValue.identifier);
    assertEquals(expectedNumberOfValues, actualConfigurationValue.values.length);
    assertEquals(new File(expectedFirstFileName),
      actualConfigurationValue.values[0].getInputFile());
    assertEquals(new File(expectedSecondFileName),
      actualConfigurationValue.values[1].getInputFile());
  }

}
