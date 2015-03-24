package de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies;

import com.google.common.annotations.GwtCompatible;

import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.algorithm_integration.results.Result;
import de.metanome.backend.result_postprocessing.io_helper.InputAnalyzer;
import de.metanome.backend.result_postprocessing.io_helper.TableInformation;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombinationPrinter;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombinationSizeComparator;
import de.metanome.backend.results_db.Execution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Result structure which allows to represent the dependant of a FD as column combination and not a single column.
 *
 * Created by Alexander Spivak on 12.11.2014.
 */
@GwtCompatible
public class FDResult implements Comparable<FDResult>, Serializable{

  // Used for serialization
  private static final long serialVersionUID = -5828090389789789766L;

  //<editor-fold desc="Private attributes">

  // Right side of the FD
  private ColumnCombination dependant;
  // Left side of the FD
  private ColumnCombination determinant;
  // Ranking
  private FDRank rank;
  // Additional columns
  private ColumnCombination additionalColumns;

  // These attributes are used to provide tooltips and sorting in GWT because the table information
  // is lost at this time moment
  // Dependant string
  private String dependantAsString;
  // Determinant string
  private String determinantAsString;
  // Dependant string with column names
  private String dependantWithColumnNames;
  // Determinant string with column names
  private String determinantWithColumnNames;
  // Additional columns as string
  private String additionalColumnsAsString;
  // Additional columns string with column names
  private String additionalColumnsWithColumnNames;

  //</editor-fold>

  //<editor-fold desc="Constructor">

  /**
   * Creates a FD result from given determinant and dependant
   * @param determinant Left hand side of the functional dependency
   * @param dependant Right hand side of the functional dependency
   */
  public FDResult(ColumnCombination determinant, ColumnCombination dependant){
    this.dependant = dependant;
    this.determinant = determinant;
  }

  public FDResult() {
  }

  //</editor-fold>

  //<editor-fold desc="Getter and Setter">

  /**
   * Returns the dependant of the functional dependency
   * @return Returns the dependant of the functional dependency
   */
  public ColumnCombination getDependant() {
    return dependant;
  }

  /**
   * Returns the determinant of the functional dependency
   * @return Returns the determinant of the functional dependency
   */
  public ColumnCombination getDeterminant() {
    return determinant;
  }

  public void setDependant(ColumnCombination dependant) {
    this.dependant = dependant;
  }

  public void setDeterminant(ColumnCombination determinant) {
    this.determinant = determinant;
  }

  public FDRank getRank() {
    return rank;
  }

  public void setRank(FDRank rank) {
    this.rank = rank;
  }

  public ColumnCombination getAdditionalColumns() {
    return additionalColumns;
  }

  public void setAdditionalColumns(ColumnCombination additionalColumns) {
    this.additionalColumns = additionalColumns;
  }

  public String getDependantWithColumnNames() {
    return dependantWithColumnNames;
  }

  public void setDependantWithColumnNames(String dependantWithColumnNames) {
    this.dependantWithColumnNames = dependantWithColumnNames;
  }

  public String getDeterminantWithColumnNames() {
    return determinantWithColumnNames;
  }

  public void setDeterminantWithColumnNames(String determinantWithColumnNames) {
    this.determinantWithColumnNames = determinantWithColumnNames;
  }

  public String getAdditionalColumnsWithColumnNames() {
    return additionalColumnsWithColumnNames;
  }

  public void setAdditionalColumnsWithColumnNames(String additionalColumnsWithColumnNames) {
    this.additionalColumnsWithColumnNames = additionalColumnsWithColumnNames;
  }

  public String getDependantAsString() {
    return dependantAsString;
  }

  public void setDependantAsString(String dependantAsString) {
    this.dependantAsString = dependantAsString;
  }

  public String getDeterminantAsString() {
    return determinantAsString;
  }

  public void setDeterminantAsString(String determinantAsString) {
    this.determinantAsString = determinantAsString;
  }

  public String getAdditionalColumnsAsString() {
    return additionalColumnsAsString;
  }

  public void setAdditionalColumnsAsString(String additionalColumnsAsString) {
    this.additionalColumnsAsString = additionalColumnsAsString;
  }

  //</editor-fold>

  //<editor-fold desc="Copy method">

  /**
   * Creates a deep copy of the current FD result
   * @return Returns a deep copy of the current FD result
   */
  public FDResult copy(){
    ColumnCombination newDeterminant = this.getDeterminant().copy();
    ColumnCombination newDependant = this.getDependant().copy();
    return new FDResult(newDeterminant, newDependant);
  }

  //</editor-fold>

  //<editor-fold desc="Comparing method">
  /**
   * Allows sorting of FDs.
   * FD A is greater than FD B, when det(A) greater or equal det(B). In case of equality the dependants are checked.
   * @param o FD to compare
   * @return Returns 0 in case of equality, -1 if FD o is greater and 1 otherwise
   */
  @Override
  public int compareTo(FDResult o) {
    ColumnCombinationSizeComparator comparator = new ColumnCombinationSizeComparator();
    int detComparison = comparator.compare(this.getDeterminant(), o.getDeterminant());
    if(detComparison != 0)
      return detComparison;
    return comparator.compare(this.getDependant(), o.getDependant());
  }
  //</editor-fold>

  //<editor-fold desc="Print methods">

