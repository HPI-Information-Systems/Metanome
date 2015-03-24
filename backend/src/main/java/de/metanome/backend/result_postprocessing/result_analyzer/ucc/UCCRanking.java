package de.metanome.backend.result_postprocessing.result_analyzer.ucc;

import de.metanome.backend.result_postprocessing.io_helper.TableInformation;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;
import de.metanome.backend.result_postprocessing.result_analyzer.ucc.ranking.MinMaxRanker;
import de.metanome.backend.result_postprocessing.result_analyzer.ucc.ranking.OccurrenceRanker;
import de.metanome.backend.result_postprocessing.result_analyzer.ucc.ranking.UCCRanker;

import java.util.*;

/**
 * Created by Tommy Neubert on 10.12.2014.
 */
public class UCCRanking {

    private static enum ORDER {
        ASC,
        DESC
    }

    public static enum RANKING {
        MIN_MAX(new MinMaxRanker(), ORDER.ASC),
        OCCURRENCE(new OccurrenceRanker(), ORDER.DESC);

        private UCCRanker ranker;
        private ORDER order;

        private RANKING(UCCRanker ranker, ORDER order) {
            this.ranker = ranker;
            this.order = order;
        }

        public UCCRanker getRanker() {
            return this.ranker;
        }

        public ORDER getOrder() {
            return this.order;
        }
    }

    private List<ColumnCombination> uccs;
    private TableInformation ti;
    private RANKING ranking;

    public UCCRanking(RANKING ranking, List<ColumnCombination> uccs, TableInformation ti) {
        this.uccs = uccs;
        this.ti = ti;
        this.ranking = ranking;
    }

    public List<RankingResult> getResult() {
        Map<Double, List<ColumnCombination>> valuedUccs = new HashMap<Double, List<ColumnCombination>>();
        for(ColumnCombination cc : this.uccs) {
            Double value = this.ranking.getRanker().calculate(cc, this.ti, this.uccs);
            if(!valuedUccs.containsKey(value)) {
                valuedUccs.put(value, new LinkedList<ColumnCombination>());
            }
            valuedUccs.get(value).add(cc);
        }
        Set<Double> values = valuedUccs.keySet();
        List<Double> sorted = new LinkedList<Double>(values);
        Collections.sort(sorted);
        if(this.ranking.getOrder() == ORDER.DESC) {
            Collections.reverse(sorted);
        }
        List<RankingResult> result = new LinkedList<RankingResult>();
        for(int i = 0; i < sorted.size(); i++) {
            double value = sorted.get(i);
            for (ColumnCombination cc : valuedUccs.get(value)) {
                result.add(new RankingResult(cc, i + 1, value));
            }
        }
        return result;
    }

    public static class RankingResult {
        private double normalizedValue;
        private long rank;
        private ColumnCombination ucc;

        public RankingResult(ColumnCombination ucc, long rank, double normalizedValue) {
            this.normalizedValue = normalizedValue;
            this.rank = rank;
            this.ucc = ucc;
        }

        public double getNormalizedValue() {
            return normalizedValue;
        }

        public long getRank() {
            return rank;
        }

        public ColumnCombination getUcc() {
            return ucc;
        }

        public static double normalize(double value, long minValue, long maxValue) {
            if(maxValue > minValue) {
                return (value - minValue) / (maxValue - minValue);
            }
            return -1;
        }
    }
}
