package de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies;

import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombinationLexicalComparator;

import java.util.Comparator;

/**
 * Created by Alexander Spivak on 10.12.2014.
 *
 * Provides a "lexical" comparison on determinants of functional dependencies
 */
public class FDResultLexicalDeterminantComparator implements Comparator<FDResult> {

  // Defines the comparator for the column combinations
  private static Comparator<ColumnCombination> columnComparator = new ColumnCombinationLexicalComparator();

  /**
   * Compares two functional dependencies based on lexical ordering of the determinants
   * @param o1 Left functional dependency to compare
   * @param o2 Right functional dependency to compare
   * @return Returns 1 if the left FD is greater, 0 in case of equality, -1 otherwise
   */
  @Override
  public int compare(FDResult o1, FDResult o2) {
    return columnComparator.compare(o1.getDeterminant(), o2.getDeterminant());
  }
}
