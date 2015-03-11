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

import com.fasterxml.jackson.annotation.JsonTypeName;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.input.RelationalInputGeneratorInitializer;

import javax.xml.bind.annotation.XmlTransient;

/**
 * The setting of a {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirementFileInput}
 *
 * @author Jakob Zwiener
 */
@JsonTypeName("ConfigurationSettingFileInput")
public class ConfigurationSettingFileInput extends ConfigurationSettingRelationalInput {

  public final static char DEFAULT_SEPARATOR = CSVParser.DEFAULT_SEPARATOR;
  public final static char DEFAULT_QUOTE = CSVParser.DEFAULT_QUOTE_CHARACTER;
  public final static char DEFAULT_ESCAPE = CSVParser.DEFAULT_ESCAPE_CHARACTER;
  public final static boolean DEFAULT_STRICTQUOTES = CSVParser.DEFAULT_STRICT_QUOTES;
  public final static boolean DEFAULT_IGNORELEADINGWHITESPACE =
      CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE;
  public final static int DEFAULT_SKIPLINES = CSVReader.DEFAULT_SKIP_LINES;
  public final static boolean DEFAULT_HEADER = true;
  public final static boolean DEFAULT_SKIPDIFFERINGLINES = false;
  public final static String DEFAULT_NULL_VALUE = "";

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
  private String nullValue;

  // Needed for restful serialization
  public String type = "ConfigurationSettingFileInput";

  /**
   * Exists for serialization.
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
         DEFAULT_SKIPDIFFERINGLINES, DEFAULT_NULL_VALUE);
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
                                       boolean header, boolean skipDifferingLines,
                                       String nullValue) {
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
    this.nullValue = nullValue;
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

  public String getSeparatorChar() {
    return separatorChar;
  }

  public ConfigurationSettingFileInput setSeparatorChar(String value) {
    this.separatorChar = value;
    return this;
  }

  public String getQuoteChar() {
    return quoteChar;
  }

  public ConfigurationSettingFileInput setQuoteChar(String value) {
    this.quoteChar = value;
    return this;
  }

  public String getEscapeChar() {
    return escapeChar;
  }

  public ConfigurationSettingFileInput setEscapeChar(String value) {
    this.escapeChar = value;
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

  public String getNullValue() {
    return nullValue;
  }

  public ConfigurationSettingFileInput setNullValue(String nullValue) {
    this.nullValue = nullValue;
    return this;
  }

  @XmlTransient
  public char getSeparatorAsChar() { return toChar(this.separatorChar); }
  @XmlTransient
  public char getQuoteCharAsChar() {
    return toChar(this.quoteChar);
  }
  @XmlTransient
  public char getEscapeCharAsChar() {
    return toChar(this.escapeChar);
  }
  @XmlTransient
  private char toChar(String str) {
    if (str.isEmpty()) {
      return '\0';
    }

    if (str.startsWith("\\")) {
      switch (str) {
        case "\\t":
          return '\t';
        case "\\0":
          return '\0';
        case "\\n":
          return '\n';
      }
    }

    return str.charAt(0);
  }

  @Override
  @XmlTransient
  public String getValueAsString() {
    return fileName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @XmlTransient
  @GwtIncompatible("Can only be called from backend.")
  public void generate(RelationalInputGeneratorInitializer initializer)
      throws AlgorithmConfigurationException {
    initializer.initialize(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ConfigurationSettingFileInput that = (ConfigurationSettingFileInput) o;

    return !(!this.fileName.equals(that.fileName) ||
             !this.separatorChar.equals(that.separatorChar) ||
             !this.quoteChar.equals(that.quoteChar) ||
             !this.escapeChar.equals(that.escapeChar) ||
             !this.nullValue.equals(that.nullValue) ||
             !(this.strictQuotes != that.strictQuotes) ||
             !(this.ignoreLeadingWhiteSpace != that.ignoreLeadingWhiteSpace) ||
             !(this.skipDifferingLines != that.skipDifferingLines) ||
             !(this.header != that.header) ||
             !(this.skipLines != that.skipLines));
  }

  @Override
  public int hashCode() {
    int result = fileName.hashCode();
    result = 31 * result + this.separatorChar.hashCode();
    result = 31 * result + this.quoteChar.hashCode();
    result = 31 * result + this.escapeChar.hashCode();
    result = 31 * result + this.nullValue.hashCode();
    result = 31 * result + hashCode(this.strictQuotes);
    result = 31 * result + hashCode(this.ignoreLeadingWhiteSpace);
    result = 31 * result + hashCode(this.skipDifferingLines);
    result = 31 * result + hashCode(this.header);
    result = 31 * result + this.skipLines;
    return result;
  }

  private int hashCode(boolean bool) {
    return bool ? 1231 : 1237;
  }

}
