package de.metanome.backend.result_postprocessing.io_helper;

import com.github.slugify.Slugify;

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.RelationalInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.PLIBuilder;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.PLIHolder;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.PLIList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides metadata and statistics about a table including all columns
 *
 * Created by Alexander Spivak on 15.11.2014.
 */
public class TableInformation {

  //<editor-fold desc="Private attributes">

  // Number of columns
  private int columnCount;

  // Table name
  private String tableName;

  // Uses row data
  private boolean isUsingRowData;

  // List of column information
  private List<ColumnInformation> columnInformationList;


  // Relational input handle
  private RelationalInputGenerator relationalInputGenerator;
  // PLI Holder
  private PLIHolder pliHolder;

  //</editor-fold>

  //<editor-fold desc="Constructor">

  /**
   * Computes table metadata on the input data
   * @param relationalInputGenerator The input data generator providing access to the input data stream
   * @param useRowData Is it allowed to access row data or only table header data?
   * @throws InputGenerationException Will be thrown if the input data is not accessible
   */
  public TableInformation(RelationalInputGenerator relationalInputGenerator, boolean useRowData)
      throws InputGenerationException {

    this.relationalInputGenerator = relationalInputGenerator;
    this.isUsingRowData = useRowData;

    this.pliHolder = new PLIHolder();

    // Get table data
    RelationalInput relationalInput = relationalInputGenerator.generateNewCopy();
    this.columnCount = relationalInput.numberOfColumns();
    this.tableName = relationalInput.relationName();

    // Create the column information
    this.columnInformationList = new ArrayList<>(this.columnCount);
    for(int columnIndex=0; columnIndex<this.columnCount; columnIndex++){
      // Generate a new data iterator for each column check
      relationalInput = relationalInputGenerator.generateNewCopy();
      // Compute the column information for the current column
      this.columnInformationList.add(new ColumnInformation(relationalInput.columnNames().get(columnIndex),
                                                           columnIndex,
                                                           relationalInput,
                                                           this.isUsingRowData));

    }
  }
  //<editor-fold desc="Getter">

  public int getColumnCount() {
    return columnCount;
  }

  public String getTableName() {
    return tableName;
  }

  public String getSlugifiedTableName() throws IOException {
    return (new Slugify()).slugify(tableName);
  }

  public boolean isUsingRowData() {
    return isUsingRowData;
  }

  public void setUsingRowData(boolean isUsingRowData) {
    this.isUsingRowData = isUsingRowData;
  }

  public long getRowCount() { return columnInformationList.get(0).getRowCount(); }

  public ColumnInformation getColumn(int column){
    return columnInformationList.get(column);
  }

  public List<ColumnInformation> getColumnInformationList() {
    return columnInformationList;
  }


  //</editor-fold>

  //TODO: this is only used for the SPIDER and BINDER table identifier workaround, remove once fixed
  public void setTableName(String tableName) {
    this.tableName = tableName;
  }
  /**
   * Produces a list of column identifiers for all columns in the combination
   * @param columnCombination Columns for which the identifiers should be produced
   * @return Returns a list of column identifiers for all columns in the combination
   */
  public List<ColumnIdentifier> getColumnIdentifiers(ColumnCombination columnCombination){
    List<ColumnIdentifier> result = new ArrayList<>();

    for(Integer i : columnCombination){
      result.add(new ColumnIdentifier(this.tableName,
                                      this.columnInformationList.get(i).getColumnName()));
    }

    return result;
  }

  public RelationalInputGenerator getRelationalInputGenerator() {return relationalInputGenerator;}

  public PLIHolder getPliHolder() {
    return pliHolder;
  }

  //</editor-fold>

  //<editor-fold desc="Uniqueness and Information Content methods">

  /**
   * Checks for a column combination if all values inside are unique (NULL = NULL)
   * TODO - add support for NULL != NULL
   * @param columnCombination A column combination the row values should be checked for uniqueness
   * @return Returns true if all row values for the current column combination are unique, false otherwise
   */
  public boolean isUniqueColumnCombination(ColumnCombination columnCombination){
    return (getUniqueValuesCount(columnCombination) == getRowCount());
  }

  /**
   * Returns the count of unique values for the given column combination
   * @param columnCombination The column combination the count of unique values should be calculated for
   * @return Returns the count of unique values for the given column combination
   */
  public long getUniqueValuesCount(ColumnCombination columnCombination){

    // Build the PLIs if necessary
    buildPLIs(columnCombination);

    // Compute the intersected PLI of given columns
    PLIList columnsPLI = pliHolder.intersectLists(columnCombination);

    // The number of unique values is the number of rows after removing the duplicates
    return  this.getRowCount() - columnsPLI.computeKeyError();
  }

  /**
   * Computes the information content as sum of columns information contents
   * @return Returns the information content of the table in bytes
   */
  public long getInformationContent(){
    long sum = 0l;
    for(ColumnInformation columnInformation : columnInformationList) {
      sum += columnInformation.getInformationContent(getRowCount());
    }
    return sum;
  }

  //</editor-fold>

  //<editor-fold desc="PLI methods">

  /**
   * Builds PLIs for needed columns and adds them to the PLI holder
   * @param neededColumns Columns for which the PLIs should be created
   */
  public void buildPLIs(ColumnCombination neededColumns){

    // Detect columns for which the PLI holder has no entry
    ColumnCombination missedColumns = new ColumnCombination(this.getColumnCount());

    List<ColumnCombination> neededColumnCombinations = neededColumns.getColumnsAsCombinations();
    for(ColumnCombination neededColumn : neededColumnCombinations){
      if(!pliHolder.getPliMap().containsKey(neededColumn)){
        missedColumns.or(neededColumn);
      }
    }

    // Create the PLIs for the missing columns
    try {
      PLIHolder additionalPLIs = PLIBuilder
          .buildPLIs(relationalInputGenerator.generateNewCopy(), missedColumns);
      // Add them to current PLIs
      pliHolder.addAll(additionalPLIs);
    }
    catch (InputGenerationException e){
      e.printStackTrace();
    }
  }

  //</editor-fold>
}
