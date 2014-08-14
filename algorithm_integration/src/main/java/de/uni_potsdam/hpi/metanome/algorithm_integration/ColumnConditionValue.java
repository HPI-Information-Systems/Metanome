package de.uni_potsdam.hpi.metanome.algorithm_integration;

/**
 * This is the leaf node class for the {@link de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCondition} using the compsite pattern.
 *
 * @author Jens Hildebrandt
 */
public class ColumnConditionValue implements ColumnCondition {
  protected ColumnIdentifier columnIdentifier;
  protected String columnValue;
  protected boolean isNegated;

  /**
   * Exists for Gwt serialization
   */
  protected ColumnConditionValue() {
    this.columnIdentifier = new ColumnIdentifier();
    this.columnValue = new String();
    this.isNegated = false;
  }

  /**
   * Constructs a {@link de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnConditionValue}
   * using a {@link de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier} and a {@link
   * java.lang.String }. By default the condition is not negated.
   */
  public ColumnConditionValue(ColumnIdentifier columnIdentifier, String columnValue) {
    this.columnIdentifier = columnIdentifier;
    this.columnValue = columnValue;
    this.isNegated = false;
  }

  /**
   * Constructs a {@link de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnConditionValue} using a {@link de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier}, a {@link java.lang.String
   * }, and a boolean which indicates the negation of the condition.
   *
   * @param columnIdentifier
   * @param columnValue
   * @param isNegated
   */
  public ColumnConditionValue(ColumnIdentifier columnIdentifier, String columnValue,
                              boolean isNegated) {
    this(columnIdentifier, columnValue);
    this.isNegated = isNegated;
  }

  @Override
  public int compareTo(ColumnCondition o) {
    if (o instanceof ColumnConditionValue) {
      ColumnConditionValue other = (ColumnConditionValue) o;
      if (other.isNegated == this.isNegated) {
        int columnComparison = this.columnIdentifier.compareTo(other.columnIdentifier);
        if (columnComparison != 0) {
          return columnComparison;
        } else {
          return this.columnValue.compareTo(other.columnValue);
        }
      } else {
        if (this.isNegated) {
          return 1;
        } else {
          return -1;
        }
      }
    }
  return -1;
  }

  @Override
  public String toString() {
    if (this.isNegated) {
      return this.columnIdentifier.toString() + ": " + ColumnCondition.NOT + this.columnValue;
    } else {
      return this.columnIdentifier.toString() + ": " + this.columnValue;
    }
  }

  @Override
  public ColumnCondition add(ColumnCondition value) {
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ColumnConditionValue that = (ColumnConditionValue) o;

    if (isNegated != that.isNegated) {
      return false;
    }
    if (!columnIdentifier.equals(that.columnIdentifier)) {
      return false;
    }
    if (!columnValue.equals(that.columnValue)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = columnIdentifier.hashCode();
    result = 31 * result + columnValue.hashCode();
    result = 31 * result + (isNegated ? 1 : 0);
    return result;
  }
}
