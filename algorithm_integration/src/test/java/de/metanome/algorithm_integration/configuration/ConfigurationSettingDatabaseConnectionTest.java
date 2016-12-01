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

/**
 * Tests for {@link ConfigurationSettingDatabaseConnection}
 *
 * @author Jakob Zwiener
 */
public class ConfigurationSettingDatabaseConnectionTest {

  /**
   * Test method for {@link ConfigurationSettingDatabaseConnection#ConfigurationSettingDatabaseConnection(String,
   * String, String, DbSystem)}
   */
  @Test
  public void testConstructor() {
    // Setup
    // Expected values
    String expectedUrl = "some url";
    String expectedUsername = "some username";
    String expectedPassword = "some password";
    DbSystem expectedSystem = DbSystem.HANA;

    // Execute functionality
    ConfigurationSettingDatabaseConnection
      actualSetting =
      new ConfigurationSettingDatabaseConnection(expectedUrl, expectedUsername, expectedPassword,
        expectedSystem);

    // Check result
    assertEquals(expectedUrl, actualSetting.getDbUrl());
    assertEquals(expectedUsername, actualSetting.getUsername());
    assertEquals(expectedPassword, actualSetting.getPassword());
    assertEquals(expectedSystem, actualSetting.getSystem());
  }

  /**
   * Test method for {@link ConfigurationSettingDatabaseConnection#getValueAsString()}
   */
  @Test
  public void testGetValueAsString() {
    // Setup
    // Expected values
    String expectedUrl = "url";
    String expectedUsername = "username";
    String expectedPassword = "password";
    DbSystem expectedSystem = DbSystem.PostgreSQL;
    ConfigurationSettingDatabaseConnection setting =
      new ConfigurationSettingDatabaseConnection(expectedUrl, expectedUsername, expectedPassword,
        expectedSystem);
    String
      expectedValuesString =
      expectedUrl + "; " + expectedUsername + "; " + DbSystem.PostgreSQL.name();

    // Execute functionality
    String actualValuesString = setting.getValueAsString();

    // Check result
    assertEquals(expectedValuesString, actualValuesString);
  }

  /**
   * Test method for {@link ConfigurationSettingDatabaseConnection#equals(Object)} and {@link
   * ConfigurationSettingDatabaseConnection#hashCode()}
   */
  @Test
  public void testEqualsHashCode() {
    // Setup
    // Expected values
    String expectedUrl = "url";
    String expectedUsername = "username";
    String expectedPassword = "password";
    DbSystem expectedSystem = DbSystem.Oracle;
    ConfigurationSettingDatabaseConnection setting =
      new ConfigurationSettingDatabaseConnection(expectedUrl, expectedUsername, expectedPassword,
        expectedSystem);
    ConfigurationSettingDatabaseConnection equalSetting =
      new ConfigurationSettingDatabaseConnection(expectedUrl, expectedUsername, expectedPassword,
        expectedSystem);
    ConfigurationSettingDatabaseConnection notEqualSettingUrl =
      new ConfigurationSettingDatabaseConnection("some other url", expectedUsername,
        expectedPassword, expectedSystem);
    ConfigurationSettingDatabaseConnection notEqualSettingUsername =
      new ConfigurationSettingDatabaseConnection(expectedUrl, "some other user", expectedPassword,
        expectedSystem);
    ConfigurationSettingDatabaseConnection notEqualSettingPassword =
      new ConfigurationSettingDatabaseConnection(expectedUrl, expectedUsername,
        "some other password", expectedSystem);
    ConfigurationSettingDatabaseConnection notEqualSettingSystem =
      new ConfigurationSettingDatabaseConnection(expectedUrl, expectedUsername, expectedPassword,
        DbSystem.PostgreSQL);

    // Execute functionality
    // Check result
    new EqualsAndHashCodeTester<ConfigurationSettingDatabaseConnection>()
      .performBasicEqualsAndHashCodeChecks(setting, equalSetting, notEqualSettingUrl,
        notEqualSettingUsername, notEqualSettingPassword,
        notEqualSettingSystem);
  }


}
