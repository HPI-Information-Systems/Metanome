package de.metanome.backend.result_postprocessing.result_analyzer.inclusion_dependencies;

import de.metanome.backend.result_postprocessing.io_helper.ColumnInformation;
import de.metanome.backend.result_postprocessing.io_helper.TableInformation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 02.03.2015.
 */
public class INDRanking {

  private List<INDResult> indList;

  private List<TableInformation> tableInformationList;

  //save which column in which table appeared how often
  private List<List<Integer>> occurenceList;


  public INDRanking(
      List<INDResult> indList,
      List<TableInformation> tableInformationList) {
    this.indList = indList;
    this.tableInformationList = tableInformationList;

    this.occurenceList = new ArrayList<>(tableInformationList.size());
    Integer i = 0;
    for (TableInformation ti : tableInformationList){
      List<Integer> columnList = new ArrayList<>(ti.getColumnCount());
      for (Integer j = 0; j <= ti.getColumnCount(); j++){
        columnList.add(0);
      }
      occurenceList.add(columnList);
      i++;
    }
    //count how many times each column occurs
    for(INDResult ind : indList){
      Integer referencedTableID = ind.getReferenced().getInputID();
      Integer dependantTableID = ind.getDependant().getInputID();
      for (Integer column : ind.getReferenced().getColumns()){
        Integer oldValue = occurenceList.get(referencedTableID).get(column);
        occurenceList.get(referencedTableID).set(column, oldValue + 1);
      }
      for (Integer column : ind.getDependant().getColumns()){
        Integer oldValue = occurenceList.get(dependantTableID).get(column);
        occurenceList.get(dependantTableID).set(column, oldValue + 1);
      }
    }
  }

  public void calculateDataIndependentRankings() {
    for (INDResult ind : indList){
      calculateDataIndependentRanks(ind);
    }
  }

  public void calculateDatDependentRankings() {
    for (INDResult ind : indList){
      calculateDataDependentRanks(ind);
    }
  }

  private void calculateDataIndependentRanks(INDResult ind) {
    if (ind.getRank() == null){
      ind.setRank(new INDRank());
    }

    Integer referencedSize = ind.getReferenced().getColumns().getColumnCount();
    Integer dependantSize = ind.getDependant().getColumns().getColumnCount();

    calculateSizeRatios(ind, referencedSize, dependantSize);
    calculateColumnOccurrences(ind);
  }

  private void calculateDataDependentRanks(INDResult ind) {
    if (ind.getRank() == null){
      ind.setRank(new INDRank());
    }

    Integer referencedSize = ind.getReferenced().getColumns().getColumnCount();
    Integer dependantSize = ind.getDependant().getColumns().getColumnCount();

    calculateConstancyRatios(ind, referencedSize, dependantSize);
  }

  private void calculateConstancyRatios(INDResult ind, Integer referencedSize,
                                        Integer dependantSize) {
    TableInformation referencedTable = tableInformationList.get(ind.getReferenced().getInputID());
    TableInformation dependantTable = tableInformationList.get(ind.getDependant().getInputID());

    // Get column information to check the uniqueness ratio
    List<ColumnInformation> referencedColumnInformationList = referencedTable.getColumnInformationList();
    List<ColumnInformation> dependantColumnInformationList = dependantTable.getColumnInformationList();

    // Prepare constancy column counters
    Integer referencedNearlyConstantColumns = 0;
    Integer dependantNearlyConstantColumns = 0;

    // Define a uniqueness threshold
    Float uniquenessThreshold = 0.1f;

    // Find the constancy column count for the determinant
    for (Integer index : ind.getReferenced().getColumns().getColumnIndices()){
      if (referencedColumnInformationList.get(index).getUniquenessRate() < uniquenessThreshold)
        referencedNearlyConstantColumns++;
    }

    // Find the constancy column count for the dependant
    for (Integer index : ind.getDependant().getColumns().getColumnIndices()){
      if (dependantColumnInformationList.get(index).getUniquenessRate() < uniquenessThreshold)
        dependantNearlyConstantColumns++;
    }

    // Calculate and store the constancy ratios
    ind.getRank().setReferencedConstancyRatio(
        (float) referencedNearlyConstantColumns / referencedSize);
    ind.getRank().setDependantConstancyRatio(
        (float) dependantNearlyConstantColumns / dependantSize);
  }

  private void calculateColumnOccurrences(INDResult ind) {
    INDTerm referenced = ind.getReferenced();
    INDTerm dependant = ind.getDependant();
    Integer referencedOccurrences = 0;
    Integer dependantOccurrences = 0;
    for (Integer column : referenced.getColumns()){
      referencedOccurrences += occurenceList.get(referenced.getInputID()).get(column);
    }
    for (Integer column : dependant.getColumns()){
      dependantOccurrences += occurenceList.get(dependant.getInputID()).get(column);
    }

    //calculate average
    ind.getRank().setReferencedColumnOccurrence(Float.valueOf(referenced.getColumns().getColumnCount()) / referencedOccurrences);

    ind.getRank().setDependantColumnOccurrence(Float.valueOf(dependant.getColumns().getColumnCount()) / dependantOccurrences);

  }


  private void calculateSizeRatios(INDResult ind, Integer referencedSize, Integer dependantSize) {
    Integer referencedTableSize = tableInformationList.get(ind.getReferenced().getInputID()).getColumnCount();
    Integer dependantTableSize = tableInformationList.get(ind.getDependant().getInputID()).getColumnCount();
    ind.getRank().setReferencedSizeRatio(Float.valueOf(referencedSize) / referencedTableSize);
    ind.getRank().setDependantSizeRatio(Float.valueOf(dependantSize) / dependantTableSize);

  }
}
