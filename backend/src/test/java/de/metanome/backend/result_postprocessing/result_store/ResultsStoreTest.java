/*
 * Copyright 2015 by the Metanome project
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

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ResultsStoreTest {

  ColumnPermutation expectedDependant1 = new ColumnPermutation(
      new ColumnIdentifier("table1", "column1"));
  ColumnPermutation expectedReferenced1 = new ColumnPermutation(
      new ColumnIdentifier("table1", "column2"));
  InclusionDependency ind1 = new InclusionDependency(expectedDependant1, expectedReferenced1);

  ColumnPermutation expectedDependant2 = new ColumnPermutation(
      new ColumnIdentifier("table1", "column2"));
  ColumnPermutation expectedReferenced2 = new ColumnPermutation(
      new ColumnIdentifier("table1", "column1"));
  InclusionDependency ind2 = new InclusionDependency(expectedDependant2, expectedReferenced2);

  ColumnPermutation expectedDependant3 = new ColumnPermutation(
      new ColumnIdentifier("table1", "column3"));
  ColumnPermutation expectedReferenced3 = new ColumnPermutation(
      new ColumnIdentifier("table1", "column3"));
  InclusionDependency ind3 = new InclusionDependency(expectedDependant3, expectedReferenced3);

  @Test
  public void testStoreAndList() {
    // Set Up
    List<InclusionDependency> expectedResults = new ArrayList<>();
    expectedResults.add(ind1);
    expectedResults.add(ind2);

    InclusionDependencyResultsStore resultsStore = new InclusionDependencyResultsStore();

    // Execute functionality
    resultsStore.store(expectedResults);
    List<InclusionDependency> actualResults = resultsStore.list();

    // Check
    assertEquals(expectedResults, actualResults);
  }

  @Test
  public void testCount() {
    // Set Up
    List<InclusionDependency> expectedResults = new ArrayList<>();
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
    List<InclusionDependency> results = new ArrayList<>();
    results.add(ind1);
    results.add(ind2);

    InclusionDependencyResultsStore resultsStore = new InclusionDependencyResultsStore();
    resultsStore.store(results);

    // Execute functionality
    List<InclusionDependency> actualResults = resultsStore.subList("referencedAsString", true,
                                                                   1, 2);

    // Check
    assertEquals(1, actualResults.size());
    assertEquals(ind2, actualResults.get(0));
  }

  @Test
  public void testSubList2() {
    // Set Up
    List<InclusionDependency> results = new ArrayList<>();
    results.add(ind1);
    results.add(ind2);
    results.add(ind3);

    InclusionDependencyResultsStore resultsStore = new InclusionDependencyResultsStore();
    resultsStore.store(results);

    // Execute functionality
    List<InclusionDependency> actualResults =
        resultsStore.subList(InclusionDependencyResultComparator.REFERENCED_COLUMN, true, 1, 2);

    // Check
    assertEquals(1, actualResults.size());
    assertEquals(ind1, actualResults.get(0));

    // Execute functionality
    actualResults = resultsStore.subList(InclusionDependencyResultComparator.REFERENCED_COLUMN, true, 0, 3);

    // Check
    assertEquals(3, actualResults.size());
    assertEquals(ind2, actualResults.get(0));
  }

}
