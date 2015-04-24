package de.metanome.backend.result_postprocessing.result_comparator;

import de.metanome.algorithm_integration.results.InclusionDependency;

/**
 * Defines an Inclusion Dependency comparator based on a predefined sort property and sort direction
 * order.
 */
public class InclusionDependencyResultComparator extends ResultComparator<InclusionDependency> {

  /**
   * Creates an Inclusion Dependency result comparator for given property and direction
   *
   * @param sortProperty Sort property
   * @param isAscending  Sort direction
   */
  public InclusionDependencyResultComparator(String sortProperty, boolean isAscending) {
    super(sortProperty, isAscending);
  }

  /**
   * Compares two given Inclusion Dependency results depending on given sort property
   *
   * @param ind1         Left IND result
   * @param ind2         Right IND result
   * @param sortProperty Sort property
   * @return Returns 1 if ind1 is greater than ind2, 0 if both are equal, -1 otherwise
   */
  @Override
  protected int compare(InclusionDependency ind1, InclusionDependency ind2, String sortProperty) {
    if ("dependantAsString".equals(sortProperty)) {
      return ind1.getDependant().toString().compareTo(ind2.getDependant().toString());
    }
    if ("referencedAsString".equals(sortProperty)) {
      return ind1.getReferenced().toString().compareTo(ind2.getReferenced().toString());
    }

    return 0;
  }


}
