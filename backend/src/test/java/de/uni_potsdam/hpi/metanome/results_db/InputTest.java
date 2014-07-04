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

package de.uni_potsdam.hpi.metanome.results_db;

import de.uni_potsdam.hpi.metanome.test_helper.EqualsAndHashCodeTester;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.results_db.Input}
 *
 * @author Jakob Zwiener
 */
public class InputTest {

  /**
   * Test method for {@link Input#store()} and {@link Input#retrieve(long)} <p/> Inputs should be
   * storable and retrievable by id.
   */
  @Test
  public void testPersistence() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Input expectedInput = new Input();

    // Execute functionality
    assertSame(expectedInput, expectedInput.store());
    long id = expectedInput.getId();
    Input actualInput = Input.retrieve(id);

    // Check result
    assertEquals(expectedInput, actualInput);

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Every time an input is saved a new id should be generated.
   */
  @Test
  public void testIdGenerator() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    Input input = new Input();

    // Execute functionality
    // Check result
    long oldId = input.getId();
    input.store();
    assertNotEquals(oldId, input.getId());
    oldId = input.getId();
    input.store();
    assertNotEquals(oldId, input.getId());

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.Input#retrieveAll()}
   */
  @Test
  public void testRetrieveAll() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    Input expectedInput = new Input()
        .store();

    FileInput expectedFileInput = new FileInput()
        .store();

    TableInput expectedTableInput = new TableInput()
        .store();

    // Execute functionality
    List<Input> actualInputs = Input.retrieveAll();

    // Check result
    assertThat(actualInputs, IsIterableContainingInAnyOrder
        .containsInAnyOrder(expectedInput, expectedFileInput, expectedTableInput));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link Input#equals(Object)} and {@link de.uni_potsdam.hpi.metanome.results_db.Input#hashCode()}
   */
  @Test
  public void testEqualsAndHashCode() {
    // Setup
    // Expected values
    int id = 42;
    Input input = new Input()
        .setId(id);
    Input equalInput = new Input()
        .setId(id);
    Input notEqualInput = new Input()
        .setId(23);

    // Execute functionality
    // Check result
    new EqualsAndHashCodeTester<Input>()
        .performBasicEqualsAndHashCodeChecks(input, equalInput, notEqualInput);
  }
}
