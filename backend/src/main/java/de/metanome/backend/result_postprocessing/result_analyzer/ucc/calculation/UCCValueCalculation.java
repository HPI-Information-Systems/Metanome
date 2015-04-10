package de.metanome.backend.result_postprocessing.result_analyzer.ucc.calculation;

import de.metanome.backend.result_postprocessing.io_helper.TableInformation;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;
import de.metanome.backend.result_postprocessing.result_analyzer.ucc.UCCResult;

import javax.persistence.Column;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tommy Neubert on 11.03.2015.
 */
public class UCCValueCalculation {

    public static enum VALUES {
        MIN,
        MAX,
        AVG_OCC,
        RND
    }

    private TableInformation ti;
    private List<ColumnCombination> uccs;
    private Map<Integer, Long> occurrences = null;

    public UCCValueCalculation(TableInformation ti, List<ColumnCombination> uccs) {
        this.ti = ti;
        this.uccs = uccs;
        this.occurrences = this.generateOccurrenceMap();
    }

    public UCCValueCalculation(List<UCCResult> uccs, TableInformation ti) {
        this.ti = ti;
        this.uccs = new LinkedList<ColumnCombination>();
        for(UCCResult result : uccs) {
            this.uccs.add(result.getUcc());
        }
        this.occurrences = this.generateOccurrenceMap();
    }

    public Map<VALUES, Object> calculate(ColumnCombination ucc, boolean useRowData) {
        Map<VALUES, Object> values = new HashMap<VALUES, Object>();
        this.calculateAvgOccurrence(ucc, values);

        if(useRowData) {
            this.calculateMinMaxRandomnessValues(ucc, values);
        }

        return values;
    }

    private void calculateMinMaxRandomnessValues(ColumnCombination ucc, Map<VALUES, Object> values) {
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
        values.put(VALUES.MIN, Double.valueOf(min));
        values.put(VALUES.MAX, Double.valueOf(max));
        values.put(VALUES.RND, Double.valueOf(value - min) / Double.valueOf(max - min));
    }

    private void calculateAvgOccurrence(ColumnCombination ucc, Map<VALUES, Object> values) {
        long occurrenceSum = 0;
        for(Integer index : ucc.getColumnIndices()) {
            occurrenceSum = occurrenceSum + occurrences.get(index);
        }
        values.put(VALUES.AVG_OCC, Double.valueOf(occurrenceSum) / Double.valueOf(ucc.getColumnIndices().size() * uccs.size()));
    }

    private Map<Integer, Long> generateOccurrenceMap() {
        Map<Integer, Long> occurrences = new HashMap<Integer, Long>();
        for(int i = 0; i < this.ti.getColumnCount(); i++) {
            occurrences.put(i, 0l);
        }
        for(ColumnCombination cc : uccs) {
            for(Integer index : cc.getColumnIndices()) {
                occurrences.put(index, occurrences.get(index) + 1);
            }
        }
        return occurrences;
    }

}
