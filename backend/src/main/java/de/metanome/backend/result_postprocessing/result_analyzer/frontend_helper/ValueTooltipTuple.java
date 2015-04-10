package de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper;

import com.google.common.annotations.GwtCompatible;

import java.io.Serializable;

/**
 * Used to store the value and the according tooltip
 *
 * Created by Alexander Spivak on 26.02.2015.
 */
@GwtCompatible
public class ValueTooltipTuple implements Serializable{

  // Used for serialization
  private static final long serialVersionUID = 8512860152639350344L;

  //<editor-fold desc="Private attributes">

  // Value
  private String value;
  // Tooltip
  private String tooltip;

  //</editor-fold>

  //<editor-fold desc="Constructors">

  public ValueTooltipTuple(){

  }

  public ValueTooltipTuple(String value, String tooltip) {
    this.value = value;
    this.tooltip = tooltip;
  }

  //</editor-fold>

  //<editor-fold desc="Getter and Setter">

  public String getTooltip() {
    return tooltip;
  }

  public void setTooltip(String tooltip) {
    this.tooltip = tooltip;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  //</editor-fold>
}
