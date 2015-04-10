package de.metanome.backend.result_postprocessing.result_analyzer.helper;

import de.metanome.backend.result_postprocessing.io_helper.TableInformation;

/**
 * Created by Alexander Spivak on 12.11.2014.
 *
 * Provides a configurable string representation of a column combination.
 */
public class ColumnCombinationPrinter {

  /**
   * Supported types of left and right limiter for the printed column combination.
   */
  public enum ColumnCombinationLimiter {
    NO_LIMITER,
    ANGLE_BRACKETS,
    BRACES,
    BRACKETS,
    PARENTHESES
  }

  /**
   * Provides a configurable string representation of a column combination
   *
   * @param columnCombination Column combination to print
   * @param combinationBorder Kind of borders to use (different kinds of brackets)
   * @param columnDelimiter   Delimiter between the columns of the combination (f.e. ",")
   * @param useWhitespaces    Should a white space follow the delimiter?
   * @param tableInformation  Table information for printing column names instead of indices - when
   *                          null, indices are printed otherwise names
   * @return Returns a string representation of the column combination
   */
  public static String prettyPrint(ColumnCombination columnCombination,
                                   ColumnCombinationLimiter combinationBorder,
                                   String columnDelimiter,
                                   boolean useWhitespaces,
                                   TableInformation tableInformation){
    return prettyPrint(columnCombination, combinationBorder, columnDelimiter, useWhitespaces, tableInformation, false);
  }

  /**
   * Provides a configurable string representation of a column combination
   *
   * @param columnCombination Column combination to print
   * @param combinationBorder Kind of borders to use (different kinds of brackets)
   * @param columnDelimiter   Delimiter between the columns of the combination (f.e. ",")
   * @param useWhitespaces    Should a white space follow the delimiter?
   * @param tableInformation  Table information for printing column names instead of indices - when
   *                          null, indices are printed otherwise names
   * @return Returns a string representation of the column combination
   */
  public static String prettyPrint(ColumnCombination columnCombination,
                                   ColumnCombinationLimiter combinationBorder,
                                   String columnDelimiter,
                                   boolean useWhitespaces,
                                   TableInformation tableInformation,
                                   boolean useTableNames) {
    String leftBorder, rightBorder;

    // Compute the right border strings
    switch (combinationBorder) {
      case NO_LIMITER:
        leftBorder = "";
        rightBorder = "";
        break;
      case ANGLE_BRACKETS:
        leftBorder = "<";
        rightBorder = ">";
        break;
      case BRACES:
        leftBorder = "{";
        rightBorder = "}";
        break;
      case BRACKETS:
        leftBorder = "[";
        rightBorder = "]";
        break;
      case PARENTHESES:
        leftBorder = "(";
        rightBorder = ")";
        break;
      default:
        leftBorder = "(";
        rightBorder = ")";
    }

    StringBuilder resultBuilder = new StringBuilder(leftBorder);

    // Iterate over all used columns
    int lastColumnIndex = columnCombination.previousSetBit(columnCombination.length());

    for (int i = columnCombination.nextSetBit(0); i >= 0 && i < lastColumnIndex; i = columnCombination.nextSetBit(i + 1)) {
      // Add the column to representation and additional signs (delimiter, whitespace)
      if (tableInformation == null) {
        resultBuilder.append(i);
      } else {
        if(useTableNames){
          resultBuilder.append(tableInformation.getTableName()).append(".");
        }
        resultBuilder.append(tableInformation.getColumn(i).getColumnName());
      }
      resultBuilder.append(columnDelimiter);
      if (useWhitespaces) {
        resultBuilder.append(" ");
      }
    }
    if(lastColumnIndex >= 0){
      if (tableInformation == null) {
        resultBuilder.append(lastColumnIndex);
      } else {
        if(useTableNames){
          resultBuilder.append(tableInformation.getTableName()).append(".");
        }
        resultBuilder.append(tableInformation.getColumn(lastColumnIndex).getColumnName());
      }
    }
    // Add the right border
    resultBuilder.append(rightBorder);

    return resultBuilder.toString();
  }
}
