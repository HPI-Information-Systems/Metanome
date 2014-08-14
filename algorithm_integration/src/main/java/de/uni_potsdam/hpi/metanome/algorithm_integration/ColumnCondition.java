package de.uni_potsdam.hpi.metanome.algorithm_integration;

import java.io.Serializable;

/**
 * Abstract super class for the composite pattern used for column conditions.
 *
 * @author Jens Hildebrandt
 */
public interface ColumnCondition extends Serializable, Comparable<ColumnCondition> {
  public static final String OPEN_BRACKET = "[";
  public static final String CLOSE_BRACKET = "]";
  public static final String AND = "&";
  public static final String OR = "|";
  public static final String NOT = "\u00AC";

  public String toString();

  public ColumnCondition add(ColumnCondition value);
}
