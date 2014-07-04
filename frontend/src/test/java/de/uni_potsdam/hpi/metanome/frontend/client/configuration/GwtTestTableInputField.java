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

package de.uni_potsdam.hpi.metanome.frontend.client.configuration;

import com.google.gwt.junit.client.GWTTestCase;

import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.results_db.EntityStorageException;

import org.junit.Test;


public class GwtTestTableInputField extends GWTTestCase {

  /**
   * Test method for {@link TableInputField#getValue()}
   * <p/>
   */
  @Test
  public void testGetValue() throws InputValidationException, EntityStorageException {
//    //Setup
//    HibernateUtil.clear();
//
//    DatabaseConnection connection = new DatabaseConnection();
//    connection.setId(1);
//    connection.setUrl("url");
//    connection.setUsername("username");
//    connection.setPassword("password");
//    DatabaseConnection.store(connection);
//
//    String expectedConnectionIdentifier = "1: url";
//    String expectedTableName = "tableName";
//
//    TableInputField input = new TableInputField();
//
//    // Execute
//    input.setValues(expectedConnectionIdentifier, expectedTableName);
//    TableInput actualTableInput = input.getValue();
//
//    //Check
//    assertEquals(expectedTableName, actualTableInput.getTableName());
//    assertEquals(connection, actualTableInput.getDatabaseConnection());
//
//    // Cleanup
//    HibernateUtil.clear();
  }

  @Override
  public String getModuleName() {
    return "de.uni_potsdam.hpi.metanome.frontend.MetanomeTest";
  }
}
