package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.SubSuperSetGraph}
 *
 * @author Jens Hildebrandt
 * @author Jakob Zwiener
 */
public class SubSuperSetGraphTest {

    /**
     * Test method for {@link de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.SubSuperSetGraph#add(ColumnCombinationBitset)}
     *
     * After inserting a column combination a subgraph for every set bit should exist.
     * Add should return the graph after addition.
     */
    @Test
    public void testAdd() {
        // Setup
        SubSuperSetGraph graph = new SubSuperSetGraph();
        ColumnCombinationBitset columnCombination = new ColumnCombinationBitset(2, 4, 7);

        // Execute functionality
        SubSuperSetGraph graphAfterAdd = graph.add(columnCombination);

        // Check result
        // Check existence of column indices in subgraphs by iterating
        SubSuperSetGraph actualSubGraph = graph;
        for (int setColumnIndex : columnCombination.getSetBits()) {
            assertTrue(actualSubGraph.subGraphs.containsKey(setColumnIndex));
            actualSubGraph = actualSubGraph.subGraphs.get(setColumnIndex);
        }
        // Check add return value
        assertSame(graph, graphAfterAdd);
    }

    /**
     * Test method for {@link de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures.SubSuperSetGraph#getExistingSubsets(ColumnCombinationBitset)}
     */
    @Test
    public void testGetExistingSubsets() {
        // Setup
        SubSuperSetGraphFixture fixture = new SubSuperSetGraphFixture();
        SubSuperSetGraph graph = fixture.getGraph();
        ColumnCombinationBitset columnCombinationToQuery = fixture.getColumnCombinationForSubsetQuery();

        // Execute functionality
        List<ColumnCombinationBitset> actualSubsets = graph.getExistingSubsets(columnCombinationToQuery);

        // Check result
        assertThat(actualSubsets,
                IsIterableContainingInAnyOrder.containsInAnyOrder(fixture.getExpectedSubsetsFromQuery()));
    }

    /**
     * Test method for{@link SubSuperSetGraph#isEmpty()}
     */
    @Test
    public void testIsEmpty() {
        // Setup
        SubSuperSetGraph emptyGraph = new SubSuperSetGraph();
        SubSuperSetGraph nonEmptyGraph = new SubSuperSetGraph();
        nonEmptyGraph.add(new ColumnCombinationBitset(10));

        // Execute functionality
        // Check result
        assertTrue(emptyGraph.isEmpty());
        assertFalse(nonEmptyGraph.isEmpty());
    }
}
