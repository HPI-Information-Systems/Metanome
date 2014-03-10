package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import java.util.ArrayList;
import java.util.List;

public class SubSuperSetGraphFixture {

    public SubSuperSetGraph getGraph() {
        SubSuperSetGraph graph = new SubSuperSetGraph();

        for (ColumnCombinationBitset columnCombination : getExpectedIncludedColumnCombinations()) {
            graph.add(columnCombination);
        }

        return graph;
    }

    public List<ColumnCombinationBitset> getExpectedIncludedColumnCombinations() {
        List<ColumnCombinationBitset> includedColumnCombinations = new ArrayList<>();

        includedColumnCombinations.add(new ColumnCombinationBitset(1, 4, 6, 8));
        includedColumnCombinations.add(new ColumnCombinationBitset(1, 3, 4, 6));
        includedColumnCombinations.add(new ColumnCombinationBitset(1, 2, 4, 7));
        includedColumnCombinations.add(new ColumnCombinationBitset(2, 3, 4, 7, 8));
        includedColumnCombinations.add(new ColumnCombinationBitset(5, 6, 8));

        return includedColumnCombinations;
    }

    public ColumnCombinationBitset getColumnCombinationForSubsetQuery() {
        return new ColumnCombinationBitset(1, 2, 3, 4, 5, 6, 8);
    }

    public ColumnCombinationBitset[] getExpectedSubsetsFromQuery() {
        return new ColumnCombinationBitset[]{
                getExpectedIncludedColumnCombinations().get(0),
                getExpectedIncludedColumnCombinations().get(1),
                getExpectedIncludedColumnCombinations().get(4)};
    }
}
