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

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.input.InputIterationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

public class CsvFileFixture {

  protected static final char QUOTE_CHAR = '"';
  protected static final char SEPARATOR = ',';
  protected static final char ESCAPE = '\\';
  protected static final boolean STRICT_QUOTES = false;
  protected static final boolean IGNORE_LEADING_WHITESPACES = true;
  protected static final boolean HAS_HEADER = true;

  protected FileFixture fileFixture;
  protected ConfigurationSettingFileInput setting;

  public CsvFileFixture() {
    this.fileFixture = new FileFixture(getCsvFileData());
    this.setting = new ConfigurationSettingFileInput("some relation")
      .setSeparatorChar(String.valueOf(SEPARATOR))
      .setHeader(HAS_HEADER)
      .setIgnoreLeadingWhiteSpace(IGNORE_LEADING_WHITESPACES)
      .setStrictQuotes(STRICT_QUOTES)
      .setEscapeChar(String.valueOf(ESCAPE))
      .setQuoteChar(String.valueOf(QUOTE_CHAR));
  }

  public File getTestDataPath(String fileName)
    throws FileNotFoundException, UnsupportedEncodingException {
    return fileFixture.getTestData(fileName);
  }

  protected String getCsvFileData() {
    return Joiner.on(SEPARATOR).join(quoteStrings(expectedHeader())) +
      System.getProperty("line.separator") +
      Joiner.on(SEPARATOR).join(quoteStrings(expectedFirstLine())) +
      System.getProperty("line.separator") +
      Joiner.on(SEPARATOR).join(quoteStrings(expectedSecondLine())) +
      System.getProperty("line.separator") +
      Joiner.on(SEPARATOR).join(quoteStrings(expectedThirdLine()));
  }

  public FileIterator getTestData(boolean skipDifferingLines) throws InputIterationException {
    this.setting.setSkipDifferingLines(skipDifferingLines);
    return new FileIterator("some relation", new StringReader(getCsvFileData()), this.setting);
  }

  protected List<String> quoteStrings(List<String> unquotedStrings) {
    List<String> quotedStrings = new LinkedList<>();

    for (String unquotedString : unquotedStrings) {
      quotedStrings.add(QUOTE_CHAR + unquotedString + QUOTE_CHAR);
    }

    return quotedStrings;
  }

  public ImmutableList<String> expectedHeader() {
    return ImmutableList.of("one", "two", "three");
  }

  public ImmutableList<String> expectedFirstLine() {
    return ImmutableList.of("3", "4", "5");
  }

  public ImmutableList<String> expectedSecondLine() {
    return ImmutableList.of("6", "7", "8");
  }

  public ImmutableList<String> expectedThirdLine() {
    return ImmutableList.of("9", "10");
  }


}
