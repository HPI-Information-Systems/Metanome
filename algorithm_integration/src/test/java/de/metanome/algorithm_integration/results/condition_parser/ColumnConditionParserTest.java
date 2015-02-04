/*
 * Copyright 2015 by the Metanome project
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

package de.metanome.algorithm_integration.results.condition_parser;

import de.metanome.algorithm_integration.ColumnCondition;
import de.metanome.algorithm_integration.ColumnConditionAnd;
import de.metanome.algorithm_integration.ColumnConditionOr;
import de.metanome.algorithm_integration.ColumnConditionValue;
import de.metanome.algorithm_integration.ColumnIdentifier;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ColumnConditionParserTest {

  private ColumnConditionParser parser = new ColumnConditionParser();

  ColumnIdentifier column1 = new ColumnIdentifier("table1", "column1");
  ColumnIdentifier column2 = new ColumnIdentifier("table2", "column2");
  ColumnCondition value1 = new ColumnConditionValue(column1, "condition1");
  ColumnCondition value2 = new ColumnConditionValue(column2, "condition2");
  ColumnCondition value3 = new ColumnConditionValue(column1, "condition3");
  ColumnCondition value4 = new ColumnConditionValue(column2, "condition4");

  ColumnConditionOr orCondition = new ColumnConditionOr(value1, value2);
  ColumnConditionAnd andCondition = new ColumnConditionAnd(value3, value4);
  ColumnCondition complexCondition = new ColumnConditionAnd(andCondition, orCondition);

  @Test
  public void testANDPattern() {
    ColumnConditionAnd actualAndCondition = parser.AND.parse(andCondition.toString());
    assertEquals(andCondition, actualAndCondition);
  }

  @Test
  public void testORPattern() {
    ColumnConditionOr actualOrCondition = parser.OR.parse(orCondition.toString());
    assertEquals(orCondition, actualOrCondition);
  }

  @Test
  public void testComplexCondition() {
    ColumnCondition actualCondition = parser.EXPR.parse(complexCondition.toString());
    assertEquals(complexCondition, actualCondition);
  }

  @Test
  public void testNOT_ANDPattern() {
    andCondition.setNegated(true);
    ColumnCondition actualAndCondition = parser.NOT_EXPR.parse(andCondition.toString());
    assertEquals(andCondition, actualAndCondition);
  }

  @Test
  public void testNOT_ORPattern() {
    orCondition.setNegated(true);
    ColumnCondition actualOrCondition = parser.NOT_EXPR.parse(orCondition.toString());
    assertEquals(orCondition, actualOrCondition);
  }

}
