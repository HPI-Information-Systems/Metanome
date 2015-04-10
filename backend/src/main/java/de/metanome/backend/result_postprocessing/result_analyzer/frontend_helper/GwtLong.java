package de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper;

import com.google.common.annotations.GwtCompatible;

import java.io.Serializable;

/**
 * Workaround to provide a GWT compatible long data type
 *
 * Created by Alexander Spivak on 27.02.2015.
 */
@GwtCompatible
public class GwtLong implements Serializable{

  // Used for serialization
  private static final long serialVersionUID = -310573163276996506L;

  //<editor-fold desc="Private attribute">

  // Value
  private long value;

  //</editor-fold>

  //<editor-fold desc="Constructor">

  public GwtLong(){

  }

  public GwtLong(long value){
    this.value = value;
  }
  //</editor-fold>

  //<editor-fold desc="Getter and Setter">

  public long getValue() {
    return value;
  }

  public void setValue(long value) {
    this.value = value;
  }

  //</editor-fold>

}
