package de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper;

import de.metanome.backend.result_postprocessing.result_analyzer.inclusion_dependencies.INDRank;

import java.io.Serializable;

/**
 * A GWT compatible representation of IND results which do not contain unsupported BitSets
 *
 * Created by Alexander Spivak on 04.03.2015.
 */
public class GwtINDResult implements Serializable {

  // Used for serialization
  private static final long serialVersionUID = -420123802633268420L;

  //<editor-fold desc="Private Attributes">

  // IND rank of corresponding IndResult
  private INDRank indRank;
  // Dependant string representation of corresponding INDResult
  private ValueTooltipTuple dependant;
  // Referenced string representation of corresponding INDResult
  private ValueTooltipTuple referenced;

  //</editor-fold>

  //<editor-fold desc="Constructor">

  public GwtINDResult(){
  }

  //</editor-fold>

  //<editor-fold desc="Getter and Setter">

  public INDRank getIndRank() {
    return indRank;
  }

  public void setIndRank(INDRank indRank) {
    this.indRank = indRank;
  }

  public ValueTooltipTuple getDependant() {
    return dependant;
  }

  public void setDependant(ValueTooltipTuple dependant) {
    this.dependant = dependant;
  }

  public ValueTooltipTuple getReferenced() {
    return referenced;
  }

  public void setReferenced(ValueTooltipTuple referenced) {
    this.referenced = referenced;
  }

  //</editor-fold>

}
