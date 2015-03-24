package de.metanome.backend.result_postprocessing.result_analyzer.inclusion_dependencies;

import de.metanome.backend.result_postprocessing.result_analyzer.ResultComparator;

/**
 * Defines an IND comparator based on a predefined sort property and sort direction order
 *
 * Created by Alexander Spivak on 05.03.2015.
 */
public class INDResultComparator extends ResultComparator<INDResult> {

  /**
   * Creates an IND result comparator for given property and direction
   *
   * @param sortProperty Sort property
   * @param isAscending Sort direction
   */
  public INDResultComparator(String sortProperty, boolean isAscending) {
    super(sortProperty, isAscending);
  }

  /**
   * Compares two given IND results depending on given sort property
   *
   * @param o1 Left IND result
   * @param o2 Right IND result
   * @param sortProperty Sort property
   * @return Returns 1 if o1 is greater than o2, 0 if both are equal, -1 otherwise
   */
  @Override
  protected int compare(INDResult o1, INDResult o2, String sortProperty) {

    if("dependantAsString".equals(sortProperty)){
      return o1.getDependantAsString().compareTo(o2.getDependantAsString());
    }
    if("referencedAsString".equals(sortProperty)){
      return o1.getReferencedAsString().compareTo(o2.getReferencedAsString());
    }
    if("rank.dependantSizeRatio".equals(sortProperty)){
      return Float.compare(o1.getRank().getDependantSizeRatio(),
                           o2.getRank().getDependantSizeRatio());
    }
    if("rank.referencedSizeRatio".equals(sortProperty)){
      return Float.compare(o1.getRank().getReferencedSizeRatio(), o2.getRank().getReferencedSizeRatio());
    }
    if("rank.dependantColumnOccurrence".equals(sortProperty)){
      return Float.compare(o1.getRank().getDependantColumnOccurrence(), o2.getRank().getDependantColumnOccurrence());
    }
    if("rank.referencedColumnOccurrence".equals(sortProperty)){
      return Float.compare(o1.getRank().getReferencedColumnOccurrence(), o2.getRank().getReferencedColumnOccurrence());
    }

    return 0;
  }


}
