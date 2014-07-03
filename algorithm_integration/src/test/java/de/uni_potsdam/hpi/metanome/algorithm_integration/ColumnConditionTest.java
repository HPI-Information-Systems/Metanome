/*
 * Copyright 2014 by the Metanome project
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

package de.uni_potsdam.hpi.metanome.algorithm_integration;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCondition}
 *
 * @author Jens Hildebrandt
 */
public class ColumnConditionTest {


    /**
     * Test method for {@link de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCondition#equals(Object)} and {@link ColumnCondition#hashCode()}
     * <p/>
     * {@link de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCondition} containing the same condition values for the same column should be equal
     */

    @Test
    public void testEqualsHashCode() {
        ColumnIdentifier column11 = new ColumnIdentifier("table1", "column1");
        ColumnIdentifier column12 = new ColumnIdentifier("table2", "column2");

        ColumnIdentifier column21 = new ColumnIdentifier("table1", "column1");

        List<String> conditionValues1 = new LinkedList<>();
        conditionValues1.add("a");
        conditionValues1.add("b");

        List<String> conditionValues2 = new LinkedList<>();
        conditionValues2.add("b");
        conditionValues2.add("a");

        List<String> conditionValues3 = new LinkedList<>();
        conditionValues3.add("a");
        conditionValues3.add("b");
        conditionValues3.add("c");

        ColumnCondition columnCondition1 = new ColumnCondition(column11, conditionValues1.toArray(new String[conditionValues1.size()]));

        ColumnCondition columnConditionEq = new ColumnCondition(column21, conditionValues1.toArray(new String[conditionValues1.size()]));
        ColumnCondition columnConditionEq2 = new ColumnCondition(column11, conditionValues2.toArray(new String[conditionValues2.size()]));

        ColumnCondition columnConditionNotEq = new ColumnCondition(column12, conditionValues1.toArray(new String[conditionValues1.size()]));
        ColumnCondition columnConditionNotEq2 = new ColumnCondition(column11, conditionValues3.toArray(new String[conditionValues3.size()]));

        assertEquals(columnCondition1.hashCode(), columnCondition1.hashCode());
        assertEquals(columnCondition1, columnCondition1);

        assertNotSame(columnCondition1, columnConditionEq);
        assertEquals(columnCondition1.hashCode(), columnConditionEq.hashCode());
        assertEquals(columnCondition1, columnConditionEq);

        assertNotSame(columnCondition1, columnConditionEq2);
        assertEquals(columnCondition1.hashCode(), columnConditionEq2.hashCode());
        assertEquals(columnCondition1, columnConditionEq2);

        assertNotSame(columnCondition1, columnConditionNotEq);
        assertNotEquals(columnCondition1, columnConditionNotEq);

        assertNotSame(columnCondition1, columnConditionNotEq2);
        assertNotEquals(columnCondition1, columnConditionNotEq2);
    }


  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCondition::toString()}
   */
  @Test
  public void testToString() {
    //Setup
    ColumnCondition actualColumnCondition = new ColumnCondition(
        new ColumnIdentifier("hello", "world"),
        "foo");
    //check result
    assertEquals("hello.world: [foo]", actualColumnCondition.toString());
  }
}
