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
import de.metanome.backend.result_postprocessing.results.InclusionDependencyResult;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ResultsStoreHolderTest {

  @Test
  public void testGetStore() {
    // Set up
    InclusionDependencyResultsStore expectedStore = new InclusionDependencyResultsStore();

    // Execute functionality
    ResultsStoreHolder.register("IND", expectedStore);
    ResultsStore<?> actualStore = ResultsStoreHolder.getStore("IND");

    // Check
    assertEquals(InclusionDependencyResultsStore.class, actualStore.getClass());
    assertEquals(expectedStore, actualStore);
  }

  @Test
  public void testClearStores() {
    // Set up
    ColumnPermutation expectedDependant1 = new ColumnPermutation(
      new ColumnIdentifier("table1", "column1"));
    ColumnPermutation expectedReferenced1 = new ColumnPermutation(
      new ColumnIdentifier("table1", "column2"));
    InclusionDependency result = new InclusionDependency(expectedDependant1, expectedReferenced1);
    InclusionDependencyResult ind1 = new InclusionDependencyResult(result);

    List<InclusionDependencyResult> expectedResults = new ArrayList<>();
    expectedResults.add(ind1);

    InclusionDependencyResultsStore resultsStore = new InclusionDependencyResultsStore();
    resultsStore.store(expectedResults);

    // Execute functionality
    ResultsStoreHolder.register("IND", resultsStore);

    // Check precondition
    assertTrue(ResultsStoreHolder.resultsStoreMap.size() > 0);
    assertTrue(ResultsStoreHolder.getStore("IND").count() > 0);

    // Execute functionality
    ResultsStoreHolder.clearStores();

    // Check
    assertTrue(ResultsStoreHolder.resultsStoreMap.size() > 0);
    assertTrue(ResultsStoreHolder.getStore("IND").count() == 0);
  }

}
