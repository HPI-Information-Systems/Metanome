package de.metanome.backend.result_postprocessing.result_analyzer.ucc.ranking;

import de.metanome.backend.result_postprocessing.io_helper.TableInformation;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;

import java.util.List;

/**
 * Created by Tommy Neubert on 10.12.2014.
 */
public interface UCCRanker {

    public double calculate(ColumnCombination ucc, TableInformation ti, List<ColumnCombination> uccs);

}
