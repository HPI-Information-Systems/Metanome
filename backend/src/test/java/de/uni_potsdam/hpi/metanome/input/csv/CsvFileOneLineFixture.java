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

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

import au.com.bytecode.opencsv.CSVParser;

import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputGenerationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

public class CsvFileOneLineFixture {

  protected char separator;
  protected char quoteChar;

  public CsvFileOneLineFixture() {
    this(CSVParser.DEFAULT_SEPARATOR);
  }

  public CsvFileOneLineFixture(char separator) {
    this(separator, CSVParser.DEFAULT_QUOTE_CHARACTER);
  }

  public CsvFileOneLineFixture(char separator, char quoteChar) {
    this.separator = separator;
    this.quoteChar = quoteChar;
  }

  public CsvFile getTestData() throws InputIterationException, InputGenerationException {
    return new CsvFile(getExpectedRelationName(), new StringReader(getCsvInputString()),
                       this.separator, this.quoteChar, 0, true);
  }

  public CsvFile getTestDataWithoutHeader()
      throws InputIterationException, InputGenerationException {
    return new CsvFile(getExpectedRelationName(), new StringReader(getCsvInputString()),
                       this.separator, this.quoteChar, 1, false);
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
      defaultColumnNames.add(CsvFile.DEFAULT_HEADER_STRING + (i + 1));
    }

    return ImmutableList.copyOf(defaultColumnNames);
  }

  public int getExpectedNumberOfColumns() {
    return getExpectedStrings().size();
  }

}
