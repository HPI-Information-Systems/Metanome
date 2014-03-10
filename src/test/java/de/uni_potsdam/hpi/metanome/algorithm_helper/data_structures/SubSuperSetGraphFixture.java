package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

/**
 * TODO docs
 *
 * @author Jens Hildebrandt
 * @author Jakob Zwiener
 */
public class SubSuperSetGraphFixture {

    public SubSuperSetGraph getGraph() {
        SubSuperSetGraph graph = new SubSuperSetGraph();

        graph.add(getColumnCombination0());
        graph.add(getColumnCombination1());
        graph.add(new ColumnCombinationBitset(1, 2, 4, 7));
        graph.add(new ColumnCombinationBitset(2, 3, 4, 7, 8));
        graph.add(getColumnCombination4());

        return graph;
    }

    protected ColumnCombinationBitset getColumnCombination0() {
        return new ColumnCombinationBitset(1, 4, 6, 8);
    }

    protected ColumnCombinationBitset getColumnCombination1() {
        return new ColumnCombinationBitset(1, 3, 4, 6);
    }

    protected ColumnCombinationBitset getColumnCombination4() {
        return new ColumnCombinationBitset(5, 6, 8);
    }

    public ColumnCombinationBitset getColumnCombinationForSubsetQuery() {
        return new ColumnCombinationBitset(1, 2, 3, 4, 5, 6, 8);
    }

    public ColumnCombinationBitset[] getExpectedSubsetsFromQuery() {
        return new ColumnCombinationBitset[]{getColumnCombination0(), getColumnCombination1(), getColumnCombination4()};
    }
}
