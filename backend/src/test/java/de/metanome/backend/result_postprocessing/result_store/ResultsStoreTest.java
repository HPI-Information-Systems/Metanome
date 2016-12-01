/**
 * Copyright 2015-2016 by Metanome Project
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
package de.metanome.backend.result_postprocessing.result_store;

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.ColumnPermutation;
import de.metanome.algorithm_integration.results.InclusionDependency;
import de.metanome.backend.result_postprocessing.result_comparator.InclusionDependencyResultComparator;
import de.metanome.backend.result_postprocessing.results.InclusionDependencyResult;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ResultsStoreTest {

  InclusionDependencyResult ind1;
  InclusionDependencyResult ind2;
  InclusionDependencyResult ind3;

  @Before
  public void setUp() {
    ColumnPermutation expectedDependant1 = new ColumnPermutation(
      new ColumnIdentifier("table1", "column1"));
    ColumnPermutation expectedReferenced1 = new ColumnPermutation(
      new ColumnIdentifier("table1", "column2"));
    InclusionDependency result1 = new InclusionDependency(expectedDependant1, expectedReferenced1);
    ind1 = new InclusionDependencyResult(result1);

    ColumnPermutation expectedDependant2 = new ColumnPermutation(
      new ColumnIdentifier("table1", "column2"));
    ColumnPermutation expectedReferenced2 = new ColumnPermutation(
      new ColumnIdentifier("table1", "column1"));
    InclusionDependency result2 = new InclusionDependency(expectedDependant2, expectedReferenced2);
    ind2 = new InclusionDependencyResult(result2);

    ColumnPermutation expectedDependant3 = new ColumnPermutation(
      new ColumnIdentifier("table1", "column3"));
    ColumnPermutation expectedReferenced3 = new ColumnPermutation(
      new ColumnIdentifier("table1", "column3"));
    InclusionDependency result3 = new InclusionDependency(expectedDependant3, expectedReferenced3);
    ind3 = new InclusionDependencyResult(result3);
  }

  @Test
  public void testList() {
    // Set Up
    List<InclusionDependencyResult> expectedResults = new ArrayList<>();
    expectedResults.add(ind1);
    expectedResults.add(ind2);

    InclusionDependencyResultsStore resultsStore = new InclusionDependencyResultsStore();

    // Execute functionality
    resultsStore.store(expectedResults);
    List<InclusionDependencyResult> actualResults = resultsStore.list();

    // Check
    assertEquals(expectedResults, actualResults);
  }

  @Test
  public void testClear() {
    // Set Up
    List<InclusionDependencyResult> expectedResults = new ArrayList<>();
    expectedResults.add(ind1);
    expectedResults.add(ind2);

    InclusionDependencyResultsStore resultsStore = new InclusionDependencyResultsStore();

    resultsStore.store(expectedResults);
    int count = resultsStore.count();

    // Check precondition
    assertTrue(count > 0);

    // Execute functionality
    resultsStore.clear();
    count = resultsStore.count();

    // Check
    assertEquals(0, count);
  }

  @Test
  public void testCount() {
    // Set Up
    List<InclusionDependencyResult> expectedResults = new ArrayList<>();
    expectedResults.add(ind1);
    expectedResults.add(ind2);

    InclusionDependencyResultsStore resultsStore = new InclusionDependencyResultsStore();

    // Execute functionality
    resultsStore.store(expectedResults);
    int count = resultsStore.count();

    // Check
    assertEquals(expectedResults.size(), count);
  }

  @Test
  public void testSubList1() {
    // Set Up
    List<InclusionDependencyResult> results = new ArrayList<>();
    results.add(ind1);
    results.add(ind2);

    InclusionDependencyResultsStore resultsStore = new InclusionDependencyResultsStore();
    resultsStore.store(results);

    // Execute functionality
    List<InclusionDependencyResult> actualResults = resultsStore.subList("referencedAsString", true,
      1, 2);

    // Check
    assertEquals(1, actualResults.size());
    assertEquals(ind2, actualResults.get(0));
  }

  @Test
  public void testSubList2() {
    // Set Up
    List<InclusionDependencyResult> results = new ArrayList<>();
    results.add(ind1);
    results.add(ind2);
    results.add(ind3);

    InclusionDependencyResultsStore resultsStore = new InclusionDependencyResultsStore();
    resultsStore.store(results);

    // Execute functionality
    List<InclusionDependencyResult> actualResults =
      resultsStore.subList(InclusionDependencyResultComparator.REFERENCED_COLUMN, true, 1, 2);

    // Check
    assertEquals(1, actualResults.size());
    assertEquals(ind1, actualResults.get(0));

    // Execute functionality
    actualResults =
      resultsStore.subList(InclusionDependencyResultComparator.REFERENCED_COLUMN, true, 0, 3);

    // Check
    assertEquals(3, actualResults.size());
    assertEquals(ind2, actualResults.get(0));
  }

}
