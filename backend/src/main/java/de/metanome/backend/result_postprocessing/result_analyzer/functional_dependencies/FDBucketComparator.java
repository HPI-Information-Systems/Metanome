package de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies;


import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombinationSizeComparator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Allows to sort functional dependency buckets
 *
 * Created by Alexander Spivak on 07.01.2015.
 */
public class FDBucketComparator implements Comparator<Map.Entry<ColumnCombination, List<FDResult>>> {

  /**
   * Compares two bucket entry based on wrong lexical ordering
   * @param o1 Left entry to compare
   * @param o2 Right entry to compare
   * @return Returns 1 if the left entry is greater, 0 in case of equality, -1 otherwise
   */
  @Override
  public int compare(Map.Entry<ColumnCombination, List<FDResult>> o1,
                     Map.Entry<ColumnCombination, List<FDResult>> o2) {

    if(o1.getValue().size() > o2.getValue().size()){
      return 1;
    }

    if(o1.getValue().size() < o2.getValue().size()){
      return -1;
    }

    return (new ColumnCombinationSizeComparator()).compare(o1.getKey(), o2.getKey());
  }
}
