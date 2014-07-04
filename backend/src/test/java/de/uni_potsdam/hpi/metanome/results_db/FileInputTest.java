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

package de.uni_potsdam.hpi.metanome.results_db;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

import de.uni_potsdam.hpi.metanome.input.csv.CsvFile;
import de.uni_potsdam.hpi.metanome.test_helper.EqualsAndHashCodeTester;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link de.uni_potsdam.hpi.metanome.results_db.FileInput}
 *
 * @author Jakob Zwiener
 */
public class FileInputTest {

  /**
   * Test method for {@link FileInput#store()} and {@link de.uni_potsdam.hpi.metanome.results_db.FileInput#retrieve(long)}
   * <p/> FileInputs should be storable and retrievable by id.
   */
  @Test
  public void testPersistence() throws EntityStorageException {
    // Setup
    HibernateUtil.clear();

    // Expected values
    FileInput expectedFileInput = new FileInput();
    expectedFileInput.setHasHeader(true);

    // Execute functionality
    assertSame(expectedFileInput, expectedFileInput.store());
    long id = expectedFileInput.getId();
    FileInput actualFileInput = FileInput.retrieve(id);

    // Check result
    assertEquals(expectedFileInput, actualFileInput);
    assertTrue(actualFileInput.isHasHeader());

    // Cleanup
    HibernateUtil.clear();
  }

  /**
   * Test method for {@link FileInput#FileInput()} <p/> After calling the constructor the default
   * parser parameters should be set.
   */
  @Test
  public void testConstructor() {
    // Execute functionality
    FileInput actualFileInput = new FileInput();

    // Check result
    checkDefaultParserSettings(actualFileInput);
  }

  /**
   * Test method for {@link FileInput#FileInput(String)} <p/> After calling the constructor with a
   * file name, the file name should be set and the default parser settings.
   */
  @Test
  public void testConstructorFileName() {
    // Setup
    // Expected values
    String expectedFileName = "some file name";

    // Execute functionality
    FileInput actualFileInput = new FileInput(expectedFileName);

    // Check result
    assertEquals(expectedFileName, actualFileInput.getFileName());
    checkDefaultParserSettings(actualFileInput);
  }

  protected void checkDefaultParserSettings(FileInput actualFileInput) {
    assertEquals(CSVParser.DEFAULT_SEPARATOR, actualFileInput.getSeparator());
    assertEquals(CSVParser.DEFAULT_QUOTE_CHARACTER, actualFileInput.getQuotechar());
    assertEquals(CSVParser.DEFAULT_ESCAPE_CHARACTER, actualFileInput.getEscapechar());
    assertEquals(CSVReader.DEFAULT_SKIP_LINES, actualFileInput.getSkipLines());
    assertEquals(CSVParser.DEFAULT_STRICT_QUOTES, actualFileInput.isStrictQuotes());
    assertEquals(CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE,
                 actualFileInput.isIgnoreLeadingWhiteSpace());
    assertEquals(CsvFile.DEFAULT_HAS_HEADER, actualFileInput.isHasHeader());
    assertEquals(CsvFile.DEFAULT_SKIP_DIFFERING_LINES, actualFileInput.isSkipDifferingLines());
  }

  /**
   * Test method for {@link de.uni_potsdam.hpi.metanome.results_db.FileInput#equals(Object)} and
   * {@link FileInput#hashCode()} <p/> Note that the equals and hashCode methods are inherited from
   * {@link de.uni_potsdam.hpi.metanome.results_db.Input}.
   */
  @Test
  public void testEqualsAndHashCode() {
    // Setup
    int id = 42;
    FileInput fileInput = new FileInput()
        .setId(id);
    FileInput equalFileInput = new FileInput()
        .setId(id);
    FileInput notEqualFileInput = new FileInput()
        .setId(23);

    // Execute functionality
    // Check result
    new EqualsAndHashCodeTester<FileInput>()
        .performBasicEqualsAndHashCodeChecks(fileInput, equalFileInput, notEqualFileInput);
  }
}
