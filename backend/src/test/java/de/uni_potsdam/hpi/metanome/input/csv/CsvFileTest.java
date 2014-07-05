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

package de.uni_potsdam.hpi.metanome.input.csv;

import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputGenerationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.input.csv.CsvFile}
 *
 * @author Jakbo Zwiener
 */
public class CsvFileTest {

  CsvFileOneLineFixture fixture;
  CsvFile csvFile;

  @Before
  public void setUp() throws Exception {
    this.fixture = new CsvFileOneLineFixture();
    this.csvFile = this.fixture.getTestData();
  }

  @After
  public void tearDown() throws Exception {
  }

  /**
   * Has next should be true once and false after calling next once (one csv line).
   */
  @Test
  public void testHasNext() throws InputIterationException {
    // Check result
    assertTrue(this.csvFile.hasNext());
    this.csvFile.next();
    assertFalse(this.csvFile.hasNext());
  }

  /**
   * A one line csv should be parsed correctly. And all the values in the line should be equal.
   */
  @Test
  public void testNext() throws InputIterationException {
    // Check result
    assertEquals(this.fixture.getExpectedStrings(), this.csvFile.next());
  }

  /**
   * A one line csv with differing separator should be parsed correctly, all the values in the line
   * should be correct.
   */
  @Test
  public void testNextSeparator() throws InputIterationException, InputGenerationException {
    // Setup
    CsvFileOneLineFixture fixtureSeparator = new CsvFileOneLineFixture(';');
    CsvFile csvFileSeparator = fixtureSeparator.getTestData();

    // Check result
    assertEquals(fixtureSeparator.getExpectedStrings(), csvFileSeparator.next());
  }

  /**
   * Test method for {@link CsvFile#next()}
   *
   * A csv with differing line lengths should be partially parsable if the skipDifferingLines
   * parameter is set to true.
   */
  @Test
  public void testParseThroughErrors() throws InputGenerationException, InputIterationException {
    // Setup
    CsvFileShortLineFixture shortLineFixture = new CsvFileShortLineFixture();
    CsvFile csvFileThroughErrors = shortLineFixture.getTestData(true);

    // Execute functionality
    // Check result
    assertEquals(shortLineFixture.getExpectedFirstParsableLine(), csvFileThroughErrors.next());
    assertEquals(shortLineFixture.getExpectedSecondParsableLine(), csvFileThroughErrors.next());
    // There should not be another parsable line.
    assertFalse(csvFileThroughErrors.hasNext());
  }

