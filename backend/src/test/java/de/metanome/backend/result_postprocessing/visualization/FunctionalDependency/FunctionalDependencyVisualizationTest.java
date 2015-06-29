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

package de.metanome.backend.result_postprocessing.visualization.FunctionalDependency;

import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;

import org.junit.Test;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;

public class FunctionalDependencyVisualizationTest {

  @Test
  public void testGet() {
    // Set Up
    Set<ColumnCombination> columnCombinations = new TreeSet<>();

    ColumnIdentifier expectedColumnIdentifier = new ColumnIdentifier("expect", "expect");

    ColumnCombination combination1 = new ColumnCombination(
        new ColumnIdentifier("1", "1"), new ColumnIdentifier("1", "2"));
    ColumnCombination combination2 = new ColumnCombination(
        expectedColumnIdentifier, new ColumnIdentifier("f", "f"));
    ColumnCombination combination3 = new ColumnCombination(
        new ColumnIdentifier("1", "5"), new ColumnIdentifier("2", "2"), expectedColumnIdentifier);

    columnCombinations.add(combination1);
    columnCombinations.add(combination2);
    columnCombinations.add(combination3);

    // Execute functionality
    ColumnIdentifier actualColumnIdentifier = FunctionalDependencyVisualization.get(columnCombinations, 2, 2);
    // Check
    assertEquals(expectedColumnIdentifier, actualColumnIdentifier);

    // Execute functionality
    actualColumnIdentifier = FunctionalDependencyVisualization.get(columnCombinations, 1, 0);
    // Check
    assertEquals(expectedColumnIdentifier, actualColumnIdentifier);
  }

}
