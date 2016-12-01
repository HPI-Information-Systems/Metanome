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
package de.metanome.backend.input.file;

import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.*;

/**
 * Tests for {@link FileIterator}
 *
 * @author Jakbo Zwiener
 */
public class FileIteratorTest {

  CsvFileOneLineFixture fixture;
  FileIterator fileIterator;

  @Before
  public void setUp() throws Exception {
    this.fixture = new CsvFileOneLineFixture();
    this.fileIterator = this.fixture.getTestData();
  }

  @After
  public void tearDown() throws Exception {
  }

  /**
   * Has next should be true once and false after calling next once (one file line).
   */
  @Test
  public void testHasNext() throws InputIterationException {
    // Check result
    assertTrue(this.fileIterator.hasNext());
    this.fileIterator.next();
    assertFalse(this.fileIterator.hasNext());
  }

  /**
   * A one line file should be parsed correctly. And all the values in the line should be equal.
   */
  @Test
  public void testNext() throws InputIterationException {
    // Check result
    assertEquals(this.fixture.getExpectedStrings(), this.fileIterator.next());
  }

  /**
   * A one line file with differing separator should be parsed correctly, all the values in the line
   * should be correct.
   */
  @Test
  public void testNextSeparator() throws InputIterationException, InputGenerationException {
    // Setup
    CsvFileOneLineFixture fixtureSeparator = new CsvFileOneLineFixture(';');
    FileIterator csvFileSeparator = fixtureSeparator.getTestData();

    // Check result
    assertEquals(fixtureSeparator.getExpectedStrings(), csvFileSeparator.next());
    assertEquals(0, csvFileSeparator.getNumberOfSkippedDifferingLines());
  }

  /**
   * Test method for {@link FileIterator#next()}
   * <p/>
   * A file with differing line lengths should be partially parsable if the skipDifferingLines
   * parameter is set to true.
   */
  @Test
  public void testParseThroughErrors() throws InputGenerationException, InputIterationException {
    // Setup
    CsvFileShortLineFixture shortLineFixture = new CsvFileShortLineFixture();
    FileIterator csvFileThroughErrors = shortLineFixture.getTestData(true);

    // Execute functionality
    // Check result
    assertEquals(shortLineFixture.getExpectedFirstParsableLine(), csvFileThroughErrors.next());
    assertEquals(shortLineFixture.getExpectedSecondParsableLine(), csvFileThroughErrors.next());
    // There should not be another parsable line.
    assertFalse(csvFileThroughErrors.hasNext());
    // Two lines should be skipped
    assertEquals(2, csvFileThroughErrors.getNumberOfSkippedDifferingLines());
  }

