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

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DefaultFileInputGenerator}
 *
 * @author Jakob Zwiener
 */
public class DefaultFileInputGeneratorTest {

  protected CsvFileFixture csvFileFixture;
  protected File expectedFile;
  protected ConfigurationSettingFileInput expectedSetting;

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
    this.expectedFile = csvFileFixture.getTestDataPath("test.file");

    this.expectedSeparator = CsvFileFixture.SEPARATOR;
    this.expectedQuotechar = CsvFileFixture.QUOTE_CHAR;
    this.expectedEscape = '\\';
    this.expectedLine = 0;
    this.expectedStrictQuotes = true;
    this.expectedIgnoreLeadingWhiteSpace = true;
    this.expectedHasHeader = true;
    this.expectedSkipDifferingLines = true;

    this.expectedSetting = new ConfigurationSettingFileInput(expectedFile.getPath());
    this.expectedSetting.setEscapeChar(String.valueOf(this.expectedEscape));
    this.expectedSetting.setSkipLines(this.expectedLine);
    this.expectedSetting.setStrictQuotes(this.expectedStrictQuotes);
    this.expectedSetting.setIgnoreLeadingWhiteSpace(this.expectedIgnoreLeadingWhiteSpace);
    this.expectedSetting.setHeader(this.expectedHasHeader);
    this.expectedSetting.setSkipDifferingLines(this.expectedSkipDifferingLines);

    this.generator = new DefaultFileInputGenerator(expectedSetting);
  }

  /**
   * Test method for {@link DefaultFileInputGenerator#DefaultFileInputGenerator(java.io.File) and
   * all set methods.
   * <p/>
   * The generator should store the setting correctly.
   */
  @Test
  public void testConstructor() {
    // Check result
    assertEquals(expectedFile, generator.inputFile);
    assertEquals(expectedSetting, generator.getSetting());
  }

  /**
   * Test method for {@link de.metanome.backend.input.file.DefaultFileInputGenerator#DefaultFileInputGenerator(de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput)}
   * <p/>
   * The generator should store the file path correctly. The generator should use a default
   * setting.
   */
  @Test
  public void testConstructorSetting() throws AlgorithmConfigurationException,
    FileNotFoundException {
    // Setup
    ConfigurationSettingFileInput
      defaultSetting =
      new ConfigurationSettingFileInput(expectedFile.getPath());

    // Execute functionality
    DefaultFileInputGenerator actualGenerator = new DefaultFileInputGenerator(expectedFile);

    // Check result
    assertEquals(expectedFile, actualGenerator.inputFile);
    assertEquals(defaultSetting.hasHeader(), actualGenerator.getSetting().hasHeader());
    assertEquals(defaultSetting.isSkipDifferingLines(),
      actualGenerator.getSetting().isSkipDifferingLines());
    assertEquals(defaultSetting.isStrictQuotes(), actualGenerator.getSetting().isStrictQuotes());
  }

  /**
   * Test method for {@link DefaultFileInputGenerator#generateNewCopy()}
   * <p/>
   * The generator should generate fresh file files iterable from the start.
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
