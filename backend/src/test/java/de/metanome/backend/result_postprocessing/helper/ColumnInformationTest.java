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
package de.metanome.backend.result_postprocessing.helper;

import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.backend.input.file.FileIterator;
import de.metanome.backend.result_postprocessing.file_fixture.FileFixtureDifferentColumnTypes;
import de.metanome.backend.result_postprocessing.file_fixture.FileFixtureUniqueColumn;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.*;

public class ColumnInformationTest {

  String columnName = "column";
  int columnIndex = 2;

  @Test
  public void testIsBooleanValue() throws InputIterationException {
    // Set Up
    ColumnInformation columnInformation = new ColumnInformation(this.columnName,
      this.columnIndex,
      new BitSet(),
      null,
      false);

    // Expected Values
    String trueBoolean = "y";
    String falseBoolean = "this is true";

    // Execute Functionality
    boolean isValid = columnInformation.isBooleanValue(trueBoolean);
    // Check
    assertTrue(isValid);

    // Execute Functionality
    isValid = columnInformation.isBooleanValue(falseBoolean);
    // Check
    assertFalse(isValid);
  }

  @Test
  public void testIsIntegerValue() throws InputIterationException {
    // Set Up
    ColumnInformation columnInformation = new ColumnInformation(this.columnName,
      this.columnIndex,
      new BitSet(),
      null,
      false);

    // Expected Values
    String trueInteger = "23413";
    String falseInteger = "1l";

    // Execute Functionality
    boolean isValid = columnInformation.isIntegerValue(trueInteger);
    // Check
    assertTrue(isValid);

    // Execute Functionality
    isValid = columnInformation.isIntegerValue(falseInteger);
    // Check
    assertFalse(isValid);
  }

  @Test
  public void testIsFloatValue() throws InputIterationException {
    // Set Up
    ColumnInformation columnInformation = new ColumnInformation(this.columnName,
      this.columnIndex,
      new BitSet(),
      null,
      false);

    // Expected Values
    String trueFloat = "112.32142";
    String falseFloat = "1234";

    // Execute Functionality
    boolean isValid = columnInformation.isFloatValue(trueFloat);
    // Check
    assertTrue(isValid);

    // Execute Functionality
    isValid = columnInformation.isFloatValue(falseFloat);
    // Check
    assertFalse(isValid);
  }

  @Test
  public void testIsDateValue() throws InputIterationException {
    // Set Up
    ColumnInformation columnInformation = new ColumnInformation(this.columnName,
      this.columnIndex,
      new BitSet(),
      null,
      false);

    // Expected Values
    String trueDate = "12.12.2001";
    String falseDate = "12121212";

    // Execute Functionality
    boolean isValid = columnInformation.isDateValue(trueDate);
    // Check
    assertTrue(isValid);

    // Execute Functionality
    isValid = columnInformation.isDateValue(falseDate);
    // Check
    assertFalse(isValid);
  }

  @Test
  public void testCreationOfDataDependentStatisticsForStringColumn()
    throws InputIterationException, InputGenerationException {
    // Expected Values
    BitSet expectedBitSet = new BitSet();
    expectedBitSet.set(0);

    // Set up
    FileFixtureDifferentColumnTypes fileFixture = new FileFixtureDifferentColumnTypes();
    FileIterator fileIterator = fileFixture.getTestData();

    // Execute Functionality
    ColumnInformation columnInformation = new ColumnInformation(this.columnName,
      0,
      expectedBitSet,
      fileIterator,
      true);

    // Check
    assertEquals(this.columnName, columnInformation.getColumnName());
    assertEquals(ColumnInformation.ColumnType.STRING_COLUMN, columnInformation.getColumnType());
    assertEquals(3, columnInformation.getDistinctValuesCount());
    assertEquals(0, columnInformation.getNullValuesCount());
    assertEquals(11, columnInformation.getRowCount());
    assertNotNull(columnInformation.getHistogram());
    assertEquals(3.2727272727, columnInformation.getAverageValueLength(), 0.01);
    assertEquals(expectedBitSet, columnInformation.getBitSet());
  }

