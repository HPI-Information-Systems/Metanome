package de.uni_potsdam.hpi.metanome.algorithm_integration;

import java.io.Serializable;

/**
 * @author Jens Hildebrandt
 */
public interface ColumnCondition extends Serializable, Comparable<ColumnCondition> {
  public static final String OPEN_BRACKET = "[";
  public static final String CLOSE_BRACKET = "]";
  public static final String AND = "&";
  public static final String OR = "|";
  public static final String NOT = "\u00AC";

  public String toString();
  public void add(ColumnCondition value);
}
