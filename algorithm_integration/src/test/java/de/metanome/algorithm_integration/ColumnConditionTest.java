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
package de.metanome.algorithm_integration;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link de.metanome.algorithm_integration.ColumnCondition}
 *
 * @author Jens Ehrlich
 */
public class ColumnConditionTest {

  /**
   * Test method for {@link de.metanome.algorithm_integration.ColumnCondition#toString()}
   */
  @Test
  public void testToString() {
    //Setup
    ColumnIdentifier column11 = new ColumnIdentifier("table1", "column1");
    ColumnIdentifier column12 = new ColumnIdentifier("table1", "column2");
    ColumnIdentifier column21 = new ColumnIdentifier("table2", "column1");

    ColumnCondition
      andCondition =
      new ColumnConditionAnd(new ColumnConditionValue(column11, "A"),
        new ColumnConditionValue(column12, "B"));
    ColumnCondition
      OrCondition =
      new ColumnConditionOr(andCondition, new ColumnConditionValue(column21, "A"));
    //check result
    assertEquals(
      "[table2.column1= A " + ColumnCondition.OR + " [table1.column1= A " + ColumnCondition.AND
        + " table1.column2= B]]",
      OrCondition.toString());
  }

}
