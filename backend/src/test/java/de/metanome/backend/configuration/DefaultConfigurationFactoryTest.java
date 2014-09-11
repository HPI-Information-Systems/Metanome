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
import de.metanome.algorithm_integration.configuration.ConfigurationRequirementRelationalInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
   * Test method for {@link de.metanome.backend.configuration.DefaultConfigurationFactory#build(de.metanome.algorithm_integration.configuration.ConfigurationRequirementRelationalInput)}
   *
   * A {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementRelationalInput}
   * should be converted into a {@link de.metanome.backend.configuration.ConfigurationValueRelationalInputGenerator}.
   * The underlying data source is a file.
   */
  @Test
  public void testBuildRelationalInput()
      throws FileNotFoundException, AlgorithmConfigurationException, InputGenerationException,
             InputIterationException {
    // Setup
    // Expected values
    String expectedIdentifier = "some identifier";
    String expectedPath = Thread.currentThread().getContextClassLoader().getResource(
        "inputData/inputA.csv").getPath();
    ConfigurationRequirementRelationalInput
        configRequirement =
        new ConfigurationRequirementRelationalInput(
            expectedIdentifier, 1);

    // Check preconditions
    assertTrue(configRequirement.setSettings(new ConfigurationSettingFileInput(expectedPath)));

    // Execute functionality
    ConfigurationValueRelationalInputGenerator
        actualConfigurationValue = factory.build(configRequirement);

    // Check result
    assertEquals(expectedIdentifier, actualConfigurationValue.identifier);
    assertFalse(actualConfigurationValue.values[0].generateNewCopy().hasNext());
  }

}