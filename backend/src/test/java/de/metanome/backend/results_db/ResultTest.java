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
package de.metanome.backend.results_db;

import de.metanome.backend.constants.Constants;
import de.metanome.test_helper.EqualsAndHashCodeTester;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link de.metanome.backend.results_db.Result}
 *
 * @author Jakob Zwiener
 */
public class ResultTest {

  /**
   * Test method for {@link Result#getId()}
   */
  @Test
  @SuppressWarnings(Constants.SUPPRESS_WARNINGS_UNCHECKED)
  public void testGetId() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    HibernateUtil.store(new Result("file1"));
    HibernateUtil.store(new Result("file2"));

    // Execute functionality
    List<Result> actualResults = (List<Result>) HibernateUtil.queryCriteria(Result.class);

    long actualId1 = actualResults.get(0).getId();
    long actualId2 = actualResults.get(1).getId();

    // Check result
    assertEquals(Math.abs(actualId1 - actualId2), 1);

    // Cleanup
    HibernateUtil.clear();
  }

  @Test(expected = EntityStorageException.class)
  public void testUniqueFileName() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    try {
      HibernateUtil.store(new Result("file1"));
    } catch (EntityStorageException e) {
      fail();
    }

    HibernateUtil.store(new Result("file1"));

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link Result#equals(Object)} and {@link de.metanome.backend.results_db.Result#hashCode()}
   */
  @Test
  public void testEqualsAndHashCode() {
    // Setup
    String expectedFilePath = "some file path";
    Result result = new Result(expectedFilePath);
    Result equalResult = new Result(expectedFilePath);
    Result notEqualResult = new Result("some other path");

    // Execute functionality
    // Check result
    new EqualsAndHashCodeTester<Result>()
      .performBasicEqualsAndHashCodeChecks(result, equalResult, notEqualResult);
  }

}
