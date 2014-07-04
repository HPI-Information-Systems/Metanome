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

package de.uni_potsdam.hpi.metanome.algorithm_execution;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link ProgressCache}
 *
 * @author Jakob Zwiener
 */
public class ProgressCacheTest {

  protected ProgressCache progressCache;

  protected float delta = 0.0001f;

  @Before
  public void setUp() throws Exception {
    progressCache = new ProgressCache();
  }

  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test method for {@link ProgressCache#updateProgress(float)} <p/> The progress should be updated
   * and retrievable through {@link ProgressCache#getProgress()}.
   */
  @Test
  public void testUpdateProgress() {
    // Setup
    // Expected values
    float expectedProgressUpdate1 = 3 / 7f;
    float expectedProgressUpdate2 = 7 / 8f;

    // Execute functionality
    // Check result
    assertTrue(progressCache.updateProgress(expectedProgressUpdate1));
    assertEquals(expectedProgressUpdate1, progressCache.getProgress(), delta);
    assertTrue(progressCache.updateProgress(expectedProgressUpdate2));
    assertEquals(expectedProgressUpdate2, progressCache.getProgress(), delta);
  }

  /**
   * Test method for {@link ProgressCache#updateProgress(float)} <p/> Progress values greater than 1
   * or smaller than 0 should not be stored and false be returned.
   */
  @Test
  public void testUpdateProgressInvalidValues() {
    // Setup
    // Expected values
    List<Float> validValues = new LinkedList<>();
    validValues.add(1f);
    validValues.add(0f);
    validValues.add(0.5f);

    float expectedLastValidValue = 0.42f;

    List<Float> invalidValues = new LinkedList<>();
    invalidValues.add(1.1f);
    invalidValues.add(-0.1f);
    invalidValues.add(23f);

    // Execute functionality
    // Check result
    for (Float progress : validValues) {
      assertTrue(progressCache.updateProgress(progress));
    }

    assertTrue(progressCache.updateProgress(expectedLastValidValue));

    for (Float progress : invalidValues) {
      assertFalse(progressCache.updateProgress(progress));
    }

    assertEquals(expectedLastValidValue, progressCache.getProgress(), delta);
  }

  /**
   * Test method for {@link ProgressCache#getProgress()} <p/> Progress should be zero initially.
   */
  @Test
  public void testGetProgressInitial() {
    // Check result
    assertEquals(0, progressCache.getProgress(), delta);
  }

}
