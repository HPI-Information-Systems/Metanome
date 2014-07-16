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

package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import de.uni_potsdam.hpi.metanome.test_helper.EqualsAndHashCodeTester;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.SuperSetGraph}
 *
 * @author Jens Hildebrandt
 */

public class SuperSetGraphTest {

  SuperSetGraphFixture fixture;

  @Before
  public void setup() {
    fixture = new SuperSetGraphFixture();
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.SuperSetGraph#add(ColumnCombinationBitset)}
   * <p/> After inserting a column combination a subgraph for every set bit should exist. Add should
   * return the graph after addition.
   */
  @Test
  public void testAdd() {
    // Setup
    SuperSetGraph graph = new SuperSetGraph(fixture.getNumberOfColumns());
    ColumnCombinationBitset columnCombination = new ColumnCombinationBitset(2, 4, 7);

    // Execute functionality
    SuperSetGraph graphAfterAdd = graph.add(columnCombination);

    // Check result
    // Check existence of column indices in subgraphs by iterating
    SubSetGraph actualSubGraph = graph.graph;
    for (int setColumnIndex : columnCombination.invert(fixture.getNumberOfColumns()).getSetBits()) {
      assertTrue(actualSubGraph.subGraphs.containsKey(setColumnIndex));
      actualSubGraph = actualSubGraph.subGraphs.get(setColumnIndex);
    }

    // Check add return value
    assertSame(graph, graphAfterAdd);
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.SuperSetGraph#addAll(java.util.Collection)}
   * <p/> After inserting all column combinations the graph should be equal to the expected graph
   * from the fixture. AddAll should return the graph after addition.
   */
  @Test
  public void testAddAll() {
    // Setup
    SuperSetGraph graph = new SuperSetGraph(fixture.getNumberOfColumns());
    // Expected values
    Collection<ColumnCombinationBitset>
        expectedColumnCombinations =
        fixture.getExpectedIncludedColumnCombinations();
    SuperSetGraph expectedGraph = fixture.getGraph();

    // Execute functionality
    SuperSetGraph graphAfterAddAll = graph.addAll(expectedColumnCombinations);

    // Check result
    assertEquals(expectedGraph, graph);
    assertSame(graph, graphAfterAddAll);
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.SuperSetGraph#getExistingSupersets(ColumnCombinationBitset)}
   */
  @Test
  public void testGetExistingSupersets() {
    // Setup
    SuperSetGraph graph = fixture.getGraph();
    ColumnCombinationBitset
        columnCombinationToQuery =
        fixture.getColumnCombinationForSupersetQuery();

    // Execute functionality
    List<ColumnCombinationBitset>
        actualSubsets =
        graph.getExistingSupersets(columnCombinationToQuery);

    // Check result
    assertThat(actualSubsets,
               IsIterableContainingInAnyOrder
                   .containsInAnyOrder(fixture.getExpectedSupersetsFromQuery()));
  }

  /**
   * Test method for {@link SuperSetGraph#getExistingSupersets(ColumnCombinationBitset)}
   *
   * This tests a special case of an empty graph. An empty list should be returned
   */
  @Test
  public void testGetExistingSupersetsOnEmptyGraph() {
    // Setup
    SuperSetGraph graph = new SuperSetGraph(fixture.getNumberOfColumns());

    // Execute functionality
    List<ColumnCombinationBitset>
        actualSubsets =
        graph.getExistingSupersets(new ColumnCombinationBitset(1, 3, 5));

    // Check result
    assertTrue(actualSubsets.isEmpty());
  }

  /**
   * Test for the method {@link de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.SuperSetGraph#containsSuperset(ColumnCombinationBitset)}
   * )}
   */
  @Test
  public void testContainsSubset() {
    //Setup
    SuperSetGraph actualGraph = fixture.getGraph();

    //Execute functionality
    assertTrue(
        actualGraph.containsSuperset(fixture.getExpectedIncludedColumnCombinations().get(0)));
    assertTrue(actualGraph.containsSuperset(fixture.getColumnCombinationForSupersetQuery()));
    assertFalse(actualGraph.containsSuperset(new ColumnCombinationBitset(1, 2, 3, 5, 8, 9)));
    //Check Result
  }

  /**
   * Test for the method {@link SuperSetGraph#containsSuperset(ColumnCombinationBitset)}
   *
   * This test tests a special case of a empty graph. False should be returned.
   */
  @Test
  public void testContainsSupersetOnEmptyGraph() {
    //Setup
    SuperSetGraph actualGraph = new SuperSetGraph(fixture.getNumberOfColumns());

    //Execute functionality
    assertFalse(actualGraph.containsSuperset(new ColumnCombinationBitset(1, 3)));
    //Check Result
  }

  /**
   * Test method for {@link SuperSetGraph#isEmpty()}
   */
  @Test
  public void testIsEmpty() {
    // Setup
    SuperSetGraph emptyGraph = new SuperSetGraph(fixture.getNumberOfColumns());
    SuperSetGraph nonEmptyGraph = new SuperSetGraph(fixture.getNumberOfColumns());
    nonEmptyGraph.add(new ColumnCombinationBitset(10));

    // Execute functionality
    // Check result
    assertTrue(emptyGraph.isEmpty());
    assertFalse(nonEmptyGraph.isEmpty());
  }

  /**
   * Test method  {@link SuperSetGraph#equals(Object)} and {@link de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.SuperSetGraph#hashCode()}
   */
  @Test
  public void testEqualsAndHashCode() {
    // Setup
    SuperSetGraph actualGraph = new SuperSetGraph(fixture.getNumberOfColumns());
    SuperSetGraph equalsGraph = new SuperSetGraph(fixture.getNumberOfColumns());
    SuperSetGraph notEqualsGraph = new SuperSetGraph(25);

    actualGraph.add(new ColumnCombinationBitset(2, 5, 9));
    actualGraph.add((new ColumnCombinationBitset(2, 5, 8)));

    equalsGraph.add((new ColumnCombinationBitset(2, 5, 8)));
    equalsGraph.add(new ColumnCombinationBitset(2, 5, 9));

    notEqualsGraph.add(new ColumnCombinationBitset(2, 5, 12, 20));
    notEqualsGraph.add((new ColumnCombinationBitset(2, 5, 10, 15)));

    // Execute functionality
    // Check result
    EqualsAndHashCodeTester<SuperSetGraph> tester = new EqualsAndHashCodeTester<>();
    tester.performBasicEqualsAndHashCodeChecks(actualGraph, equalsGraph, notEqualsGraph);
  }
}