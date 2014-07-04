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

package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

import de.uni_potsdam.hpi.metanome.test_helper.GwtSerializationTester;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingInteger}
 */
public class ConfigurationSettingIntegerTest {

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingInteger#ConfigurationSettingInteger(int)}
   */
  @Test
  public void testConstructor() {
    // Setup
    // Expected values
    int expectedValue = 7;

    // Execute functionality
    ConfigurationSettingInteger setting = new ConfigurationSettingInteger(expectedValue);

    // Check result
    assertEquals(expectedValue, setting.value);
  }

  /**
   * Tests that the instances of {@link de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingInteger}
   * are serializable in GWT.
   */
  @Test
  public void testGwtSerialization() {
    GwtSerializationTester.checkGwtSerializability(new ConfigurationSettingInteger(4));
  }
}
