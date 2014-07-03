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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.results_db.AlgorithmContentEquals}
 */
public class AlgorithmContentEqualsTest {

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.AlgorithmContentEquals#contentEquals(Algorithm,
   * Algorithm)} <p/> When in the first algorithm all fields are set, check, that only algorithms
   * are equal, that exhibit equal values in all fields.
   */
  @Test
  public void testContentEquals() {
    // Setup
    // Expected values
    Algorithm algorithm = buildEqualAlgorithm();
    Algorithm equalAlgorithm = buildEqualAlgorithm();

    Algorithm notEqualAlgorithmFileNameNull = buildEqualAlgorithm();
    notEqualAlgorithmFileNameNull.setFileName(null);
    Algorithm notEqualAlgorithmFileName = buildEqualAlgorithm();
    notEqualAlgorithmFileName.setFileName("some other file name");

    Algorithm notEqualAlgorithmNameNull = buildEqualAlgorithm();
    notEqualAlgorithmNameNull.setName(null);
    Algorithm notEqualAlgorithmName = buildEqualAlgorithm();
    notEqualAlgorithmName.setName("some other name");

    Algorithm notEqualAlgorithmAuthorNull = buildEqualAlgorithm();
    notEqualAlgorithmAuthorNull.setAuthor(null);
    Algorithm notEqualAlgorithmAuthor = buildEqualAlgorithm();
    notEqualAlgorithmAuthor.setAuthor("some other author");

    Algorithm notEqualAlgorithmDescriptionNull = buildEqualAlgorithm();
    notEqualAlgorithmDescriptionNull.setDescription(null);
    Algorithm notEqualAlgorithmDescription = buildEqualAlgorithm();
    notEqualAlgorithmDescription.setDescription("some other description");

    Algorithm notEqualAlgorithmIsNotInd = buildEqualAlgorithm();
    notEqualAlgorithmIsNotInd.setInd(false);

    Algorithm notEqualAlgorithmIsNotFd = buildEqualAlgorithm();
    notEqualAlgorithmIsNotFd.setFd(false);

    Algorithm notEqualAlgorithmIsNotUcc = buildEqualAlgorithm();
    notEqualAlgorithmIsNotUcc.setUcc(false);

    Algorithm notEqualAlgorithmIsNotBasicStat = buildEqualAlgorithm();
    notEqualAlgorithmIsNotBasicStat.setBasicStat(false);

    // Execute functionality
    // Check result
    assertTrue(AlgorithmContentEquals.contentEquals(algorithm, equalAlgorithm));
    assertFalse(AlgorithmContentEquals.contentEquals(algorithm, notEqualAlgorithmFileNameNull));
    assertFalse(AlgorithmContentEquals.contentEquals(algorithm, notEqualAlgorithmFileName));
    assertFalse(AlgorithmContentEquals.contentEquals(algorithm, notEqualAlgorithmNameNull));
    assertFalse(AlgorithmContentEquals.contentEquals(algorithm, notEqualAlgorithmName));
    assertFalse(AlgorithmContentEquals.contentEquals(algorithm, notEqualAlgorithmAuthorNull));
    assertFalse(AlgorithmContentEquals.contentEquals(algorithm, notEqualAlgorithmAuthor));
    assertFalse(AlgorithmContentEquals.contentEquals(algorithm, notEqualAlgorithmDescriptionNull));
    assertFalse(AlgorithmContentEquals.contentEquals(algorithm, notEqualAlgorithmDescription));
    assertFalse(AlgorithmContentEquals.contentEquals(algorithm, notEqualAlgorithmIsNotInd));
    assertFalse(AlgorithmContentEquals.contentEquals(algorithm, notEqualAlgorithmIsNotFd));
    assertFalse(AlgorithmContentEquals.contentEquals(algorithm, notEqualAlgorithmIsNotUcc));
    assertFalse(AlgorithmContentEquals.contentEquals(algorithm, notEqualAlgorithmIsNotBasicStat));
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.AlgorithmContentEquals#contentEquals(Algorithm,
   * Algorithm)} <p/> Algorithms that have fields set to null are equal to otherwise equal
   * algorithms that exhibit null values in the same fields.
   */
  @Test
  public void testContentEqualsLeftNull() {
    // Setup
    // Expected values
    Algorithm algorithm = new Algorithm(null);
    Algorithm equalAlgorithm = new Algorithm(null);

    // Execute functionality
    // Check result
    assertTrue(AlgorithmContentEquals.contentEquals(algorithm, equalAlgorithm));
  }

  protected Algorithm buildEqualAlgorithm() {
    String expectedFileName = "some file name";
    String expectedName = "some name";
    String expectedAuthor = "some author";
    String expectedDescription = "some description";
    boolean expectedIsInd = true;
    boolean expectedIsFd = true;
    boolean expectedIsUcc = true;
    boolean expectedIsBasicStat = true;

    Algorithm algorithm = new Algorithm(expectedFileName);
    algorithm.setName(expectedName);
    algorithm.setAuthor(expectedAuthor);
    algorithm.setDescription(expectedDescription);
    algorithm.setInd(expectedIsInd);
    algorithm.setFd(expectedIsFd);
    algorithm.setUcc(expectedIsUcc);
    algorithm.setBasicStat(expectedIsBasicStat);

    return algorithm;
  }
}
