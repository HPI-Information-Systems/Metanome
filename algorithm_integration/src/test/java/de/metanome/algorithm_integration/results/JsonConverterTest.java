/**
 * Copyright 2015-2016 by Metanome Project
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

import de.metanome.algorithm_integration.*;
import de.metanome.algorithm_integration.results.basic_statistic_values.BasicStatisticValueString;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class JsonConverterTest {

  /**
   * A {@link BasicStatistic} should be converted to a JSON String and creatable from a JSON
   * string.
   */
  @Test
  public void testToAndFromJsonStringBasicStatistic() throws IOException {
    // Setup
    String expectedStatisticName = "Min";
    BasicStatisticValueString expectedStatisticValue = new BasicStatisticValueString("minValue");
    ColumnIdentifier expectedColumn = new ColumnIdentifier("table42", "column23");
    BasicStatistic
      expectedStatistic =
      new BasicStatistic(expectedColumn);
    expectedStatistic.addStatistic(expectedStatisticName, expectedStatisticValue);

    JsonConverter<BasicStatistic> jsonConverter = new JsonConverter<>();

    // Expected values
    String
      expectedJson = "{\"type\":\"BasicStatistic\",\"columnCombination\":{\"columnIdentifiers\":[{\"tableIdentifier\":\"table42\",\"columnIdentifier\":\"column23\"}]},\"statisticMap\":{\"Min\":{\"type\":\"BasicStatisticValueString\",\"value\":\"minValue\"}}}";
    // Execute functionality
    String actualJson = jsonConverter.toJsonString(expectedStatistic);

    // Check result
    assertEquals(expectedJson, actualJson);

    // Execute functionality
    BasicStatistic actualStatistic = jsonConverter.fromJsonString(actualJson, BasicStatistic.class);

    // Check result
    assertEquals(expectedStatistic, actualStatistic);
  }


  /**
   * A {@link InclusionDependency} should be converted to a JSON String and creatable from a JSON
   * string.
   */
  @Test
  public void testToAndFromJsonStringInclusionDependency() throws IOException {
    // Setup
    ColumnPermutation expectedDependant = new ColumnPermutation(
      new ColumnIdentifier("table2", "column2"),
      new ColumnIdentifier("table2", "column27"));
    ColumnPermutation expectedReferenced = new ColumnPermutation(
      new ColumnIdentifier("table1", "column1"),
      new ColumnIdentifier("table1", "column4"));
    InclusionDependency
      expectedInd =
      new InclusionDependency(expectedDependant, expectedReferenced);

    JsonConverter<InclusionDependency> jsonConverter = new JsonConverter<>();

    // Expected values
    String
      expectedJson =
      "{\"type\":\"InclusionDependency\",\"dependant\":{\"columnIdentifiers\":[{\"tableIdentifier\":\"table2\",\"columnIdentifier\":\"column2\"},{\"tableIdentifier\":\"table2\",\"columnIdentifier\":\"column27\"}]},\"referenced\":{\"columnIdentifiers\":[{\"tableIdentifier\":\"table1\",\"columnIdentifier\":\"column1\"},{\"tableIdentifier\":\"table1\",\"columnIdentifier\":\"column4\"}]}}";

    // Execute functionality
    String actualJson = jsonConverter.toJsonString(expectedInd);

    // Check result
    assertEquals(expectedJson, actualJson);

    // Execute functionality
    InclusionDependency
      actualInd =
      jsonConverter.fromJsonString(actualJson, InclusionDependency.class);

    // Check result
    assertEquals(expectedInd, actualInd);
  }

  /**
   * A {@link OrderDependency} should be converted to a JSON String and creatable from a JSON
   * string.
   */
  @Test
  public void testToAndFromJsonStringOrderDependency() throws IOException {
    // Setup
    final ColumnPermutation expectedLhs =
      new ColumnPermutation(new ColumnIdentifier("table1", "column1"), new ColumnIdentifier(
        "table1", "column2"));
    final ColumnPermutation expectedRhs =
      new ColumnPermutation(new ColumnIdentifier("table1", "column3"), new ColumnIdentifier(
        "table1", "column4"));
    final OrderDependency expectedOD =
      new OrderDependency(expectedLhs, expectedRhs, OrderDependency.OrderType.LEXICOGRAPHICAL,
        OrderDependency.ComparisonOperator.SMALLER_EQUAL);

    JsonConverter<OrderDependency> jsonConverter = new JsonConverter<>();

    // Expected values
    String
      expectedJson =
      "{\"type\":\"OrderDependency\",\"comparisonOperator\":\"SMALLER_EQUAL\",\"lhs\":{\"columnIdentifiers\":[{\"tableIdentifier\":\"table1\",\"columnIdentifier\":\"column1\"},{\"tableIdentifier\":\"table1\",\"columnIdentifier\":\"column2\"}]},\"orderType\":\"LEXICOGRAPHICAL\",\"rhs\":{\"columnIdentifiers\":[{\"tableIdentifier\":\"table1\",\"columnIdentifier\":\"column3\"},{\"tableIdentifier\":\"table1\",\"columnIdentifier\":\"column4\"}]}}";

    // Execute functionality
    String actualJson = jsonConverter.toJsonString(expectedOD);

    // Check result
    assertEquals(expectedJson, actualJson);

    // Execute functionality
    OrderDependency actualOD = jsonConverter.fromJsonString(actualJson, OrderDependency.class);

    // Check result
    assertEquals(expectedOD, actualOD);
  }


  /**
   * A {@link FunctionalDependency} should be converted to a JSON String and creatable from a JSON
   * string.
   */
  @Test
  public void testToAndFromJsonStringFunctionalDependency() throws IOException {
    // Setup
    ColumnCombination
      expectedDeterminant =
      new ColumnCombination();
    ColumnIdentifier expectedDependant = new ColumnIdentifier("table1", "column7");
    FunctionalDependency
      expectedFD =
      new FunctionalDependency(expectedDeterminant, expectedDependant);

    JsonConverter<FunctionalDependency> jsonConverter = new JsonConverter<>();

    // Expected values
    String
      expectedJson =
      "{\"type\":\"FunctionalDependency\",\"determinant\":{\"columnIdentifiers\":[]},\"dependant\":{\"tableIdentifier\":\"table1\",\"columnIdentifier\":\"column7\"}}";

    // Execute functionality
    String actualJson = jsonConverter.toJsonString(expectedFD);

    // Check result
    assertEquals(expectedJson, actualJson);

    // Execute functionality
    FunctionalDependency
      actualFd =
      jsonConverter.fromJsonString(actualJson, FunctionalDependency.class);

    // Check result
    assertEquals(expectedFD, actualFd);
  }

  /**
   * A {@link UniqueColumnCombination} should be converted to a JSON String and creatable from a
   * JSON string.
   */
  @Test
  public void testToAndFromJsonStringUniqueColumnCombination() throws IOException {
    // Setup
    ColumnIdentifier expectedColumn1 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier expectedColumn2 = new ColumnIdentifier("table2", "column2");
    UniqueColumnCombination
      expectedUCC =
      new UniqueColumnCombination(expectedColumn1, expectedColumn2);

    JsonConverter<UniqueColumnCombination> jsonConverter = new JsonConverter<>();

    // Expected values
    String
      expectedJson =
      "{\"type\":\"UniqueColumnCombination\",\"columnCombination\":{\"columnIdentifiers\":[{\"tableIdentifier\":\"table1\",\"columnIdentifier\":\"column1\"},{\"tableIdentifier\":\"table2\",\"columnIdentifier\":\"column2\"}]}}";

    // Execute functionality
    String actualJson = jsonConverter.toJsonString(expectedUCC);

    // Check result
    assertEquals(expectedJson, actualJson);

    // Execute functionality
    UniqueColumnCombination
      actualUCC =
      jsonConverter.fromJsonString(actualJson, UniqueColumnCombination.class);

    // Check result
    assertEquals(expectedUCC, actualUCC);
  }


  /**
   * A {@link ConditionalUniqueColumnCombination} should be converted to a JSON String and creatable
   * from a JSON string.
   */
  @Test
  public void testToAndFromJsonStringConditionalUniqueColumnCombination() throws IOException {
    // Setup
    ColumnIdentifier expectedColumn1 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier expectedColumn2 = new ColumnIdentifier("table2", "column2");
    ColumnConditionOr outerCondition = new ColumnConditionOr();
    outerCondition.add(
      new ColumnConditionAnd(new ColumnConditionValue(expectedColumn1, "condition1"),
        new ColumnConditionValue(expectedColumn2, "condition2"),
        new ColumnConditionOr(
          new ColumnConditionValue(expectedColumn1, "condition4"),
          new ColumnConditionValue(expectedColumn1, "condition5"))));
    outerCondition
      .add(new ColumnConditionValue(expectedColumn1, "condition3"));
    outerCondition.add(
      new ColumnConditionAnd(new ColumnConditionValue(expectedColumn1, "condition6"),
        new ColumnConditionValue(expectedColumn2, "condition7"),
        new ColumnConditionOr(
          new ColumnConditionValue(expectedColumn1, "condition8"),
          new ColumnConditionValue(expectedColumn1, "condition9"))));

    ConditionalUniqueColumnCombination
      expectedCUCC =
      new ConditionalUniqueColumnCombination(
        new ColumnCombination(expectedColumn1, expectedColumn2),
        outerCondition);

    JsonConverter<ConditionalUniqueColumnCombination> jsonConverter = new JsonConverter<>();

    // Execute functionality
    String actualJson = jsonConverter.toJsonString(expectedCUCC);
    ConditionalUniqueColumnCombination
      actualCUCC =
      jsonConverter.fromJsonString(actualJson, ConditionalUniqueColumnCombination.class);

    // Check result
    assertEquals(expectedCUCC, actualCUCC);
  }

  /**
   * A {@link de.metanome.algorithm_integration.ColumnConditionValue} should be converted to a JSON
   * String and creatable from a JSON string.
   */
  @Test
  public void testToAndFromJsonStringColumnConditionValue() throws IOException {
    // Setup
    ColumnIdentifier columnIdentifier = new ColumnIdentifier("table1", "column1");
    ColumnConditionValue
      expectedColumnValue =
      new ColumnConditionValue(columnIdentifier, "condition1");

    // Execute functionality
    JsonConverter<ColumnConditionValue> jsonConverter = new JsonConverter<>();
    String actualJson = jsonConverter.toJsonString(expectedColumnValue);
    ColumnConditionValue
      actualColumnValue =
      jsonConverter.fromJsonString(actualJson, ColumnConditionValue.class);

    // Check result
    assertEquals(expectedColumnValue, actualColumnValue);
  }

}
