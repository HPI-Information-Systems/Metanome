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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for {@link ResultsDbEntity}
 *
 * @author Jakob Zwiener
 */
public class ResultsDbEntityTest {

  /**
   * Test method for {@link ResultsDbEntity#delete()}
   *
   * ResultsDbEntities should be deletable. After deletion they should no longer be retrievable.
   */
  @Test
  public void testDelete() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    String expectedFileName = "someFileName";
    Algorithm expectedAlgorithm = new Algorithm(expectedFileName)
        .store();

    // Check precondition
    Algorithm actualAlgorithm = Algorithm.retrieve(expectedFileName);
    assertEquals(expectedAlgorithm, actualAlgorithm);

    // Execute functionality
    expectedAlgorithm.delete();

    // Check result
    assertNull(Algorithm.retrieve(expectedFileName));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link Algorithm#delete()}
   *
   * Calling delete on an ResultsDbEntity that has not yet been stored should be successful with no
   * result.
   */
  @Test
  public void testDeleteNotStored() {
    // Setup
    HibernateUtil.clear();

    // Expected values
    String expectedFileName = "someFileName";
    Algorithm expectedAlgorithm = new Algorithm(expectedFileName);

    // Execute functionality
    expectedAlgorithm.delete();

    // Cleanup
    HibernateUtil.clear();
  }
}
