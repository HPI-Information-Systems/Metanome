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

package de.uni_potsdam.hpi.metanome.frontend.server;

import de.metanome.backend.results_db.DatabaseConnection;
import de.metanome.backend.results_db.EntityStorageException;
import de.metanome.backend.results_db.HibernateUtil;
import de.metanome.backend.results_db.TableInput;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.frontend.server.TableInputServiceImpl}
 */
public class TableInputServiceImplTest {

  /**
   * Test method for {@link TableInputServiceImpl#listTableInputs()}
   */
  @Test
  public void testListTableInputs() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    TableInputServiceImpl tableInputService = new TableInputServiceImpl();

    // Expected values
    DatabaseConnection expectedDb1 = new DatabaseConnection();
    expectedDb1.setId(1);
    expectedDb1.setUrl("url1");
    expectedDb1.setPassword("password1");
    expectedDb1.setUsername("db1");
    expectedDb1.store();

    DatabaseConnection expectedDb2 = new DatabaseConnection();
    expectedDb2.setId(2);
    expectedDb2.setUrl("url2");
    expectedDb2.setPassword("password2");
    expectedDb2.setUsername("db2");
    expectedDb2.store();

    TableInput expectedTableInput1 = new TableInput();
    expectedTableInput1.setDatabaseConnection(expectedDb1);
    expectedTableInput1.setTableName("table1");

    TableInput expectedTableInput2 = new TableInput();
    expectedTableInput1.setDatabaseConnection(expectedDb2);
    expectedTableInput1.setTableName("table2");

    TableInput[] expectedTableInputs = {expectedTableInput1, expectedTableInput2};

    for (TableInput input : expectedTableInputs) {
      tableInputService.storeTableInput(input);
    }

    // Execute functionality
    List<TableInput> actualTableInputs = tableInputService.listTableInputs();

    // Check result
    assertThat(actualTableInputs,
               IsIterableContainingInAnyOrder.containsInAnyOrder(expectedTableInputs));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.server.TableInputServiceImpl#getTableInput(long)}
   */
  @Test
  public void testGetTableInput() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    TableInputServiceImpl tableInputService = new TableInputServiceImpl();

    // Expected values
    DatabaseConnection dbConnection = new DatabaseConnection();
    dbConnection.setUrl("url1");
    dbConnection.setPassword("password1");
    dbConnection.setUsername("db1");
    dbConnection.store();

    TableInput expectedTableInput = new TableInput();
    expectedTableInput.setDatabaseConnection(dbConnection);
    expectedTableInput.setTableName("table1");

    tableInputService.storeTableInput(expectedTableInput);

    long id = expectedTableInput.getId();

    // Execute functionality
    TableInput actualTableInput = tableInputService.getTableInput(id);

    // Check result
    assertEquals(expectedTableInput, actualTableInput);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.server.TableInputServiceImpl#getTableInput(long)}
   */
  @Test
  public void testGetTableInputWithIncorrectId() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    TableInputServiceImpl tableInputService = new TableInputServiceImpl();

    // Execute functionality
    TableInput actualTableInput = tableInputService.getTableInput(2);

    // Check result
    assertEquals(null, actualTableInput);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.server.TableInputServiceImpl#storeTableInput(de.metanome.backend.results_db.TableInput)}
   */
  @Test
  public void testStoreTableInput() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    TableInputServiceImpl tableInputService = new TableInputServiceImpl();

    // Expected values
    DatabaseConnection dbConnection = new DatabaseConnection();
    dbConnection.setId(0);
    dbConnection.setUrl("url1");
    dbConnection.setPassword("password1");
    dbConnection.setUsername("db1");
    dbConnection.store();

    TableInput expectedTableInput = new TableInput();
    expectedTableInput.setDatabaseConnection(dbConnection);
    expectedTableInput.setTableName("table1");

    // Execute functionality
    tableInputService.storeTableInput(expectedTableInput);

    // Finds algorithms of all or no interfaces
    List<TableInput> inputs = tableInputService.listTableInputs();

    // Check result
    assertTrue(inputs.contains(expectedTableInput));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.frontend.server.TableInputServiceImpl#deleteTableInput(de.metanome.backend.results_db.TableInput)}
   */
  @Test
  public void testDeleteTableInput() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    TableInputServiceImpl service = new TableInputServiceImpl();

    DatabaseConnection dbConnection = new DatabaseConnection();
    dbConnection.setUrl("url1");
    dbConnection.setPassword("password1");
    dbConnection.setUsername("db1");
    dbConnection.store();

    TableInput expectedTableInput = new TableInput();
    expectedTableInput.setDatabaseConnection(dbConnection);
    expectedTableInput.setTableName("table1");
    expectedTableInput.store();

    long id = expectedTableInput.getId();

    // Check precondition
    TableInput actualTableInput = TableInput.retrieve(id);
    assertEquals(expectedTableInput, actualTableInput);

    // Execute functionality
    service.deleteTableInput(expectedTableInput);

    // Check result
    assertNull(TableInput.retrieve(id));

    // Cleanup
    HibernateUtil.clear();
  }

}
