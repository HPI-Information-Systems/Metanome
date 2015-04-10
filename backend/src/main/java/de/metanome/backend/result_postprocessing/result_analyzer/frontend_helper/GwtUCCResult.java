package de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper;

import de.metanome.backend.result_postprocessing.result_analyzer.ucc.UCCRank;

import java.io.Serializable;

/**
 * A GWT compatible representation of UCC results which do not contain unsupported BitSets
 *
 * Created by Alexander Spivak on 04.03.2015.
 */
public class GwtUCCResult implements Serializable {

  // Used for serialization
  private static final long serialVersionUID = -6124162815266745732L;

  private UCCRank uccRank;
  private ValueTooltipTuple ucc;

  //<editor-fold desc="Getter and Setter">

  public UCCRank getUccRank() {
    return uccRank;
  }

  public void setUccRank(UCCRank uccRank) {
    this.uccRank = uccRank;
  }

  public ValueTooltipTuple getUcc() {
    return ucc;
  }

  public void setUcc(ValueTooltipTuple ucc) {
    this.ucc = ucc;
  }

  //</editor-fold>
}
