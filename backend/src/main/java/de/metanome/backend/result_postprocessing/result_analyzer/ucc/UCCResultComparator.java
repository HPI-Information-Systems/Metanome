package de.metanome.backend.result_postprocessing.result_analyzer.ucc;

import de.metanome.backend.result_postprocessing.result_analyzer.ResultComparator;

/**
 * Created by Alexander Spivak on 11.03.2015.
 */
public class UCCResultComparator extends ResultComparator<UCCResult>{

  /**
   * Creates a result comparator for given property and direction
   *
   * @param sortProperty Sort property
   * @param isAscending  Sort direction
   */
  public UCCResultComparator(String sortProperty, boolean isAscending) {
    super(sortProperty, isAscending);
  }

  /**
   * Compares two given UCC results depending on given sort property
   *
   * @param o1 Left UCC result
   * @param o2 Right UCC result
   * @param sortProperty Sort property
   * @return Returns 1 if o1 is greater than o2, 0 if both are equal, -1 otherwise
   */
  @Override
  protected int compare(UCCResult o1, UCCResult o2, String sortProperty) {

    if("UCCAsString".equals(sortProperty)){
      return o1.getUccAsString().compareTo(o2.getUccAsString());
    }

    if("rank.length".equals(sortProperty)){
      return Integer.compare(o1.getRank().getLength(),
                             o2.getRank().getLength());
    }

    if("rank.max".equals(sortProperty)){
      return Double.compare(o1.getRank().getMax(),
                            o2.getRank().getMax());
    }

    if("rank.min".equals(sortProperty)){
      return Double.compare(o1.getRank().getMin(),
                            o2.getRank().getMin());
    }

    if("rank.averageOccurrence".equals(sortProperty)){
      return Double.compare(o1.getRank().getAverageOccurrence(),
                            o2.getRank().getAverageOccurrence());
    }

    if("rank.randomness".equals(sortProperty)){
      return Double.compare(o1.getRank().getRandomness(),
                            o2.getRank().getRandomness());
    }

    return 0;
  }
}
