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
package de.metanome.backend.result_postprocessing.file_fixture;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.backend.input.file.FileIterator;

import java.io.StringReader;

public class FileFixtureGeneral {

  protected static final char QUOTE_CHAR = '\'';
  protected static final char SEPARATOR = ',';
  protected static final char ESCAPE = '\\';
  protected static final boolean STRICT_QUOTES = false;
  protected static final boolean IGNORE_LEADING_WHITESPACES = true;
  protected static final boolean HAS_HEADER = false;
  protected static final int SKIP_LINES = 0;

  public static final String TABLE_NAME = "general_file";

  public FileIterator getTestData() throws InputGenerationException, InputIterationException {
    return getTestData(false);
  }

  public FileIterator getTestData(boolean skipDifferingLines)
    throws InputIterationException, InputGenerationException {

    ConfigurationSettingFileInput setting = new ConfigurationSettingFileInput(TABLE_NAME)
      .setSeparatorChar(String.valueOf(SEPARATOR))
      .setHeader(HAS_HEADER)
      .setIgnoreLeadingWhiteSpace(IGNORE_LEADING_WHITESPACES)
      .setStrictQuotes(STRICT_QUOTES)
      .setEscapeChar(String.valueOf(ESCAPE))
      .setQuoteChar(String.valueOf(QUOTE_CHAR))
      .setSkipLines(SKIP_LINES)
      .setNullValue("null")
      .setSkipDifferingLines(skipDifferingLines);

    return new FileIterator(
      TABLE_NAME,
      new StringReader(
        Joiner.on(',').join(getLineOne()) + "\n" +
          Joiner.on(',').join(getLineTwo()) + "\n" +
          Joiner.on(',').join(getLineThree()) + "\n" +
          Joiner.on(',').join(getLineFour()) + "\n"),
      setting);
  }

  public ImmutableList<String> getLineOne() {
    return ImmutableList.of("1", "1", "1", "one");
  }

  public ImmutableList<String> getLineTwo() {
    return ImmutableList.of("2", "2", "2", "one");
  }

  public ImmutableList<String> getLineThree() {
    return ImmutableList.of("3", "2", "1", "two");
  }

  public ImmutableList<String> getLineFour() {
    return ImmutableList.of("4", "3", "2", "one");
  }

}
