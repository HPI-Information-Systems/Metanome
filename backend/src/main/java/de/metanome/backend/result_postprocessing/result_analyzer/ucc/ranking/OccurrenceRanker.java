package de.metanome.backend.result_postprocessing.result_analyzer.ucc.ranking;


import de.metanome.backend.result_postprocessing.io_helper.TableInformation;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tommy Neubert on 10.12.2014.
 */
public class OccurrenceRanker implements UCCRanker {
    @Override
    public double calculate(ColumnCombination ucc, TableInformation ti, List<ColumnCombination> uccs) {
        Map<Integer, Long> occurrences = new HashMap<Integer, Long>();
        for(int i = 0; i < ti.getColumnCount(); i++) {
            occurrences.put(i, 0l);
        }
        for(ColumnCombination cc : uccs) {
            for(Integer index : cc.getColumnIndices()) {
                occurrences.put(index, occurrences.get(index) + 1);
            }
        }
        long occurrenceSum = 0;
        for(Integer index : ucc.getColumnIndices()) {
            occurrenceSum = occurrenceSum + occurrences.get(index);
        }
        return Double.valueOf(occurrenceSum) / Double.valueOf(ucc.getColumnIndices().size() * uccs.size());
    }
}
