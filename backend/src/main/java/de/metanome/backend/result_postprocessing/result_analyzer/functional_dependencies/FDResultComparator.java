package de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies;

import de.metanome.backend.result_postprocessing.result_analyzer.ResultComparator;

/**
 * Defines a FD comparator based on a predefined sort property and sort direction order
 *
 * Created by Alexander Spivak on 05.03.2015.
 */
public class FDResultComparator extends ResultComparator<FDResult> {

  /**
   * Creates a FD result comparator for given property and direction
   *
   * @param sortProperty Sort property
   * @param isAscending Sort direction
   */
  public FDResultComparator(String sortProperty, boolean isAscending) {
    super(sortProperty, isAscending);
  }

  /**
   * Compares two given FD results depending on given sort property
   *
   * @param o1 Left FD result
   * @param o2 Right FD result
   * @param sortProperty Sort property
   * @return Returns 1 if o1 is greater than o2, 0 if both are equal, -1 otherwise
   */
  @Override
  protected int compare(FDResult o1, FDResult o2, String sortProperty) {
    if("determinantAsString".equals(sortProperty)){
      return o1.getDeterminantAsString().compareTo(o2.getDeterminantAsString());
    }
    if("dependantAsString".equals(sortProperty)){
      return o1.getDependantAsString().compareTo(o2.getDependantAsString());
    }
    if("additionalColumnsAsString".equals(sortProperty)){
      return o1.getAdditionalColumnsAsString().compareTo(o2.getAdditionalColumnsAsString());
    }
    if("rank.determinantSizeRatio".equals(sortProperty)){
      return Float.compare(o1.getRank().getDeterminantSizeRatio(),
                           o2.getRank().getDeterminantSizeRatio());
    }
    if("rank.dependantSizeRatio".equals(sortProperty)){
      return Float.compare(o1.getRank().getDependantSizeRatio(),
                           o2.getRank().getDependantSizeRatio());
    }
    if("rank.determinantConstancyRatio".equals(sortProperty)){
      return Float.compare(o1.getRank().getDeterminantConstancyRatio(),
                           o2.getRank().getDeterminantConstancyRatio());
    }
    if("rank.dependantConstancyRatio".equals(sortProperty)){
      return Float.compare(o1.getRank().getDependantConstancyRatio(),
                           o2.getRank().getDependantConstancyRatio());
    }
    if("rank.coverage".equals(sortProperty)){
      return Float.compare(o1.getRank().getCoverage(),
                           o2.getRank().getCoverage());
    }
    if("rank.pollution".equals(sortProperty)){
      return Float.compare(o1.getRank().getPollution(),
                           o2.getRank().getPollution());
    }
    if("rank.minPollutionColumn".equals(sortProperty)){
      return o1.getRank().getMinPollutionColumn().compareTo(o2.getRank().getMinPollutionColumn());
    }
    if("rank.informationGainBytes".equals(sortProperty)){
      return Float.compare(o1.getRank().getInformationGainBytes(),
                           o2.getRank().getInformationGainBytes());
    }
    if("rank.informationGainCells".equals(sortProperty)){
      return Float.compare(o1.getRank().getInformationGainCells(),
                           o2.getRank().getInformationGainCells());
    }

    return 0;
  }
}
