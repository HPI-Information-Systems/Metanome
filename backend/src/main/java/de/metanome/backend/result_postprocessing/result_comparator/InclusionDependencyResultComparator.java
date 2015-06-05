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
  public static final String DEPENDANT_COLUMN_RATIO = "dependantColumnRatio";
  public static final String REFERENCED_COLUMN_RATIO = "referencedColumnRatio";
  public static final String DEPENDANT_OCCURRENCE_RATIO = "dependantOccurrenceRatio";
  public static final String REFERENCED_OCCURRENCE_RATIO = "referencedOccurrenceRatio";
  public static final String DEPENDANT_UNIQUENESS_RATIO = "dependantUniquenessRatio";
  public static final String REFERENCED_UNIQUENESS_RATIO = "referencedUniquenessRatio";

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
    if (DEPENDANT_COLUMN_RATIO.equals(sortProperty)) {
      return Float.compare(ind1.getDependantColumnRatio(), ind2.getDependantColumnRatio());
    }
    if (REFERENCED_COLUMN_RATIO.equals(sortProperty)) {
      return Float.compare(ind1.getReferencedColumnRatio(), ind2.getReferencedColumnRatio());
    }
    if (DEPENDANT_OCCURRENCE_RATIO.equals(sortProperty)) {
      return Float.compare(ind1.getDependantOccurrenceRatio(), ind2.getDependantOccurrenceRatio());
    }
    if (REFERENCED_OCCURRENCE_RATIO.equals(sortProperty)) {
      return Float.compare(ind1.getReferencedOccurrenceRatio(), ind2.getReferencedOccurrenceRatio());
    }
    if (DEPENDANT_UNIQUENESS_RATIO.equals(sortProperty)) {
      return Float.compare(ind1.getDependantUniquenessRatio(), ind2.getDependantUniquenessRatio());
    }
    if (REFERENCED_UNIQUENESS_RATIO.equals(sortProperty)) {
      return Float.compare(ind1.getReferencedUniquenessRatio(), ind2.getReferencedUniquenessRatio());
    }

    return 0;
  }


}
