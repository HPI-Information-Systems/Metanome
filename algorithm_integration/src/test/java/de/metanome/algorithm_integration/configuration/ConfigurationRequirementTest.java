/*
 * Copyright 2015 by the Metanome project
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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ConfigurationRequirementTest {

  /**
   * Test method for {@link ConfigurationRequirement#ConfigurationRequirement(String)}
   * The identifier should be set in the constructor and be retrievable through getIdentifier.
   * The numberOfValues should be set to 1.
   */
  @Test
  public void testConstructorGetOne() {
    // Setup
    // Expected values
    String expectedIdentifier = "parameter1";
    int expectedNumberOfValues = 1;

    // Execute functionality
    ConfigurationRequirement
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
   * Test method for {@link ConfigurationRequirement#ConfigurationRequirement(String,
   * int)}
   * The identifier should be set in the constructor and be retrievable through
   * getIdentifier. The numberOfValues should be set to 2.
   */
  @Test
  public void testConstructorGetTwo() {
    // Setup
    // Expected values
    String expectedIdentifier = "parameter1";
    int expectedNumberOfValues = 2;

    // Execute functionality
    ConfigurationRequirement
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
   * Test method for {@link ConfigurationRequirement#ConfigurationRequirement(String,
   * int)}
   * The identifier should be set in the constructor and be retrievable through
   * getIdentifier. The numberOfValues should be set to the range (2, 4).
   */
  @Test
  public void testConstructorGetRange() {
    // Setup
    // Expected values
    String expectedIdentifier = "parameter1";
    int expectedMinNumberOfValues = 2;
    int expectedMaxNumberOfValues = 4;

    // Execute functionality
    ConfigurationRequirement
        configSpec =
        new ConfigurationRequirementString(expectedIdentifier, expectedMinNumberOfValues, expectedMaxNumberOfValues);
    String actualIdentifier = configSpec.getIdentifier();
    int actualMinNumberOfValues = configSpec.getMinNumberOfSettings();
    int actualMaxNumberOfValues = configSpec.getMaxNumberOfSettings();

    // Check result
    assertEquals(expectedIdentifier, actualIdentifier);
    assertEquals(expectedMinNumberOfValues, actualMinNumberOfValues);
    assertEquals(expectedMaxNumberOfValues, actualMaxNumberOfValues);
    assertFalse(configSpec.isFixNumberOfSettings());
  }

}