  /**
   * Test method for {@link FileIterator#next()} <p/> The first line in the file should determine
   * the line length (it could be the header). Every next line should have this length or an {@link
   * de.metanome.algorithm_integration.input.InputIterationException} should be thrown.
   */
  @Test
  public void testShortWithHeader() throws InputIterationException {
    // Setup
    CsvFileShortLineWithHeaderFixture shortLineFixture = new CsvFileShortLineWithHeaderFixture();
    FileIterator csvFileShortWithHeader = shortLineFixture.getTestData();

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
   * Test method for {@link FileIterator#next()} <p/> All empty Strings in a CSV file should be
   * parsed to null values.
   */
  @Test
  public void testParsingOfEmptyStrings() throws InputIterationException, InputGenerationException {
    // Setup
    CsvFileNullValuesFixture nullValuesFixture = new CsvFileNullValuesFixture();
    FileIterator csvFile = nullValuesFixture.getTestData();

    // Execute functionality
    // Check result
    assertEquals(nullValuesFixture.getFirstLineWithNullValues(), csvFile.next());
    assertEquals(nullValuesFixture.getSecondLineWithNullValues(), csvFile.next());
  }

  /**
   * Test method for {@link FileIterator#next()}
   * <p/>
   * A valid file file without differing lines should be parsable with the skipDifferingLines
   * parameter set.
   */
  @Test
  public void testParseCorrectFileWithSkipDifferingLines() throws InputIterationException {
    // Setup
    CsvFileFixture csvFileFixture = new CsvFileFixture();
    FileIterator multiLineCsvFile = csvFileFixture.getTestData(true);

    // Execute functionality
    // Check result
    assertEquals(csvFileFixture.expectedHeader(), multiLineCsvFile.columnNames());
    assertEquals(csvFileFixture.expectedFirstLine(), multiLineCsvFile.next());
    assertEquals(csvFileFixture.expectedSecondLine(), multiLineCsvFile.next());
    assertEquals(1, multiLineCsvFile.getNumberOfSkippedDifferingLines());
  }

  /**
   * Test method for {@link FileIterator#next()}
   * <p/>
   * When iterating over a file file with alternating line length an exception should be thrown.
   */
  @Test
  public void testShort() throws InputIterationException, InputGenerationException {
    // Setup
    FileIterator shortCsvFile = new CsvFileShortLineFixture().getTestData();

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
   * Test method for {@link FileIterator#next()}
   * <p/>
   * A tsv with differing line lengths should be partially parsable if the skipDifferingLines
   * parameter is set to true.
   */
  @Test
  public void testTsvParseThroughErrors() throws InputGenerationException, InputIterationException {
    // Setup
    TsvFileFixture shortLineFixture = new TsvFileFixture();
    FileIterator tsvFile = shortLineFixture.getTestData(true);

    // Execute functionality
    // Check result
    assertEquals(shortLineFixture.getExpectedFirstParsableLine(), tsvFile.next());
    assertEquals(shortLineFixture.getExpectedSecondParsableLine(), tsvFile.next());
    // There should not be another parsable line.
    assertFalse(tsvFile.hasNext());
    // Two lines should be skipped
    assertEquals(2, tsvFile.getNumberOfSkippedDifferingLines());
  }


  /**
   * Test method for {@link FileIterator#numberOfColumns()} <p/> A {@link FileIterator} should
   * return the correct number of columns of the file.
   */
  @Test
  public void testNumberOfColumns() throws InputIterationException, InputGenerationException {
    // Setup
    CsvFileOneLineFixture fixtureSeparator = new CsvFileOneLineFixture(';');
    FileIterator fileIterator = fixtureSeparator.getTestData();

    // Check result
    assertEquals(fixture.getExpectedNumberOfColumns(), fileIterator.numberOfColumns());
  }

  /**
   * Test method for {@link FileIterator#relationName()} <p/> A {@link FileIterator} should return a
   * relation name.
   */
  @Test
  public void testRelationName() throws InputIterationException, InputGenerationException {
    // Setup
    CsvFileOneLineFixture fixtureSeparator = new CsvFileOneLineFixture(';');
    FileIterator fileIterator = fixtureSeparator.getTestData();

    // Execute functionality
    // Check result
    assertEquals(fixture.getExpectedRelationName(), fileIterator.relationName());
  }

  /**
   * Test method for {@link FileIterator#columnNames()} <p/> A {@link FileIterator} should return
   * the correct column names.
   */
  @Test
  public void testColumnNames() throws InputIterationException, InputGenerationException {
    // Setup
    CsvFileOneLineFixture fixtureSeparator = new CsvFileOneLineFixture(';');
    FileIterator fileIterator = fixtureSeparator.getTestData();

    // Execute functionality
    // Check result
    assertEquals(fixture.getExpectedColumnNames(), fileIterator.columnNames());
  }

  /**
   * Test method for {@link FileIterator#generateHeaderLine()} <p/> A {@link FileIterator} should
   * return the correct header names.
   */
  @Test
  public void testGenerateHeaderLine() throws InputIterationException, InputGenerationException {
    // Setup
    CsvFileOneLineFixture fixtureSeparator = new CsvFileOneLineFixture(';');

    FileIterator csvFileWithHeader = fixtureSeparator.getTestData();
    FileIterator csvFileWithoutHeader = fixtureSeparator.getTestDataWithoutHeader();

    // Execute functionality
    // Check result
    assertEquals(fixture.getExpectedColumnNames(), csvFileWithHeader.columnNames());
    assertEquals(fixture.getExpectedDefaultColumnNames(), csvFileWithoutHeader.columnNames());
    assertEquals(fixture.getExpectedStrings(), csvFileWithoutHeader.next());
  }

  /**
   * Test method for {@link FileIterator#FileIterator(String, java.io.Reader,
   * de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput)}  <p/> A {@link
   * FileIterator} generated from an empty file should be constructable without exceptions, return
   * false on hasNext, return 0 as numberOfColumns and have a valid standard header.
   */
  @Test
  public void testConstructWithEmptyFile() throws InputIterationException, IOException {
    // Set up
    ConfigurationSettingFileInput setting = new ConfigurationSettingFileInput("testRelation")
      .setSeparatorChar(",")
      .setQuoteChar("\"");

    // Execute functionality
    // Should not throw exception
    FileIterator fileIterator = new FileIterator("testRelation", new StringReader(""), setting);

    // Check result
    assertFalse(fileIterator.hasNext());
    assertEquals(0, fileIterator.numberOfColumns());
    assertNotNull(fileIterator.columnNames());

    // Cleanup
    fileIterator.close();
  }

  /**
   * Test method for {@link FileIterator#FileIterator(String, java.io.Reader,
   * de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput)}  <p/> A {@link
   * FileIterator} with header generated from an empty file should be constructable without
   * exceptions, return false on hasNext, return 0 as numberOfColumns and have a valid standard
   * header.
   */
  @Test
  public void testConstructWithEmptyFileAndHeader() throws InputIterationException, IOException {
    // Set up
    ConfigurationSettingFileInput setting = new ConfigurationSettingFileInput("testRelation")
      .setSeparatorChar(",")
      .setQuoteChar("\"")
      .setHeader(true)
      .setSkipLines(0);

    // Execute functionality
    // Should not throw exception
    FileIterator
      fileIterator = new FileIterator("testRelation", new StringReader(""), setting);

    // Check result
    assertFalse(fileIterator.hasNext());
    assertEquals(0, fileIterator.numberOfColumns());
    assertNotNull(fileIterator.columnNames());

    // Cleanup
    fileIterator.close();
  }
}
