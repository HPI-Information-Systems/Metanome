package de.metanome.backend.result_postprocessing.result_analyzer.inclusion_dependencies;

import de.metanome.backend.result_postprocessing.result_analyzer.ResultComparator;
import de.metanome.backend.result_postprocessing.result_analyzer.ResultsStore;

/**
 * Stores IND ranking results of one execution
 *
 * Created by Alexander Spivak on 05.03.2015.
 */
public class INDResultsStore extends ResultsStore<INDResult> {

  /**
   * Defines an IND result comparator
   *
   * @param sortProperty Sort property
   * @param ascending Sort direction
   * @return Returns a new IND result comparator
   */
  @Override
  protected ResultComparator<INDResult> getResultComparator(String sortProperty,
                                                            boolean ascending) {
    return new INDResultComparator(sortProperty, ascending);
  }
}
