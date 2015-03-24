package de.metanome.backend.result_postprocessing.io_helper;

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Provides basic functionality for analyzing the input data, for example for metadata extraction
 *
 * Created by Alexander Spivak on 13.11.2014.
 */
public class InputAnalyzer {

  // Contains the table information
  private TableInformation tableInformation;

  //<editor-fold desc="Constructor">
  /**
   * Computes the table information from the input generator
   * @param relationalInputGenerator Input generator providing input data for the analysis
   * @param useRowData Is it allowed to access row data or only table header data?
   */
  public InputAnalyzer(RelationalInputGenerator relationalInputGenerator, boolean useRowData){
    try {
      tableInformation = new TableInformation(relationalInputGenerator, useRowData);
    } catch (InputGenerationException e) {
      e.printStackTrace();
    }
  }
  //</editor-fold>

  //<editor-fold desc="Getter">

  public TableInformation getTableInformation() {
    return tableInformation;
  }

  //</editor-fold>


  //<editor-fold desc="Static column index determine methods">

  /**
   * Extracts the column number from the identifier
   * @param columnIdentifier Provides table and column name.
   * @param tableInformation Provides data about the table
   * @return Returns the column number starting with 1 as integer
   */
  public static Integer extractColumnNumber(ColumnIdentifier columnIdentifier, TableInformation tableInformation){
    for (ColumnInformation columnInformation : tableInformation.getColumnInformationList()) {
      ColumnIdentifier checkIdentifier = new ColumnIdentifier(tableInformation.getTableName(), columnInformation.getColumnName());
      if (checkIdentifier.compareTo(columnIdentifier) == 0) {
        return columnInformation.getColumnIndex();
      }
    }
    return -1;
  }

  /**
   * Extracts the column numbers for given set of identifiers
   * @param columnIdentifiers Provides a set column identifiers
   * @param tableInformation Provides data about the table
   * @return Returns a list of column numbers of giving identifiers
   */
  public static List<Integer> extractColumnNumbers(Set<ColumnIdentifier> columnIdentifiers, TableInformation tableInformation){
    List<Integer> columnNumbers = new ArrayList<>(columnIdentifiers.size());

    for(ColumnIdentifier columnIdentifier : columnIdentifiers){
      columnNumbers.add(InputAnalyzer.extractColumnNumber(columnIdentifier, tableInformation));
    }

    return columnNumbers;
  }

  //</editor-fold>
}
