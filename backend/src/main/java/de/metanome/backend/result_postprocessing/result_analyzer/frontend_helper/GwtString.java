package de.metanome.backend.result_postprocessing.result_analyzer.frontend_helper;

import com.google.common.annotations.GwtCompatible;

import java.io.Serializable;

/**
 * Created by Daniel on 05.03.2015.
 */
//workaround to properly send Strings in GWT
@GwtCompatible
public class GwtString implements Serializable{

  private static final long serialVersionUID = 9155857024552538569L;

  private String value;

  public GwtString() {
  }

  public GwtString(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
