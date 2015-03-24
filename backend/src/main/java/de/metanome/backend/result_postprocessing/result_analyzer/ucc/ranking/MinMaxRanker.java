package de.metanome.backend.result_postprocessing.result_analyzer.ucc.ranking;

import de.metanome.backend.result_postprocessing.io_helper.TableInformation;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;


import java.util.List;

/**
 * Created by Tommy Neubert on 10.12.2014.
 */
public class MinMaxRanker implements UCCRanker {

    @Override
    public double calculate(ColumnCombination ucc, TableInformation ti, List<ColumnCombination> uccs) {
        List<Integer> indices = ucc.getColumnIndices();
        long value = 1;
        for(Integer i : indices) {
            value = value * ti.getColumn(i).getDistinctValuesCount();
        }
        long min = ti.getRowCount();
        for(int i = 0; i < ti.getColumnCount(); i++) {
            min = Math.min(ti.getRowCount(), min);
        }
        double tmpMax = Math.pow(min-1, ucc.getColumnCount());
        long max = (long) tmpMax;
        return Double.valueOf(value - min) / Double.valueOf(max - min);
    }
}
