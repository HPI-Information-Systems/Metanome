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

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.input.FileInputGenerator;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Generator for {@link de.metanome.algorithm_integration.input.RelationalInput}s based on csv
 * files.
 *
 * @author Jakob Zwiener
 */
public class DefaultFileInputGenerator implements FileInputGenerator {

  protected File inputFile;
  protected char separator = CSVParser.DEFAULT_SEPARATOR;
  protected char quotechar = CSVParser.DEFAULT_QUOTE_CHARACTER;
  protected char escape = CSVParser.DEFAULT_ESCAPE_CHARACTER;
  protected int skipLines = CSVReader.DEFAULT_SKIP_LINES;
  protected boolean strictQuotes = CSVParser.DEFAULT_STRICT_QUOTES;
  protected boolean ignoreLeadingWhiteSpace = CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE;
  protected boolean hasHeader = FileIterator.DEFAULT_HAS_HEADER;
  protected boolean skipDifferingLines = FileIterator.DEFAULT_SKIP_DIFFERING_LINES;

  /**
   * @param inputFile the csv input
   */
  public DefaultFileInputGenerator(File inputFile) throws FileNotFoundException {
    this.setInputFile(inputFile);
  }

  /**
   * @param inputFile               the csv input
   * @param separator               cell separator
   * @param quotechar               cell quote character
   * @param escape                  escape character
   * @param skipLines               number of lines to skip
   * @param strictQuotes            sets if characters outside the quotes are ignored
   * @param ignoreLeadingWhiteSpace it true, parser should ignore white space before a quote in a
   *                                field
   * @param hasHeader               set if the csv has a header
   * @param skipDifferingLines      set if the csv file should skip lines with differing length
   */
  public DefaultFileInputGenerator(File inputFile, char separator, char quotechar, char escape,
                                   int skipLines,
                                   boolean strictQuotes, boolean ignoreLeadingWhiteSpace,
                                   boolean hasHeader,
                                   boolean skipDifferingLines) throws FileNotFoundException {
    this.setInputFile(inputFile);
    this.separator = separator;
    this.quotechar = quotechar;
    this.escape = escape;
    this.skipLines = skipLines;
    this.strictQuotes = strictQuotes;
    this.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;
    this.hasHeader = hasHeader;
    this.skipDifferingLines = skipDifferingLines;
  }

  /**
   * @param setting the settings to construct new {@link de.metanome.algorithm_integration.input.RelationalInput}s
   *                with
   * @throws AlgorithmConfigurationException thrown if the file cannot be found
   */
  public DefaultFileInputGenerator(ConfigurationSettingFileInput setting)
      throws AlgorithmConfigurationException {
    try {
      this.setInputFile(new File(setting.getFileName()));
    } catch (FileNotFoundException e) {
      throw new AlgorithmConfigurationException("File could not be found.", e);
    }
    this.separator = setting.getSeparatorChar();
    this.quotechar = setting.getQuoteChar();
    this.escape = setting.getEscapeChar();
    this.skipLines = setting.getSkipLines();
    this.strictQuotes = setting.isStrictQuotes();
    this.ignoreLeadingWhiteSpace = setting.isIgnoreLeadingWhiteSpace();
    this.hasHeader = setting.hasHeader();
    this.skipDifferingLines = setting.isSkipDifferingLines();
  }

  @Override
  public RelationalInput generateNewCopy() throws InputGenerationException {
    try {
      return new FileIterator(inputFile.getName(), new FileReader(inputFile), separator, quotechar,
                         escape, skipLines, strictQuotes, ignoreLeadingWhiteSpace, hasHeader,
                         skipDifferingLines);
    } catch (FileNotFoundException e) {
      throw new InputGenerationException("File not found.");
    } catch (InputIterationException e) {
      throw new InputGenerationException("Could not iterate over the first line of the csv file.");
    }
  }

  /**
   * @return inputFile
   */
  @Override
  public File getInputFile() {
    return inputFile;
  }

  protected void setInputFile(File inputFile) throws FileNotFoundException {
    if (!inputFile.isFile()) {
      throw new FileNotFoundException();
    }
    this.inputFile = inputFile;
  }
}
