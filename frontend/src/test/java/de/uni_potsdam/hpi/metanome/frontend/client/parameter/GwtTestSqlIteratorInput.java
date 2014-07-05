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

package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import com.google.gwt.junit.client.GWTTestCase;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingSqlIterator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.DbSystem;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.frontend.client.parameter.SqlIteratorInput}
 *
 * @author Jakob Zwiener
 */
public class GwtTestSqlIteratorInput extends GWTTestCase {

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.client.parameter.SqlIteratorInput#SqlIteratorInput(boolean)}
   * <p/> After calling the constructor the optional parameter should be set correctly and all
   * widgets should be initialized.
   */
  public void testConstructor() {
    // Setup
    // Expected values
    boolean expectedOptional = true;

    // Execute functionality
    SqlIteratorInput actualSqlIteratorInput = new SqlIteratorInput(expectedOptional);

    // Check result
    assertEquals(expectedOptional, actualSqlIteratorInput.isOptional);
    assertEquals(4, actualSqlIteratorInput.layoutTable.getRowCount());
    assertNotNull(actualSqlIteratorInput.dbUrlTextbox);
    assertNotNull(actualSqlIteratorInput.usernameTextbox);
    assertNotNull(actualSqlIteratorInput.passwordTextbox);
    assertNotNull(actualSqlIteratorInput.systemListBox);
  }

  /**
   * Test method for {@link SqlIteratorInput#getValues()} and {@link de.uni_potsdam.hpi.metanome.frontend.client.parameter.SqlIteratorInput#setValues(de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingSqlIterator)}
   * <p/> The getValues and setValues methods should set and retrieve settings.
   */
  public void testGetSetValues() {
    // Setup
    SqlIteratorInput sqlIteratorInput = new SqlIteratorInput(false);
    // Expected values
    ConfigurationSettingSqlIterator expectedSetting =
        new ConfigurationSettingSqlIterator("some url", "username", "password", DbSystem.HANA);

    // Execute functionality
    sqlIteratorInput.setValues(expectedSetting);
    ConfigurationSettingSqlIterator actualSetting = sqlIteratorInput.getValues();

    // Check result
    assertEquals(expectedSetting, actualSetting);
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.Metanome";
  }
}
