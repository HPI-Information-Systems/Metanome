/**
 * Copyright 2014-2016 by Metanome Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.metanome.algorithm_integration.results;

import com.fasterxml.jackson.annotation.JsonTypeName;
import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnCondition;
import de.metanome.algorithm_integration.ColumnConditionValue;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;

import javax.xml.bind.annotation.XmlTransient;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * Represents a conditional unique column combination
 *
 * @author Jens Ehrlich
 */
@JsonTypeName("ConditionalUniqueColumnCombination")
public class ConditionalUniqueColumnCombination implements Result {

  public static final String LINESEPARATOR = "\n";
  public static final String CUCC_SEPARATOR = " | ";

  private static final long serialVersionUID = -7793914512848983094L;

  protected ColumnCombination columnCombination;
  protected ColumnCondition condition;

  /**
   * Exists for serialization.
   */
  protected ConditionalUniqueColumnCombination() {
    this.columnCombination = new ColumnCombination();
    this.condition = new ColumnConditionValue(new ColumnIdentifier("", ""), "");
  }

  /**
   * Constructs a {@link de.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination}
   * from a {@link ColumnCombination} and an array of {@link de.metanome.algorithm_integration.ColumnConditionAnd}s.
   *
   * @param columnCombination a supposedly unique column combination
   * @param columnCondition   array of conditions for the CUCC
   */
  public ConditionalUniqueColumnCombination(ColumnCombination columnCombination,
                                            ColumnCondition columnCondition) {
    this.columnCombination = columnCombination;
    this.condition = columnCondition;

  }

  /**
   * @return the column combination
   */
  public ColumnCombination getColumnCombination() {
    return this.columnCombination;
  }

  public void setColumnCombination(ColumnCombination columnCombination) {
    this.columnCombination = columnCombination;
  }

  /**
   * @return the condition list
   */
  public ColumnCondition getCondition() {
    return this.condition;
  }

  public void setCondition(ColumnCondition condition) {
    this.condition = condition;
  }

  @XmlTransient
  @Override
  public void sendResultTo(OmniscientResultReceiver resultReceiver)
    throws CouldNotReceiveResultException, ColumnNameMismatchException {
    resultReceiver.receiveResult(this);
  }

  public String buildPatternTableauTable() {
    StringBuilder builder = new StringBuilder();
    TreeSet<ColumnIdentifier> patternTableauHead = condition.getContainedColumns();
    builder.append(patternTableauHead);

    List<Map<ColumnIdentifier, String>> conditions = condition.getPatternConditions();
    for (Map<ColumnIdentifier, String> condition : conditions) {
      builder.append(LINESEPARATOR);
      for (ColumnIdentifier column : patternTableauHead) {
        if (condition.containsKey(column)) {
          String value = condition.get(column);
          if (value.length() < column.toString().length()) {
            StringBuilder whitespaceBuilder = new StringBuilder();
            for (int i = 0; i < (column.toString().length() - value.length()) / 2; i++) {
              whitespaceBuilder.append(" ");
            }
            builder.append(whitespaceBuilder);
            builder.append(value);
            builder.append(whitespaceBuilder);
          } else {
            builder.append(value);
            builder.append(" ");
          }
        } else {
          StringBuilder whitespaceBuilder = new StringBuilder();
          for (int i = 0; i < column.toString().length() / 2; i++) {
            whitespaceBuilder.append(" ");
          }
          builder.append(whitespaceBuilder);
          builder.append("-");
          builder.append(whitespaceBuilder);
        }
      }
    }
    return builder.toString();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(columnCombination.toString());
    builder.append(CUCC_SEPARATOR);
    builder.append(condition.toString());
    builder.append(" Coverage: ");
    builder.append(this.condition.getCoverage());
    return builder.toString();
  }

  public String buildPatternTableau() {
    StringBuilder builder = new StringBuilder();
    builder.append(columnCombination.toString());
    builder.append(LINESEPARATOR);

    builder.append(this.buildPatternTableauTable());

    builder.append(LINESEPARATOR);
    builder.append("Coverage: ");
    builder.append(this.condition.getCoverage());
    builder.append(LINESEPARATOR);
    return builder.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ConditionalUniqueColumnCombination that = (ConditionalUniqueColumnCombination) o;

    if (columnCombination != null ? !columnCombination.equals(that.columnCombination)
      : that.columnCombination != null) {
      return false;
    }
    if (condition != null ? !condition.equals(that.condition) : that.condition != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result = columnCombination != null ? columnCombination.hashCode() : 0;
    result = 31 * result + (condition != null ? condition.hashCode() : 0);
    return result;
  }

}
