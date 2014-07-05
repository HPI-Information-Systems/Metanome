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

package de.uni_potsdam.hpi.metanome.algorithm_helper.data_structures;

import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link PLIBuilder}
 */
public class PLIBuilderTest {

  protected PLIBuilderFixture fixture;
  protected PLIBuilder builder;

  @Before
  public void setUp() throws Exception {
    fixture = new PLIBuilderFixture();

    builder = new PLIBuilder(fixture.getSimpleRelationalInput());
  }

  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test method for {@link PLIBuilder#getPLIList()} <p/> Tests that {@link PositionListIndex}es are
   * build correclty.
   */
  @Test
  public void testCalculatePLI() throws InputIterationException {
    // Setup
    // Expected values
    List<PositionListIndex> expectedPLIList = fixture.getExpectedPLIList();
    PositionListIndex[]
        expectedPLIArray =
        expectedPLIList.toArray(new PositionListIndex[expectedPLIList.size()]);

    // Execute functionality
    List<PositionListIndex> actualPLIList = builder.getPLIList();

    // Check result
    assertThat(actualPLIList, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedPLIArray));
  }

  /**
   * Test method for {@link PLIBuilder#getDistinctSortedColumns()} <p/> Creates the distinct sorted
   * columns from the raw plis.
   */
  @Test
  public void testGetDistinctSortedColumns() throws InputIterationException {
    // Setup
    // Expected values
    List<TreeSet<String>>
        expectedDistinctSortedColumns =
        fixture.getExpectedDistinctSortedColumns();

    // Execute functionality
    List<TreeSet<String>> actualDistinctSortedColumns = builder.getDistinctSortedColumns();

    // Check result
    assertEquals(expectedDistinctSortedColumns, actualDistinctSortedColumns);
  }
}
