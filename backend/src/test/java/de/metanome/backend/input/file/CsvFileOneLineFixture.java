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

import au.com.bytecode.opencsv.CSVParser;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

public class CsvFileOneLineFixture {

  protected char separator;
  protected char quoteChar;
  protected ConfigurationSettingFileInput setting;

  public CsvFileOneLineFixture() {
    this(CSVParser.DEFAULT_SEPARATOR);
  }

  public CsvFileOneLineFixture(char separator) {
    this(separator, CSVParser.DEFAULT_QUOTE_CHARACTER);
  }

  public CsvFileOneLineFixture(char separator, char quoteChar) {
    this.separator = separator;
    this.quoteChar = quoteChar;
    this.setting = new ConfigurationSettingFileInput(this.getExpectedRelationName())
      .setSeparatorChar(String.valueOf(this.separator))
      .setQuoteChar(String.valueOf(this.quoteChar));
  }

  public FileIterator getTestData() throws InputIterationException, InputGenerationException {
    this.setting.setHeader(true);
    this.setting.setSkipLines(0);

    return new FileIterator(getExpectedRelationName(),
      new StringReader(getCsvInputString()),
      setting);
  }

  public FileIterator getTestDataWithoutHeader()
    throws InputIterationException, InputGenerationException {
    this.setting.setHeader(false);
    this.setting.setSkipLines(1);

    return new FileIterator(getExpectedRelationName(),
      new StringReader(getCsvInputString()),
      setting);
  }

  protected String getCsvInputString() {
    List<String> quotedHeader = quoteStrings(getExpectedColumnNames());
    List<String> quotedLine = quoteStrings(getExpectedStrings());

    StringBuilder csvBuilder = new StringBuilder();

    csvBuilder.append(buildLineString(quotedHeader));
    csvBuilder.append("\n");
    csvBuilder.append(buildLineString(quotedLine));

    return csvBuilder.toString();
  }

  protected String buildLineString(List<String> line) {
    return Joiner.on(this.separator).join(line);
  }

  /**
   * Puts the input strings into quotes.
   *
   * @return quoted strings
   */
  protected List<String> quoteStrings(List<String> unquotedStrings) {
    List<String> quotedStrings = new LinkedList<>();

    for (String unquotedString : unquotedStrings) {
      quotedStrings.add(this.quoteChar + unquotedString + this.quoteChar);
    }

    return quotedStrings;
  }

  public ImmutableList<String> getExpectedStrings() {
    return ImmutableList.of("value1", "value2", "value3");
  }

  public String getExpectedRelationName() {
    return "some_relation";
  }

  public ImmutableList<String> getExpectedColumnNames() {
    return ImmutableList.of("column1", "column2", "column3");
  }

  public ImmutableList<String> getExpectedDefaultColumnNames() {
    List<String> defaultColumnNames = new LinkedList<>();

    for (int i = 0; i < getExpectedNumberOfColumns(); i++) {
      defaultColumnNames.add(FileIterator.DEFAULT_HEADER_STRING + (i + 1));
    }

    return ImmutableList.copyOf(defaultColumnNames);
  }

  public int getExpectedNumberOfColumns() {
    return getExpectedStrings().size();
  }

}
