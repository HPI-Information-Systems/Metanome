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

package de.metanome.backend.results_db;

import de.metanome.backend.resources.FileInputResource;
import de.metanome.backend.resources.InputResource;
import de.metanome.backend.resources.TableInputResource;
import de.metanome.test_helper.EqualsAndHashCodeTester;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link de.metanome.backend.results_db.Input}
 *
 * @author Jakob Zwiener
 */
public class InputTest {

  private InputResource inputResource = new InputResource();
  private FileInputResource fileInputResource = new FileInputResource();
  private TableInputResource tableInputResource = new TableInputResource();

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
    inputResource.store(input);
    assertNotEquals(oldId, input.getId());
    oldId = input.getId();
    inputResource.store(input);
    assertNotEquals(oldId, input.getId());

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link Input#equals(Object)} and {@link de.metanome.backend.results_db.Input#hashCode()}
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
