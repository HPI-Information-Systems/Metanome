package de.metanome.backend.result_postprocessing.result_analyzer.inclusion_dependencies;

import de.metanome.algorithm_integration.results.InclusionDependency;
import de.metanome.backend.result_postprocessing.io_helper.InputAnalyzer;
import de.metanome.backend.result_postprocessing.io_helper.TableInformation;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombinationPrinter;
import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombinationSizeComparator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

/**
 * Result structure for INDs allowing ranking and further computation methods
 *
 * Created by Daniel Roeder on 02.03.2015.
 */
public class INDResult implements Comparable<INDResult>, Serializable{

  private static final long serialVersionUID = 688887547072123677L;

  //left-hand side
  private INDTerm dependant;

  //right-hand side
  private INDTerm referenced;

  //rankings for this IND
  private INDRank rank;

  private String dependantAsString;
  private String dependantWithColumnNames;
  private String referencedAsString;
  private String referencedWithColumnNames;

  public INDResult(
      INDTerm referenced,
      INDTerm dependant) {
    this.referenced = referenced;
    this.dependant = dependant;
  }

  public INDTerm getReferenced() {
    return referenced;
  }

  public INDTerm getDependant() {
    return dependant;
  }

  public void setDependant(INDTerm dependant) {
    this.dependant = dependant;
  }

  public void setReferenced(INDTerm referenced) {
    this.referenced = referenced;
  }

  public INDRank getRank() {
    return rank;
  }

  public void setRank(INDRank rank) {
    this.rank = rank;
  }


  public INDResult(){

  }

  public static List<INDResult> createFromResults(List<InclusionDependency> oldResults,
                                                  List<TableInformation> tableInformationList,
                                                  Map<String, Integer> tableNameIndex,
                                                  Map<String, Integer> tableNameWorkaroundIndex){
    List<INDResult> results = new ArrayList<>();
    for (InclusionDependency ind : oldResults) {

      Boolean referencedWorkaround = false, dependantWorkaround = false;

      //look up the table information IDs in the map
      //TODO: export name lookup to InclusionDependency class

      String referencedTableIdentifier = ind.getReferenced().getColumnIdentifiers().get(0).getTableIdentifier();
      String dependantTableIdentifier = ind.getReferenced().getColumnIdentifiers().get(0).getTableIdentifier();


      Integer referencedTableNumber = tableNameIndex.get(referencedTableIdentifier);
      Integer dependantTableNumber = tableNameIndex.get(dependantTableIdentifier);

      //workaround
      if (referencedTableNumber == null) {
        referencedTableNumber = tableNameWorkaroundIndex.get(referencedTableIdentifier);
        referencedWorkaround = true;
      }

      if (dependantTableNumber == null){
        dependantTableNumber = tableNameWorkaroundIndex.get(dependantTableIdentifier);
        dependantWorkaround = true;
      }

      //get the table information objects by their ID
      TableInformation referencedTableInformation = tableInformationList.get(referencedTableNumber);
      TableInformation dependantTableInformation = tableInformationList.get(dependantTableNumber);

      Integer referencedTableColumnCount = referencedTableInformation.getColumnCount();
      Integer dependantTableColumnCount = dependantTableInformation.getColumnCount();

      //get column indices that have to be set in the resulting column combination
      //workaround: temporarily change the table name
      List<Integer> referencedIndices;
      List<Integer> dependantIndices;
      if (referencedWorkaround){
        String actualTableName = referencedTableInformation.getTableName();
        referencedTableInformation.setTableName(actualTableName.split("\\.")[0]);
        referencedIndices = InputAnalyzer.extractColumnNumbers(new HashSet<>(ind.getReferenced()
                                                                                               .getColumnIdentifiers()),
                                                                             referencedTableInformation);
        referencedTableInformation.setTableName(actualTableName);
      }
      else{

        referencedIndices = InputAnalyzer.extractColumnNumbers(new HashSet<>(ind.getReferenced()
                                                                                               .getColumnIdentifiers()),
                                                                             referencedTableInformation);
      }

      if(dependantWorkaround){
        String actualTableName = referencedTableInformation.getTableName();
        referencedTableInformation.setTableName(actualTableName.split("\\.")[0]);
        dependantIndices = InputAnalyzer.extractColumnNumbers(new HashSet<>(ind.getDependant()
                                                                                .getColumnIdentifiers()),
                                                              dependantTableInformation);
        referencedTableInformation.setTableName(actualTableName);
      }
      else {
        dependantIndices = InputAnalyzer.extractColumnNumbers(new HashSet<>(ind.getDependant()
                                                                                .getColumnIdentifiers()),
                                                              dependantTableInformation);
      }

      ColumnCombination referencedCC = new ColumnCombination(referencedIndices, referencedTableColumnCount);
      ColumnCombination dependantCC = new ColumnCombination(dependantIndices, dependantTableColumnCount);

      INDTerm referencedTerm = new INDTerm(referencedTableNumber, referencedCC);
      INDTerm dependantTerm = new INDTerm(dependantTableNumber, dependantCC);

      results.add(new INDResult(referencedTerm, dependantTerm));

    }
    return results;
  }

  @Override
  public int compareTo(@NotNull INDResult o) {
    ColumnCombinationSizeComparator comparator = new ColumnCombinationSizeComparator();
    int detComparison = comparator.compare(this.getReferenced().getColumns(), o.getReferenced().getColumns());
    if(detComparison != 0)
      return detComparison;
    return comparator.compare(this.getDependant().getColumns(), o.getDependant().getColumns());
  }

  public void update(List<TableInformation> tableInformationList) {
    TableInformation referencedTable = tableInformationList.get(this.getReferenced().getInputID());
    TableInformation dependantTable = tableInformationList.get(this.getDependant().getInputID());
    this.setDependantAsString(this.getDependant().getColumns().toString());
    this.setDependantWithColumnNames(
        ColumnCombinationPrinter.prettyPrint(this.getDependant().getColumns(),
                                             ColumnCombinationPrinter.ColumnCombinationLimiter.PARENTHESES,
                                             ",", true, dependantTable, true));
    this.setReferencedAsString(this.getReferenced().getColumns().toString());
    this.setReferencedWithColumnNames(
        ColumnCombinationPrinter.prettyPrint(this.getReferenced().getColumns(),
                                             ColumnCombinationPrinter.ColumnCombinationLimiter.PARENTHESES,
                                             ",", true, referencedTable, true));
  }

  public String getDependantAsString() {
    return dependantAsString;
  }

  public String getDependantWithColumnNames() {
    return dependantWithColumnNames;
  }

  public String getReferencedAsString() {
    return referencedAsString;
  }

  public String getReferencedWithColumnNames() {
    return referencedWithColumnNames;
  }

  public void setDependantAsString(String dependantAsString) {
    this.dependantAsString = dependantAsString;
  }

  public void setDependantWithColumnNames(String dependantWithColumnNames) {
    this.dependantWithColumnNames = dependantWithColumnNames;
  }

  public void setReferencedAsString(String referencedAsString) {
    this.referencedAsString = referencedAsString;
  }


  public void setReferencedWithColumnNames(String referencedWithColumnNames) {
    this.referencedWithColumnNames = referencedWithColumnNames;
  }
}
