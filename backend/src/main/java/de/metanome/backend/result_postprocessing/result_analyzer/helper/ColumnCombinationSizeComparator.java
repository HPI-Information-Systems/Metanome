package de.metanome.backend.result_postprocessing.result_analyzer.helper;

import java.util.Comparator;

/**
 * Comparator for column combinations based on their column counts
 *
 * Created by Alexander Spivak on 12.11.2014.
 */
public class ColumnCombinationSizeComparator
    implements Comparator<ColumnCombination> {

  /**
   * Compares two column combinations based on their column counts
   * @param o1 Left column combination to compare
   * @param o2 Right column combination to compare
   * @return Returns 1 if the left CC is greater, 0 in case of equality, -1 otherwise
   */
  @Override
  public int compare(ColumnCombination o1, ColumnCombination o2) {
    if(o1.getColumnCount() < o2.getColumnCount()){
      return -1;
    }

    if(o1.getColumnCount() > o2.getColumnCount()){
      return 1;
    }

    ColumnCombination intermediate = o1.copy();
    intermediate.xor(o2);
    int firstUnequalBit = intermediate.nextSetBit(0);
    if (firstUnequalBit == -1) {
      return 0;
    }

    return (o1.get(firstUnequalBit) ? -1 : 1);
  }
}
