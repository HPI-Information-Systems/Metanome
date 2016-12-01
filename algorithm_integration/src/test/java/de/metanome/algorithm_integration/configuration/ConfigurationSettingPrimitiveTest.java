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


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConfigurationSettingPrimitiveTest {

  @Test
  public void testIntegerConstructor() {
    // Setup
    // Expected values
    Integer expectedValue = 7;

    // Execute functionality
    ConfigurationSettingInteger setting = new ConfigurationSettingInteger(expectedValue);

    // Check result
    assertEquals(expectedValue, setting.getValue());
  }

  @Test
  public void testStringConstructor() {
    // Setup
    // Expected values
    String expectedValue = "expected";

    // Execute functionality
    ConfigurationSettingString setting = new ConfigurationSettingString(expectedValue);

    // Check result
    assertEquals(expectedValue, setting.getValue());
  }

  @Test
  public void testBooleanConstructor() {
    // Setup
    // Expected values
    Boolean expectedValue = false;

    // Execute functionality
    ConfigurationSettingBoolean setting = new ConfigurationSettingBoolean(expectedValue);

    // Check result
    assertEquals(expectedValue, setting.getValue());
  }


}