  /**
   * Creates a string representation of the functional dependency based on indices
   * @return Returns a string representation of the functional dependency based on indices
   */
  public String toString(){
    return this.toString(null);
  }

  /**
   * Creates a string representation of the functional dependency based on column names
   * @param tableInformation Table information for extraction of column names. Is allowed to be null!
   * @return Returns a string representation of the functional dependency based on column names
   */
  public String toString(TableInformation tableInformation){
    // Create pretty string representations of the left and right column combination
    String determinant = ColumnCombinationPrinter.prettyPrint(this.determinant,
                                                              ColumnCombinationPrinter.ColumnCombinationLimiter.PARENTHESES,
                                                              ",", true, tableInformation);
    String dependant = ColumnCombinationPrinter.prettyPrint(this.dependant,
                                                            ColumnCombinationPrinter.ColumnCombinationLimiter.PARENTHESES,
                                                            ",", true, tableInformation);

    return String.format("%s -> %s", determinant, dependant);

  }

  //</editor-fold>

  //<editor-fold desc="FD methods">

  /**
   * Create a new FD from merge of the current FD and the provided FD.
   * Attention: no check that the determinants are equal!
   * @param o FD to merge
   * @return Returns a new FD created from merge.
   */
  public FDResult merge(FDResult o){
    return new FDResult(this.determinant.merge(o.getDeterminant()),
                        this.dependant.merge(o.getDependant()));
  }

  /**
   * Check an FD for being a key candidate (the left side dominate all columns of the table)
   * @param tableInformation Table information which is used for the uniqueness check
   * @return Returns true if all columns of the table are part of the FD, false otherwise
   */
  public boolean isKeyCandidate(TableInformation tableInformation){
    // A key candidate should have all columns in determinant and dependant
    ColumnCombination detdep = this.determinant.merge(this.dependant);
    if (detdep.getColumnCount() != detdep.getTableColumnCount()){
      return false;
    }
    return tableInformation.isUniqueColumnCombination(this.determinant);
  }

  //</editor-fold>

  //<editor-fold desc="FD conversion">

  /**
   * Creates a list of FD results in a new format from a list of Metanome's FD results
   * @param oldResults List with Metanome FD results
   * @param tableInformation Table information
   * @return Returns a list of FD results in FDResult format
   */
  public static List<FDResult> createFromResults(List<Result> oldResults, TableInformation tableInformation){
    List<FDResult> fdResults = new ArrayList<>(oldResults.size());

    for(Result oldResult : oldResults){
      FunctionalDependency fdOld = (FunctionalDependency)oldResult;

      // Handle empty determinants (for example: FDEP creates some for constant columns)
      if(fdOld.getDeterminant().getColumnIdentifiers().isEmpty()){
        // The meaning is that every column determines the dependant
        for (int columnIndex = 0; columnIndex < tableInformation.getColumnCount(); columnIndex++){
          int dependantColumnIndex = InputAnalyzer
              .extractColumnNumber(fdOld.getDependant(), tableInformation);
          if(columnIndex == dependantColumnIndex){
            continue;
          }
          fdResults.add(new FDResult(new ColumnCombination(columnIndex, tableInformation.getColumnCount()),
                                     new ColumnCombination(dependantColumnIndex, tableInformation.getColumnCount())));
        }
        // Ignore the usual handling
        continue;
      }

      // Get the column numbers for determinant and dependant
      List<Integer> determinantIndices = InputAnalyzer.extractColumnNumbers(fdOld.getDeterminant().getColumnIdentifiers(), tableInformation);
      Integer dependantIndex = InputAnalyzer.extractColumnNumber(fdOld.getDependant(), tableInformation);

      // Create column combinations for determinant and dependant
      ColumnCombination determinantCC = new ColumnCombination(determinantIndices, tableInformation.getColumnCount());
      ColumnCombination dependantCC = new ColumnCombination(dependantIndex, tableInformation.getColumnCount());

      fdResults.add(new FDResult(determinantCC, dependantCC));
    }

    return fdResults;
  }

  //</editor-fold>

  //<editor-fold desc="Hibernate helper methods">

  /**
   * Updates the string representations and the execution environment
   *
   * @param tableInformation Table information
   */
  public void update(TableInformation tableInformation){
    this.setDependantAsString(this.getDependant().toString());
    this.setDependantWithColumnNames(ColumnCombinationPrinter.prettyPrint(this.getDependant(),
                                                                          ColumnCombinationPrinter.ColumnCombinationLimiter.PARENTHESES,
                                                                          ",", true,
                                                                          tableInformation));
    this.setDeterminantAsString(this.getDeterminant().toString());
    this.setDeterminantWithColumnNames(
        ColumnCombinationPrinter.prettyPrint(this.getDeterminant(),
                                             ColumnCombinationPrinter.ColumnCombinationLimiter.PARENTHESES,
                                             ",", true,
                                             tableInformation));
    this.setAdditionalColumnsAsString(this.getAdditionalColumns().toString());
    this.setAdditionalColumnsWithColumnNames(ColumnCombinationPrinter.prettyPrint(this.getAdditionalColumns(),
                                                                                  ColumnCombinationPrinter.ColumnCombinationLimiter.PARENTHESES,
                                                                                  ",", true,
                                                                                  tableInformation));
  }

  //</editor-fold>
}
