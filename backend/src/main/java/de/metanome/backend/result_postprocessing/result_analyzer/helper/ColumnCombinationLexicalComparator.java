package de.metanome.backend.result_postprocessing.result_analyzer.helper;

import java.util.Comparator;

/**
 * Created by Alexander Spivak on 10.12.2014.
 *
 * Provides a "lexical" comparison on column combinations
 */
public class ColumnCombinationLexicalComparator
    implements Comparator<ColumnCombination> {

  /**
   * Compares two column combinations based on lexical ordering
   * @param o1 Left column combination to compare
   * @param o2 Right column combination to compare
   * @return Returns 1 if the left CC is greater, 0 in case of equality, -1 otherwise
   */
  @Override
  public int compare(ColumnCombination o1, ColumnCombination o2) {
    // Compute the column indices which differ in both combinations
    ColumnCombination xorResult = o1.copy();
    xorResult.xor(o2);

    // Get the first differing column
    int firstUnequalBit = xorResult.nextSetBit(0);
    // If both combinations contains the same columns, they are equal
    if(firstUnequalBit == -1)
      return 0;

    // If the first differing column is not in o1 then return that o1 is smaller
    int nextUnequalBitO1 = o1.nextSetBit(firstUnequalBit);
    if(nextUnequalBitO1 == -1)
      return -1;

    // If the first differing column is not in o2 then return that o1 is bigger
    int nextUnequalBitO2 = o2.nextSetBit(firstUnequalBit);
    if(nextUnequalBitO2 == -1){
      return 1;
    }

    // Sort depending on the first unequal column
    return (nextUnequalBitO1 > nextUnequalBitO2) ? 1 : -1;
  }
}