  @Test
  public void testCreationOfDataDependentStatisticsForIntegerColumn()
    throws InputIterationException, InputGenerationException {
    // Expected Values
    BitSet expectedBitSet = new BitSet();
    expectedBitSet.set(1);

    // Set up
    FileFixtureDifferentColumnTypes fileFixture = new FileFixtureDifferentColumnTypes();
    FileIterator fileIterator = fileFixture.getTestData();

    // Execute Functionality
    ColumnInformation columnInformation = new ColumnInformation(this.columnName,
      1,
      expectedBitSet,
      fileIterator,
      true);

    // Check
    assertEquals(this.columnName, columnInformation.getColumnName());
    assertEquals(ColumnInformation.ColumnType.INTEGER_COLUMN, columnInformation.getColumnType());
    assertEquals(3, columnInformation.getDistinctValuesCount());
    assertEquals(0, columnInformation.getNullValuesCount());
    assertEquals(11, columnInformation.getRowCount());
    assertNotNull(columnInformation.getHistogram());
    assertEquals(0.0, columnInformation.getAverageValueLength(), 0.01);
    assertEquals(expectedBitSet, columnInformation.getBitSet());
  }

  @Test
  public void testCreationOfDataDependentStatisticsForDateColumn()
    throws InputIterationException, InputGenerationException {
    // Set up
    FileFixtureDifferentColumnTypes fileFixture = new FileFixtureDifferentColumnTypes();
    FileIterator fileIterator = fileFixture.getTestData();

    // Execute Functionality
    ColumnInformation columnInformation = new ColumnInformation(this.columnName,
      2,
      new BitSet(),
      fileIterator,
      true);

    // Check
    assertEquals(this.columnName, columnInformation.getColumnName());
    assertEquals(ColumnInformation.ColumnType.DATE_COLUMN, columnInformation.getColumnType());
    assertEquals(3, columnInformation.getDistinctValuesCount());
    assertEquals(4, columnInformation.getNullValuesCount());
    assertEquals(11, columnInformation.getRowCount());
    assertNotNull(columnInformation.getHistogram());
    assertEquals(0.0, columnInformation.getAverageValueLength(), 0.01);
  }

  @Test
  public void testGetNullRate()
    throws InputIterationException, InputGenerationException {
    // Set up
    FileFixtureDifferentColumnTypes fileFixture = new FileFixtureDifferentColumnTypes();
    FileIterator fileIterator = fileFixture.getTestData();

    // Execute Functionality
    ColumnInformation columnInformation = new ColumnInformation(this.columnName,
      2,
      new BitSet(),
      fileIterator,
      true);

    // Check
    assertEquals(0.363636, columnInformation.getNullRate(), 0.01);
  }

  @Test
  public void testIsUniqueColumn()
    throws InputIterationException, InputGenerationException {
    // Set up
    FileFixtureUniqueColumn fileFixture = new FileFixtureUniqueColumn();
    FileIterator fileIterator = fileFixture.getTestData();

    // Execute Functionality
    ColumnInformation columnInformation = new ColumnInformation(this.columnName,
      0,
      new BitSet(),
      fileIterator,
      true);

    // Check
    assertTrue(columnInformation.isUniqueColumn());

    // Execute Functionality
    fileIterator = fileFixture.getTestData();
    columnInformation = new ColumnInformation(this.columnName,
      1,
      new BitSet(),
      fileIterator,
      true);

    // Check
    assertFalse(columnInformation.isUniqueColumn());
  }

  @Test
  public void testGetUniquenessRate()
    throws InputIterationException, InputGenerationException {
    // Set up
    FileFixtureUniqueColumn fileFixture = new FileFixtureUniqueColumn();
    FileIterator fileIterator = fileFixture.getTestData();

    // Execute Functionality
    ColumnInformation columnInformation = new ColumnInformation(this.columnName,
      0,
      new BitSet(),
      fileIterator,
      true);

    // Check
    assertEquals(1.0, columnInformation.getUniquenessRate(), 0.0);

    // Execute Functionality
    fileIterator = fileFixture.getTestData();
    columnInformation = new ColumnInformation(this.columnName,
      1,
      new BitSet(),
      fileIterator,
      true);

    // Check
    assertEquals(0.66, columnInformation.getUniquenessRate(), 0.01);
  }

  @Test
  public void testGetInformationContent()
    throws InputIterationException, InputGenerationException {
    // Set up
    FileFixtureUniqueColumn fileFixture = new FileFixtureUniqueColumn();
    FileIterator fileIterator = fileFixture.getTestData();

    // Execute Functionality
    ColumnInformation columnInformation = new ColumnInformation(this.columnName,
      0,
      new BitSet(),
      fileIterator,
      true);

    // Check
    assertEquals(80.0, columnInformation.getInformationContent(3), 0.0);
  }

}
