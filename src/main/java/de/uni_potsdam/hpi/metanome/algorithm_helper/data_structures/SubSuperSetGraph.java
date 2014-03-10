package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

/**
 * A graph that allows efficient lookup of all subsets and supersets in the graph for a given ColumnCombinationBitset.
 * TODO describe minimality
 *
 * @author Jens Hildebrandt
 * @author Jakob Zwiener
 */
public class SubSuperSetGraph {

    protected Int2ObjectMap<SubSuperSetGraph> subGraphs = new Int2ObjectOpenHashMap<>();

    /**
     * Adds a column combination to the graph. Returns the graph after adding.
     *
     * @param columnCombination a column combination to add
     */
    public SubSuperSetGraph add(ColumnCombinationBitset columnCombination) {
        SubSuperSetGraph subGraph = this;

        for (int setColumnIndex : columnCombination.getSetBits()) {
            subGraph = subGraph.lazySubGraphGeneration(setColumnIndex);
        }

        return this;
    }

    /**
     * Looks for the subgraph or builds and adds a new one.
     *
     * @param setColumnIndex the column index to perform the lookup on
     * @return the subgraph behind the column index
     */
    protected SubSuperSetGraph lazySubGraphGeneration(int setColumnIndex) {
        SubSuperSetGraph subGraph = subGraphs.get(setColumnIndex);

        if (subGraph == null) {
            subGraph = new SubSuperSetGraph();
            subGraphs.put(setColumnIndex, subGraph);
        }

        return subGraph;
    }

}
