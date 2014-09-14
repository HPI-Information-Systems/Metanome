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

package de.metanome.backend.input.csv;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DefaultFileInputGenerator}
 *
 * @author Jakob Zwiener
 */
public class DefaultFileInputGeneratorTest {

  protected CsvFileFixture csvFileFixture;
  protected File expectedFile;
  protected char expectedSeparator;
  protected char expectedQuotechar;
  protected char expectedEscape;
  protected int expectedLine;
  protected boolean expectedStrictQuotes;
  protected boolean expectedIgnoreLeadingWhiteSpace;
  protected boolean expectedHasHeader;
  protected boolean expectedSkipDifferingLines;
  protected DefaultFileInputGenerator generator;

  @Before
  public void setUp() throws Exception {
    this.csvFileFixture = new CsvFileFixture();
    this.expectedFile = csvFileFixture.getTestDataPath("test.csv");
    this.expectedSeparator = CsvFileFixture.SEPARATOR;
    this.expectedQuotechar = CsvFileFixture.QUOTE_CHAR;
    this.expectedEscape = '\\';
    this.expectedLine = 0;
    this.expectedStrictQuotes = true;
    this.expectedIgnoreLeadingWhiteSpace = true;
    this.expectedHasHeader = true;
    this.expectedSkipDifferingLines = true;
    this.generator =
        new DefaultFileInputGenerator(expectedFile, expectedSeparator, expectedQuotechar,
                                      expectedEscape,
                                      expectedLine, expectedStrictQuotes,
                                      expectedIgnoreLeadingWhiteSpace,
                                      expectedHasHeader, expectedSkipDifferingLines);
  }

  /**
   * Test method for {@link DefaultFileInputGenerator#DefaultFileInputGenerator(java.io.File, char,
   * char, char, int, boolean, boolean, boolean, boolean)}
   *
   * The generator should store the file path correctly.
   */
  @Test
  public void testConstructor() {
    // Check result
    assertEquals(expectedFile, generator.inputFile);
    assertEquals(expectedSeparator, generator.separator);
    assertEquals(expectedQuotechar, generator.quotechar);
    assertEquals(expectedEscape, generator.escape);
    assertEquals(expectedLine, generator.skipLines);
    assertEquals(expectedStrictQuotes, generator.strictQuotes);
    assertEquals(expectedIgnoreLeadingWhiteSpace, generator.ignoreLeadingWhiteSpace);
    assertEquals(expectedHasHeader, generator.hasHeader);
    assertEquals(expectedSkipDifferingLines, generator.skipDifferingLines);
  }

  /**
   * Test method for {@link de.metanome.backend.input.csv.DefaultFileInputGenerator#DefaultFileInputGenerator(de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput)}
   *
   * The generator should store the settings correctly.
   */
  @Test
  public void testConstructorSetting() throws AlgorithmConfigurationException {
    // Setup
    ConfigurationSettingFileInput expectedSetting = new ConfigurationSettingFileInput();
    expectedSetting.setFileName(expectedFile.getPath());
    expectedSetting.setSeparatorChar(expectedSeparator);
    expectedSetting.setQuoteChar(expectedQuotechar);
    expectedSetting.setEscapeChar(expectedEscape);
    expectedSetting.setSkipLines(expectedLine);
    expectedSetting.setStrictQuotes(expectedStrictQuotes);
    expectedSetting.setIgnoreLeadingWhiteSpace(expectedIgnoreLeadingWhiteSpace);
    expectedSetting.setHeader(expectedHasHeader);
    expectedSetting.setSkipDifferingLines(expectedSkipDifferingLines);

    // Execute functionality
    DefaultFileInputGenerator actualGenerator = new DefaultFileInputGenerator(expectedSetting);

    // Check result
    assertEquals(expectedFile, actualGenerator.inputFile);
    assertEquals(expectedSeparator, actualGenerator.separator);
    assertEquals(expectedQuotechar, actualGenerator.quotechar);
    assertEquals(expectedEscape, actualGenerator.escape);
    assertEquals(expectedLine, actualGenerator.skipLines);
    assertEquals(expectedStrictQuotes, actualGenerator.strictQuotes);
    assertEquals(expectedIgnoreLeadingWhiteSpace, actualGenerator.ignoreLeadingWhiteSpace);
    assertEquals(expectedHasHeader, actualGenerator.hasHeader);
    assertEquals(expectedSkipDifferingLines, actualGenerator.skipDifferingLines);
  }

  /**
   * Test method for {@link DefaultFileInputGenerator#generateNewCopy()}
   *
   * The generator should generate fresh csv files iterable from the start.
   */
  @Test
  public void testGenerateNewCsvFile() throws InputGenerationException, InputIterationException {
    // Setup
    RelationalInput fileInput = generator.generateNewCopy();

    // Check result
    assertEquals(expectedSkipDifferingLines, ((FileIterator) fileInput).skipDifferingLines);
    // The fileInput should contain both lines and iterate through them with next.
    assertEquals(csvFileFixture.expectedHeader(), fileInput.columnNames());
    assertEquals(csvFileFixture.expectedFirstLine(), fileInput.next());
    assertEquals(csvFileFixture.expectedSecondLine(), fileInput.next());
    // A new CsvFile should iterate from the start.
    RelationalInput csv2 = generator.generateNewCopy();
    assertEquals(csvFileFixture.expectedHeader(), fileInput.columnNames());
    assertEquals(csvFileFixture.expectedFirstLine(), csv2.next());
    assertEquals(csvFileFixture.expectedSecondLine(), csv2.next());
  }

  /**
   * Test method for {@link DefaultFileInputGenerator#getInputFile()}
   */
  @Test
  public void testGetInputFile() {
    // Execute functionality
    File actualFile = generator.getInputFile();

    // Check result
    assertEquals(expectedFile, actualFile);
  }
}
