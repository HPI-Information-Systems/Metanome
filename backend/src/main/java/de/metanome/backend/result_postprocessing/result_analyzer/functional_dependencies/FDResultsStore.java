package de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies;

import de.metanome.backend.result_postprocessing.result_analyzer.ResultComparator;
import de.metanome.backend.result_postprocessing.result_analyzer.ResultsStore;

/**
 * Stores FD ranking results of one execution
 *
 * Created by Alexander Spivak on 05.03.2015.
 */
public class FDResultsStore extends ResultsStore<FDResult> {

  /**
   * Defines a FD result comparator
   *
   * @param sortProperty Sort property
   * @param ascending Sort direction
   * @return Returns a new FD result comparator
   */
  @Override
  protected ResultComparator<FDResult> getResultComparator(String sortProperty, boolean ascending) {
    return new FDResultComparator(sortProperty, ascending);
  }
}