  /**
   * Test method for {@link CsvFile#next()} <p/> The first line in the csv should determine the line
   * length (it could be the header). Every next line should have this length or an {@link
   * de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException} should be
   * thrown.
   */
  @Test
  public void testShortWithHeader() throws InputIterationException {
    // Setup
    CsvFileShortLineWithHeaderFixture shortLineFixture = new CsvFileShortLineWithHeaderFixture();
    CsvFile csvFileShortWithHeader = shortLineFixture.getTestData();

    // Execute functionality
    // Check result
    try {
      csvFileShortWithHeader.next();
      fail("Expected an InputIterationException to be thrown.");
    } catch (InputIterationException e) {
      assertTrue(e.getMessage().contains("2"));
    }
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.input.csv.CsvFile#next()}
   *
   * A valid csv file without differing lines should be parsable with the skipDifferingLines
   * parameter set.
   */
  @Test
  public void testParseCorrectFileWithSkipDifferingLines() throws InputIterationException {
    // Setup
    CsvFileFixture csvFileFixture = new CsvFileFixture();
    CsvFile multiLineCsvFile = csvFileFixture.getTestData(true);

    // Execute functionality
    // Check result
    assertEquals(csvFileFixture.expectedHeader(), multiLineCsvFile.columnNames());
    assertEquals(csvFileFixture.expectedFirstLine(), multiLineCsvFile.next());
    assertEquals(csvFileFixture.expectedSecondLine(), multiLineCsvFile.next());
  }

  /**
   * Test method for {@link CsvFile#next()}
   *
   * When iterating over a csv file with alternating line length an exception should be thrown.
   */
  @Test
  public void testShort() throws InputIterationException, InputGenerationException {
    // Setup
    CsvFile shortCsvFile = new CsvFileShortLineFixture().getTestData();

    // Check result
    try {
      shortCsvFile.next();
      shortCsvFile.next();
      fail("Expected an InputIterationException to be thrown.");
    } catch (InputIterationException e) {
      assertTrue(e.getMessage().contains("2"));
    }
  }

  /**
   * Test method for {@link CsvFile#numberOfColumns()} <p/> A {@link CsvFile} should return the
   * correct number of columns of the file.
   */
  @Test
  public void testNumberOfColumns() throws InputIterationException, InputGenerationException {
    // Setup
    CsvFileOneLineFixture fixtureSeparator = new CsvFileOneLineFixture(';');
    CsvFile csvFile = fixtureSeparator.getTestData();

    // Check result
    assertEquals(fixture.getExpectedNumberOfColumns(), csvFile.numberOfColumns());
  }

  /**
   * Test method for {@link CsvFile#relationName()} <p/> A {@link CsvFile} should return a relation
   * name.
   */
  @Test
  public void testRelationName() throws InputIterationException, InputGenerationException {
    // Setup
    CsvFileOneLineFixture fixtureSeparator = new CsvFileOneLineFixture(';');
    CsvFile csvFile = fixtureSeparator.getTestData();

    // Execute functionality
    // Check result
    assertEquals(fixture.getExpectedRelationName(), csvFile.relationName());
  }

  /**
   * Test method for {@link CsvFile#columnNames()} <p/> A {@link CsvFile} should return the correct
   * column names.
   */
  @Test
  public void testColumnNames() throws InputIterationException, InputGenerationException {
    // Setup
    CsvFileOneLineFixture fixtureSeparator = new CsvFileOneLineFixture(';');
    CsvFile csvFile = fixtureSeparator.getTestData();

    // Execute functionality
    // Check result
    assertEquals(fixture.getExpectedColumnNames(), csvFile.columnNames());
  }

  /**
   * Test method for {@link CsvFile#generateHeaderLine()} <p/> A {@link CsvFile} should return the
   * correct header names.
   */
  @Test
  public void testGenerateHeaderLine() throws InputIterationException, InputGenerationException {
    // Setup
    CsvFileOneLineFixture fixtureSeparator = new CsvFileOneLineFixture(';');

    CsvFile csvFileWithHeader = fixtureSeparator.getTestData();
    CsvFile csvFileWithoutHeader = fixtureSeparator.getTestDataWithoutHeader();

    // Execute functionality
    // Check result
    assertEquals(fixture.getExpectedColumnNames(), csvFileWithHeader.columnNames());
    assertEquals(fixture.getExpectedDefaultColumnNames(), csvFileWithoutHeader.columnNames());
    assertEquals(fixture.getExpectedStrings(), csvFileWithoutHeader.next());
  }

  /**
   * Test method for {@link CsvFile#CsvFile(String, java.io.Reader, char, char)} <p/> A {@link
   * CsvFile} generated from an empty file should be constructable without exceptions, return false
   * on hasNext, return 0 as numberOfColumns and have a valid standard header.
   */
  @Test
  public void testConstructWithEmptyFile() throws InputIterationException, IOException {
    // Execute functionality
    // Check result
    // Should not throw exception
    CsvFile csvFile = new CsvFile("testRelation", new StringReader(""), ',', '"');
    assertFalse(csvFile.hasNext());
    assertEquals(0, csvFile.numberOfColumns());
    assertNotNull(csvFile.columnNames());

    // Cleanup
    csvFile.close();
  }

  /**
   * Test method for {@link CsvFile#CsvFile(String, java.io.Reader, char, char, int, boolean)} <p/>
   * A {@link CsvFile} with header generated from an empty file should be constructable without
   * exceptions, return false on hasNext, return 0 as numberOfColumns and have a valid standard
   * header.
   */
  @Test
  public void testConstructWithEmptyFileAndHeader() throws InputIterationException, IOException {
    // Execute functionality
    // Check result
    // Should not throw exception
    CsvFile csvFile = new CsvFile("testRelation", new StringReader(""), ',', '"', 0, true);
    assertFalse(csvFile.hasNext());
    assertEquals(0, csvFile.numberOfColumns());
    assertNotNull(csvFile.columnNames());

    // Cleanup
    csvFile.close();
  }
}
