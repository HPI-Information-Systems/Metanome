package de.metanome.backend.result_postprocessing.result_store;

import de.metanome.backend.result_postprocessing.result_comparator.InclusionDependencyResultComparator;
import de.metanome.backend.result_postprocessing.result_comparator.ResultComparator;
import de.metanome.backend.result_postprocessing.results.InclusionDependencyResult;

/**
 * Stores Inclusion Dependency results of one execution.
 */
public class InclusionDependencyResultsStore extends ResultsStore<InclusionDependencyResult> {

  /**
   * Defines an Inclusion Dependency result comparator
   *
   * @param sortProperty Sort property
   * @param ascending    Sort direction
   * @return Returns a new Inclusion Dependency result comparator
   */
  @Override
  protected ResultComparator<InclusionDependencyResult> getResultComparator(String sortProperty,
                                                                            boolean ascending) {
    return new InclusionDependencyResultComparator(sortProperty, ascending);
  }
}
