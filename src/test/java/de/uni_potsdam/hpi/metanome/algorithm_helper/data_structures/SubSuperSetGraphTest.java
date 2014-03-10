package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

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
     */
    @Test
    public void testAdd() {
        // Setup
        SubSuperSetGraph graph = new SubSuperSetGraph();
        ColumnCombinationBitset columnCombination = new ColumnCombinationBitset(2, 4, 7);

        // Execute functionality
        graph.add(columnCombination);

        // Check result
        // Check existence of column indices in subgraphs by iterating
        SubSuperSetGraph actualSubGraph = graph;
        for (int setColumnIndex : columnCombination.getSetBits()) {
            assertTrue(actualSubGraph.subGraphs.containsKey(setColumnIndex));
            actualSubGraph = actualSubGraph.subGraphs.get(setColumnIndex);
        }
    }
}
