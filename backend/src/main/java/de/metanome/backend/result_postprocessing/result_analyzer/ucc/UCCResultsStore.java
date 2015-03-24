package de.metanome.backend.result_postprocessing.result_analyzer.ucc;

import de.metanome.backend.result_postprocessing.result_analyzer.ResultComparator;
import de.metanome.backend.result_postprocessing.result_analyzer.ResultsStore;

/**
 * Created by Alexander Spivak on 11.03.2015.
 */
public class UCCResultsStore extends ResultsStore<UCCResult> {

  @Override
  protected ResultComparator<UCCResult> getResultComparator(String sortProperty,
                                                            boolean ascending) {
    return new UCCResultComparator(sortProperty, ascending);
  }
}
