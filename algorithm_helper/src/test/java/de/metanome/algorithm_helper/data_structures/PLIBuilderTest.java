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
package de.metanome.algorithm_helper.data_structures;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.TreeSet;

import static org.junit.Assert.*;

/**
 * Tests for {@link de.metanome.algorithm_helper.data_structures.PLIBuilder}
 */
public class PLIBuilderTest {

  protected PLIBuilderFixture fixture;
  protected PLIBuilder builder;

  @Before
  public void setUp() throws Exception {
    fixture = new PLIBuilderFixture();
    builder = new PLIBuilder(fixture.getInputGenerator().generateNewCopy());
  }

  @After
  public void tearDown() throws Exception {
  }

  /**
   * Test method for {@link PLIBuilder#getPLIList()} <p/> Tests that {@link
   * de.metanome.algorithm_helper.data_structures.PositionListIndex}es are build correctly.
   */
  @Test
  public void testCalculatePLINullEqualsNull() throws InputIterationException {
    // Setup
    // Expected values
    List<PositionListIndex> expectedPLIList = fixture.getExpectedPLIList(true);
    PositionListIndex[]
      expectedPLIArray =
      expectedPLIList.toArray(new PositionListIndex[expectedPLIList.size()]);

    // Execute functionality
    List<PositionListIndex> actualPLIList = builder.getPLIList();

    // Check result
    assertThat(actualPLIList, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedPLIArray));
  }

  /**
   * Test method for {@link PLIBuilder#getPLIList()} <p/> Tests that {@link
   * de.metanome.algorithm_helper.data_structures.PositionListIndex}es are build correctly.
   */
  @Test
  public void testCalculatePLINullNotEqualsNull()
    throws InputIterationException, InputGenerationException, AlgorithmConfigurationException {
    // Setup
    this.builder = new PLIBuilder(fixture.getInputGenerator().generateNewCopy(), false);
    // Expected values
    List<PositionListIndex> expectedPLIList = fixture.getExpectedPLIList(false);
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

  /**
   * Test methode for {@link de.metanome.algorithm_helper.data_structures.PLIBuilder#getNumberOfTuples}
   * <p/> The total number of tuples should be calculated if the PLIs are calculated
   */
  @Test
  public void testGetNumberOfTuples() throws InputIterationException {
    //Setup
    long expectedNumberOfColumns = fixture.getExpectedNumberOfTuples();

    //Execute functionality
    try {
      builder.getNumberOfTuples();
      fail();
    } catch (InputIterationException e) {
      //Intentionally left blank
    }
    builder.getPLIList();

    //Check result
    assertEquals(expectedNumberOfColumns, builder.getNumberOfTuples());

  }
}
