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

package de.uni_potsdam.hpi.metanome.frontend.client.inputs;

import com.google.gwt.junit.client.GWTTestCase;

import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.results_db.DatabaseConnection;

import org.junit.Test;


public class GwtTestDatabaseConnectionField extends GWTTestCase {

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.client.inputs.DatabaseConnectionField#getValue()}
   * <p/>
   */
  @Test
  public void testGetValue() throws InputValidationException {
    //Setup
    String expectedUrl = "url";
    String expectedUser = "user";
    String expectedPassword = "password";

    DatabaseConnectionField input = new DatabaseConnectionField();

    // Execute
    input.setValues(expectedUrl, expectedUser, expectedPassword);
    DatabaseConnection actualConnection = input.getValue();

    //Check
    assertEquals(expectedUrl, actualConnection.getUrl());
    assertEquals(expectedUser, actualConnection.getUsername());
    assertEquals(expectedPassword, actualConnection.getPassword());
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.client.inputs.DatabaseConnectionField#getValue()}
   * <p/>
   */
  @Test
  public void testGetValueWithInvalidValues() {
    //Setup
    String expectedUrl = "url";
    String expectedUser = "";
    String expectedPassword = "";

    DatabaseConnectionField input = new DatabaseConnectionField();

    // Execute
    input.setValues(expectedUrl, expectedUser, expectedPassword);

    //Check
    try {
      input.getValue();
    } catch (InputValidationException e) {
      assertTrue(true);
    }
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.MetanomeTest";
  }

}
