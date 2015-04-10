package de.metanome.backend.result_postprocessing.result_analyzer.inclusion_dependencies;

import de.metanome.backend.result_postprocessing.result_analyzer.helper.ColumnCombination;

import java.io.Serializable;


/**
 * Created by Daniel on 02.03.2015.
 */
public class INDTerm implements Serializable{

  private static final long serialVersionUID = 1051186503473551704L;

  //ID of table the columns belong to
  private Integer inputID;

  private ColumnCombination columns;

  public INDTerm(Integer inputID,
                 ColumnCombination columns) {
    this.inputID = inputID;
    this.columns = columns;
  }

  public INDTerm(){

  }

  public ColumnCombination getColumns() {
    return columns;
  }

  public void setColumns(ColumnCombination columns) {
    this.columns = columns;
  }

  public Integer getInputID() {
    return inputID;
  }

  public void setInputID(Integer inputID) {
    this.inputID = inputID;
  }

}
