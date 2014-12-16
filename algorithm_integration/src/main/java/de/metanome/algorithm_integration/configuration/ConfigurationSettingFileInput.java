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

package de.metanome.algorithm_integration.configuration;

import com.google.common.annotations.GwtIncompatible;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.input.RelationalInputGeneratorInitializer;

import org.codehaus.jackson.annotate.JsonTypeName;


/**
 * Stores one file input's configuration settings.
 *
 * @author Jakob Zwiener
 */
@JsonTypeName("configurationSettingFileInput")
public class ConfigurationSettingFileInput implements ConfigurationSettingDataSource, ConfigurationSettingRelationalInput {

  public final static char DEFAULT_SEPARATOR = CSVParser.DEFAULT_SEPARATOR;
  public final static char DEFAULT_QUOTE = CSVParser.DEFAULT_QUOTE_CHARACTER;
  public final static char DEFAULT_ESCAPE = CSVParser.DEFAULT_ESCAPE_CHARACTER;
  public final static boolean DEFAULT_STRICTQUOTES = CSVParser.DEFAULT_STRICT_QUOTES;
  public final static boolean
      DEFAULT_IGNORELEADINGWHITESPACE =
      CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE;
  public final static int DEFAULT_SKIPLINES = CSVReader.DEFAULT_SKIP_LINES;
  public final static boolean DEFAULT_HEADER = true;
  public final static boolean DEFAULT_SKIPDIFFERINGLINES = false;

  private String fileName;
  private boolean advanced;
  private String separatorChar; //Todo: atm needs to be String instead of char for serialization
  private String quoteChar;     //Todo: atm needs to be String instead of char for serialization
  private String escapeChar;    //Todo: atm needs to be String instead of char for serialization
  private boolean strictQuotes;
  private boolean ignoreLeadingWhiteSpace;
  private int skipLines;
  private boolean header;
  private boolean skipDifferingLines;


  /**
   * Exists for GWT serialization.
   */
  public ConfigurationSettingFileInput() {
  }

  /**
   * Simple constructor, uses default values.
   *
   * @param fileName the name of the CSV file
   */
  public ConfigurationSettingFileInput(String fileName) {
    this(fileName, false, DEFAULT_SEPARATOR, DEFAULT_QUOTE, DEFAULT_ESCAPE, DEFAULT_STRICTQUOTES,
         DEFAULT_IGNORELEADINGWHITESPACE, DEFAULT_SKIPLINES, DEFAULT_HEADER,
         DEFAULT_SKIPDIFFERINGLINES);
  }

  /**
   * Advanced constructor.
   *
   * @param fileName the name of the CSV file
   * @param advanced true if the custom configurations should be used; that is, if one of the
   *                 following parameters differs from the default value
   */
  public ConfigurationSettingFileInput(String fileName, boolean advanced, char separator,
                                       char quote,
                                       char escape, boolean strictQuotes,
                                       boolean ignoreLeadingWhiteSpace, int line,
                                       boolean header, boolean skipDifferingLines) {
    this.fileName = fileName;
    this.advanced = advanced;
    this.separatorChar = String.valueOf(separator);
    this.quoteChar = String.valueOf(quote);
    this.escapeChar = String.valueOf(escape);
    this.strictQuotes = strictQuotes;
    this.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;
    this.skipLines = line;
    this.header = header;
    this.skipDifferingLines = skipDifferingLines;
  }

  public String getFileName() {
    return fileName;
  }

  public ConfigurationSettingFileInput setFileName(String value) {
    this.fileName = value;
    return this;
  }

  public boolean isAdvanced() {
    return advanced;
  }

  public void setAdvanced(boolean value) {
    this.advanced = value;
  }

  public char getSeparatorChar() {
    return separatorChar.charAt(0);
  }

  public ConfigurationSettingFileInput setSeparatorChar(char value) {
    this.separatorChar = String.valueOf(value);
    return this;
  }

  public char getQuoteChar() {
    return quoteChar.charAt(0);
  }

  public ConfigurationSettingFileInput setQuoteChar(char value) {
    this.quoteChar = String.valueOf(value);
    return this;
  }

  public char getEscapeChar() {
    return escapeChar.charAt(0);
  }

  public ConfigurationSettingFileInput setEscapeChar(char value) {
    this.escapeChar = String.valueOf(value);
    return this;
  }

  public boolean isStrictQuotes() {
    return strictQuotes;
  }

  public ConfigurationSettingFileInput setStrictQuotes(boolean value) {
    this.strictQuotes = value;
    return this;
  }

  public boolean isIgnoreLeadingWhiteSpace() {
    return ignoreLeadingWhiteSpace;
  }

  public ConfigurationSettingFileInput setIgnoreLeadingWhiteSpace(boolean value) {
    this.ignoreLeadingWhiteSpace = value;
    return this;
  }

  public int getSkipLines() {
    return skipLines;
  }

  public ConfigurationSettingFileInput setSkipLines(int value) {
    this.skipLines = value;
    return this;
  }

  public boolean hasHeader() {
    return header;
  }

  public ConfigurationSettingFileInput setHeader(boolean header) {
    this.header = header;
    return this;
  }

  public boolean isSkipDifferingLines() {
    return skipDifferingLines;
  }

  public ConfigurationSettingFileInput setSkipDifferingLines(boolean skipDifferingLines) {
    this.skipDifferingLines = skipDifferingLines;
    return this;
  }

  @Override
  public String getValueAsString() {
    return fileName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @GwtIncompatible("Can only be called from backend.")
  public void generate(RelationalInputGeneratorInitializer initializer)
      throws AlgorithmConfigurationException {
    initializer.initialize(this);
  }
}
