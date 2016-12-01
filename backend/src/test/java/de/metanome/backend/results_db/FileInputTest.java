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
package de.metanome.backend.results_db;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import de.metanome.backend.input.file.FileIterator;
import de.metanome.backend.resources.AlgorithmResource;
import de.metanome.backend.resources.ExecutionResource;
import de.metanome.backend.resources.FileInputResource;
import de.metanome.test_helper.EqualsAndHashCodeTester;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link de.metanome.backend.results_db.FileInput}
 *
 * @author Jakob Zwiener
 */
public class FileInputTest {

  private FileInputResource fileInputResource = new FileInputResource();

  /**
   * Test method for {@link FileInput#FileInput(String)} <p/> After calling the constructor the
   * default parser parameters should be set.
   */
  @Test
  public void testConstructor() {
    // Execute functionality
    FileInput actualFileInput = new FileInput("fileInput");

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
    assertEquals(CSVParser.DEFAULT_SEPARATOR, actualFileInput.getSeparatorAsChar());
    assertEquals(CSVParser.DEFAULT_QUOTE_CHARACTER, actualFileInput.getQuoteCharAsChar());
    assertEquals(CSVParser.DEFAULT_ESCAPE_CHARACTER, actualFileInput.getEscapeCharAsChar());
    assertEquals(Integer.valueOf(CSVReader.DEFAULT_SKIP_LINES), actualFileInput.getSkipLines());
    assertEquals(CSVParser.DEFAULT_STRICT_QUOTES, actualFileInput.isStrictQuotes());
    assertEquals(CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE,
      actualFileInput.isIgnoreLeadingWhiteSpace());
    assertEquals(FileIterator.DEFAULT_HAS_HEADER, actualFileInput.isHasHeader());
    assertEquals(FileIterator.DEFAULT_SKIP_DIFFERING_LINES, actualFileInput.isSkipDifferingLines());
  }

  /**
   * Test method for {@link de.metanome.backend.results_db.FileInput#equals(Object)} and {@link
   * FileInput#hashCode()} <p/> Note that the equals and hashCode methods are inherited from {@link
   * de.metanome.backend.results_db.Input}.
   */
  @Test
  public void testEqualsAndHashCode() {
    // Setup
    int id = 42;
    FileInput fileInput = new FileInput("fileInput")
      .setId(id);
    FileInput equalFileInput = new FileInput("fileInput")
      .setId(id);
    FileInput notEqualFileInput = new FileInput("fileInput")
      .setId(23);

    // Execute functionality
    // Check result
    new EqualsAndHashCodeTester<FileInput>()
      .performBasicEqualsAndHashCodeChecks(fileInput, equalFileInput, notEqualFileInput);
  }

  @Test
  public void testGetIdentifier() {
    // Setup
    String fileName = "name";

    FileInput input = new FileInput(fileName);

    // Execute
    String actualIdentifier = input.getIdentifier();

    // Check
    assertEquals(fileName, actualIdentifier);
  }

  @Test
  public void testCascadingDelete() throws EntityStorageException {
    AlgorithmResource algorithmResource = new AlgorithmResource();
    ExecutionResource executionResource = new ExecutionResource();

    // Setup
    HibernateUtil.clear();

    // Store prerequisite objects in the database
    Algorithm algorithm = new Algorithm("example_ind_algorithm.jar");
    algorithmResource.store(algorithm);

    // Expected values
    long begin = new Date().getTime();
    Execution expectedExecution = new Execution(algorithm, begin);
    // Adding results and inputs
    // Results
    Result result = new Result("some result file path");
    expectedExecution.addResult(result);
    // Inputs
    FileInput expectedFileInput = new FileInput("fileInput");
    expectedExecution.addInput(expectedFileInput);

    // Execute functionality
    HibernateUtil.store(expectedFileInput);
    HibernateUtil.store(expectedExecution);

    // Execute
    fileInputResource.delete(expectedFileInput.id);

    // Check
    List<Execution> actualExecutions = executionResource.getAll();
    assertEquals(0, actualExecutions.size());
  }
}
