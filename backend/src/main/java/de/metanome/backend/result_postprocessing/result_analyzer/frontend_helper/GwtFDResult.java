package de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper;

import com.google.common.annotations.GwtCompatible;

import de.metanome.backend.result_postprocessing.result_analyzer.functional_dependencies.FDRank;

import java.io.Serializable;

/**
 * A GWT compatible representation of FD results which do not contain unsupported BitSets
 *
 * Created by Alexander Spivak on 26.02.2015.
 */
@GwtCompatible
public class GwtFDResult implements Serializable{

  // Used for serialization
  private static final long serialVersionUID = -6340452419530523845L;

  //<editor-fold desc="Private Attributes">

  // FD rank of corresponding FDResult
  private FDRank fdRank;
  // Determinant string representation of corresponding FDResult
  private ValueTooltipTuple determinant;
  // Dependant string representation of corresponding FDResult
  private ValueTooltipTuple dependant;
  // Additional columns string representation of corresponding FDResult
  private ValueTooltipTuple additionalColumns;

  //</editor-fold>

  //<editor-fold desc="Constructor">

  public GwtFDResult(){
  }

  //</editor-fold>

  //<editor-fold desc="Getter and Setter">

  public FDRank getFdRank() {
    return fdRank;
  }

  public void setFdRank(FDRank fdRank) {
    this.fdRank = fdRank;
  }

  public ValueTooltipTuple getDeterminant() {
    return determinant;
  }

  public void setDeterminant(ValueTooltipTuple determinant) {
    this.determinant = determinant;
  }

  public ValueTooltipTuple getDependant() {
    return dependant;
  }

  public void setDependant(ValueTooltipTuple dependant) {
    this.dependant = dependant;
  }

  public ValueTooltipTuple getAdditionalColumns() {
    return additionalColumns;
  }

  public void setAdditionalColumns(ValueTooltipTuple additionalColumns) {
    this.additionalColumns = additionalColumns;
  }

  //</editor-fold>
}

