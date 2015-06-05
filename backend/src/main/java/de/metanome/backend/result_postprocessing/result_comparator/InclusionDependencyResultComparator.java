package de.metanome.backend.result_postprocessing.result_comparator;

import de.metanome.backend.result_postprocessing.results.InclusionDependencyResult;

/**
 * Defines an inclusion dependency comparator based on a predefined sort property and sort direction
 * order.
 */
public class InclusionDependencyResultComparator
    extends ResultComparator<InclusionDependencyResult> {

  public static final String DEPENDANT_COLUMN = "dependant";
  public static final String REFERENCED_COLUMN = "referenced";

  /**
   * Creates an inclusion dependency result comparator for given property and direction
   *
   * @param sortProperty Sort property
   * @param isAscending  Sort direction
   */
  public InclusionDependencyResultComparator(String sortProperty, boolean isAscending) {
    super(sortProperty, isAscending);
  }

  /**
   * Compares two given inclusion dependency results depending on given sort property
   *
   * @param ind1         inclusion dependency result
   * @param ind2         other inclusion dependency result
   * @param sortProperty Sort property
   * @return Returns 1 if ind1 is greater than ind2, 0 if both are equal, -1 otherwise
   */
  @Override
  protected int compare(InclusionDependencyResult ind1, InclusionDependencyResult ind2,
                        String sortProperty) {
    if (DEPENDANT_COLUMN.equals(sortProperty)) {
      return ind1.getDependant().toString().compareTo(ind2.getDependant().toString());
    }
    if (REFERENCED_COLUMN.equals(sortProperty)) {
      return ind1.getReferenced().toString().compareTo(ind2.getReferenced().toString());
    }

    return 0;
  }


}
