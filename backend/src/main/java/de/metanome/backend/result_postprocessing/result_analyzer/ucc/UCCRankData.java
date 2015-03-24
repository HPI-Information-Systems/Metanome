package de.metanome.backend.result_postprocessing.result_analyzer.ucc;

import de.metanome.backend.result_postprocessing.io_helper.ColumnInformation;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;

import java.util.*;

/**
 * Needed to carry all data over to google charts
 *
 * Created by Tommy Neubert on 07.01.2015.
 */
public class UCCRankData {
    private long rank;
    private String ucc;
    private double value;
    private double min;
    private double max;
    private long length;
    private String occurencesString;
    private double avgOccurence;

    public UCCRankData(long rank, ColumnCombination ucc, double value, double min, double max, List<ColumnCombination> allUccs, List<ColumnInformation> info) {
        this.rank = rank;
        this.ucc = this.createNamedUcc(ucc, info);
        this.length = ucc.getColumnCount();
        this.value = value;
        this.min = min;
        this.max = max;
        Map<Integer, Long> occurencesMap = this.calculateOccurences(ucc, allUccs);
        this.occurencesString = this.convertToReadableString(occurencesMap, info);
        long count = 0;
        for(Integer index : occurencesMap.keySet()) {
            count += occurencesMap.get(index);
        }
        this.avgOccurence = ((double) count) / ((double) occurencesMap.keySet().size());
    }

    private String createNamedUcc(ColumnCombination ucc, List<ColumnInformation> info) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(Integer index : ucc.getColumnIndices()) {
            if (!first) {
                sb.append("<br>");
            }
            first = false;
            sb.append("<a href=\"piechart.html?column=" + info.get(index).getColumnName() + "\">" + info.get(index).getColumnName() + "</a>");
        }
        return sb.toString();
    }

    private String convertToReadableString(Map<Integer, Long> occurencesMap, List<ColumnInformation> info) {
        StringBuilder builder = new StringBuilder();
        builder.append("Column: Count");
        List<Integer> keys = new ArrayList<Integer>();
        keys.addAll(occurencesMap.keySet());
        Collections.sort(keys);
        for(Integer column : keys) {
            builder.append("<br>").append(info.get(column).getColumnName()).append(": ").append(occurencesMap.get(column));
        }
        return builder.toString();
    }

    private Map<Integer, Long> calculateOccurences(ColumnCombination ucc, List<ColumnCombination> allUccs) {
        Map<Integer, Long> occurences = new HashMap<Integer, Long>();
        for(ColumnCombination otherUcc : allUccs) {
            for(Integer cIndex : ucc.getColumnIndices()) {
                if(otherUcc.getColumnIndices().contains(cIndex)) {
                    if(!occurences.containsKey(cIndex)) {
                        occurences.put(cIndex, 0l);
                    }
                    occurences.put(cIndex, occurences.get(cIndex) + 1);
                }
            }
        }
        return occurences;
    }

    public long getRank() {
        return rank;
    }

    public String getUcc() {
        return ucc;
    }

    public double getValue() {
        return value;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public long getLength() {
        return length;
    }

    public String getOccurences() {
        return occurencesString;
    }

    public double getAvgOccurence() {
       return avgOccurence;
    }
}
