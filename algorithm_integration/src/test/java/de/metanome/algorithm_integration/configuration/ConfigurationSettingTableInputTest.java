/**
 * Copyright 2014-2016 by Metanome Project
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

import de.metanome.test_helper.EqualsAndHashCodeTester;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConfigurationSettingTableInputTest {


  /**
   * Test method for {@link ConfigurationSettingTableInput#ConfigurationSettingTableInput(String,
   * ConfigurationSettingDatabaseConnection)}
   */
  @Test
  public void testConstructor() {
    // Setup
    String expectedUrl = "url";
    String expectedUsername = "username";
    String expectedPassword = "password";
    DbSystem expectedSystem = DbSystem.PostgreSQL;

    // Expected values
    String expectedTable = "table";
    ConfigurationSettingDatabaseConnection
      expectedDatabaseConnection =
      new ConfigurationSettingDatabaseConnection(expectedUrl, expectedUsername, expectedPassword,
        expectedSystem);

    // Execute functionality
    ConfigurationSettingTableInput
      actualSetting =
      new ConfigurationSettingTableInput(expectedTable, expectedDatabaseConnection);

    // Check result
    assertEquals(expectedTable, actualSetting.getTable());
    assertEquals(expectedDatabaseConnection, actualSetting.getDatabaseConnection());
  }

  /**
   * Test method for {@link ConfigurationSettingTableInput#getValueAsString()}
   */
  @Test
  public void testGetValueAsString() {
    // Setup
    String expectedUrl = "url";
    String expectedUsername = "username";
    String expectedPassword = "password";
    DbSystem expectedSystem = DbSystem.PostgreSQL;

    // Expected values
    String expectedTable = "table";
    ConfigurationSettingDatabaseConnection
      expectedDatabaseConnection =
      new ConfigurationSettingDatabaseConnection(expectedUrl, expectedUsername, expectedPassword,
        expectedSystem);

    ConfigurationSettingTableInput
      setting =
      new ConfigurationSettingTableInput(expectedTable, expectedDatabaseConnection);

    String
      expectedValuesString =
      expectedTable + "; " + expectedDatabaseConnection.getValueAsString();

    // Execute functionality
    String actualValuesString = setting.getValueAsString();

    // Check result
    assertEquals(expectedValuesString, actualValuesString);
  }

  /**
   * Test method for {@link de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput#equals(Object)}
   * and {@link ConfigurationSettingTableInput#hashCode()}
   */
  @Test
  public void testEqualsHashCode() {
    // Setup
    String expectedUrl = "url";
    String expectedUsername = "username";
    String expectedPassword = "password";
    DbSystem expectedSystem = DbSystem.PostgreSQL;
    // Expected values
    String expectedTable = "table";
    ConfigurationSettingDatabaseConnection
      expectedDatabaseConnection =
      new ConfigurationSettingDatabaseConnection(expectedUrl, expectedUsername, expectedPassword,
        expectedSystem);

    ConfigurationSettingTableInput
      setting =
      new ConfigurationSettingTableInput(expectedTable, expectedDatabaseConnection);

    ConfigurationSettingTableInput equalSetting =
      new ConfigurationSettingTableInput(expectedTable, expectedDatabaseConnection);
    ConfigurationSettingTableInput notEqualSettingTable =
      new ConfigurationSettingTableInput("some other table", expectedDatabaseConnection);
    ConfigurationSettingTableInput notEqualSettingDatabaseConnection =
      new ConfigurationSettingTableInput(expectedTable,
        new ConfigurationSettingDatabaseConnection(expectedUrl,
          "some other username",
          expectedPassword,
          expectedSystem));

    // Execute functionality
    // Check result
    new EqualsAndHashCodeTester<ConfigurationSettingTableInput>()
      .performBasicEqualsAndHashCodeChecks(setting, equalSetting, notEqualSettingTable,
        notEqualSettingDatabaseConnection);
  }


}
