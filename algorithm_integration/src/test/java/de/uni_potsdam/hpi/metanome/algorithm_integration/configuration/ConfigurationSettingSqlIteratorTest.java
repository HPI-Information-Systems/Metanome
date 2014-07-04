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

import com.google.common.base.Joiner;

import de.uni_potsdam.hpi.metanome.test_helper.EqualsAndHashCodeTester;
import de.uni_potsdam.hpi.metanome.test_helper.GwtSerializationTester;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingSqlIterator}
 *
 * @author Jakob Zwiener
 */
public class ConfigurationSettingSqlIteratorTest {

  /**
   * Test method for {@link ConfigurationSettingSqlIterator#ConfigurationSettingSqlIterator(String,
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
    ConfigurationSettingSqlIterator
        actualSetting =
        new ConfigurationSettingSqlIterator(expectedUrl, expectedUsername, expectedPassword,
                                            expectedSystem);

    // Check result
    assertEquals(expectedUrl, actualSetting.getDbUrl());
    assertEquals(expectedUsername, actualSetting.getUsername());
    assertEquals(expectedPassword, actualSetting.getPassword());
    assertEquals(expectedSystem, actualSetting.getSystem());
  }

  /**
   * Test method for {@link ConfigurationSettingSqlIterator#getValueAsString()
   */
  @Test
  public void testGetValueAsString() {
    // Setup
    // Expected values
    String expectedUrl = "url";
    String expectedUsername = "username";
    String expectedPassword = "password";
    DbSystem expectedSystem = DbSystem.PostgreSQL;
    ConfigurationSettingSqlIterator setting =
        new ConfigurationSettingSqlIterator(expectedUrl, expectedUsername, expectedPassword,
                                            expectedSystem);
    String expectedValuesString = Joiner.on(';')
        .join(expectedUrl, expectedUsername, expectedPassword, DbSystem.PostgreSQL.name());

    // Execute functionality
    String actualValuesString = setting.getValueAsString();

    // Check result
    assertEquals(expectedValuesString, actualValuesString);
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingSqlIterator#equals(Object)}
   * and {@link ConfigurationSettingSqlIterator#hashCode()}
   */
  @Test
  public void testEqualsHashCode() {
    // Setup
    // Expected values
    String expectedUrl = "url";
    String expectedUsername = "username";
    String expectedPassword = "password";
    DbSystem expectedSystem = DbSystem.Oracle;
    ConfigurationSettingSqlIterator setting =
        new ConfigurationSettingSqlIterator(expectedUrl, expectedUsername, expectedPassword,
                                            expectedSystem);
    ConfigurationSettingSqlIterator equalSetting =
        new ConfigurationSettingSqlIterator(expectedUrl, expectedUsername, expectedPassword,
                                            expectedSystem);
    ConfigurationSettingSqlIterator notEqualSettingUrl =
        new ConfigurationSettingSqlIterator("some other url", expectedUsername,
                                            expectedPassword, expectedSystem);
    ConfigurationSettingSqlIterator notEqualSettingUsername =
        new ConfigurationSettingSqlIterator(expectedUrl, "some other user", expectedPassword,
                                            expectedSystem);
    ConfigurationSettingSqlIterator notEqualSettingPassword =
        new ConfigurationSettingSqlIterator(expectedUrl, expectedUsername,
                                            "some other password", expectedSystem);
    ConfigurationSettingSqlIterator notEqualSettingSystem =
        new ConfigurationSettingSqlIterator(expectedUrl, expectedUsername, expectedPassword,
                                            DbSystem.PostgreSQL);

    // Execute functionality
    // Check result
    new EqualsAndHashCodeTester<ConfigurationSettingSqlIterator>()
        .performBasicEqualsAndHashCodeChecks(setting, equalSetting, notEqualSettingUrl,
                                             notEqualSettingUsername, notEqualSettingPassword,
                                             notEqualSettingSystem);
  }

  /**
   * Tests that the instances of {@link de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingSqlIterator}
   * are serializable in GWT.
   */
  @Test
  public void testGwtSerialization() {
    GwtSerializationTester.checkGwtSerializability(
        new ConfigurationSettingSqlIterator("dbUrl", "username", "password", DbSystem.DB2));
  }
}
