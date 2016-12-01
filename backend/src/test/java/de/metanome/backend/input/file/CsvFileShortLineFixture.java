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
import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;

import java.io.StringReader;

/**
 * A fixture generating a file file with 4 rows. Rows 2 and 4 have differing lengths (2 (short) and
 * 4 (long)).
 *
 * @author Jakob Zwiener
 */
public class CsvFileShortLineFixture {

  protected static final char QUOTE_CHAR = '\'';
  protected static final char SEPARATOR = ',';
  protected static final char ESCAPE = '\\';
  protected static final boolean STRICT_QUOTES = false;
  protected static final boolean IGNORE_LEADING_WHITESPACES = true;
  protected static final boolean HAS_HEADER = false;
  protected static final int SKIP_LINES = 0;

  public FileIterator getTestData() throws InputGenerationException, InputIterationException {
    return getTestData(false);
  }

  public FileIterator getTestData(boolean skipDifferingLines)
    throws InputIterationException, InputGenerationException {
    ConfigurationSettingFileInput setting = new ConfigurationSettingFileInput("some_file")
      .setSeparatorChar(String.valueOf(SEPARATOR))
      .setHeader(HAS_HEADER)
      .setIgnoreLeadingWhiteSpace(IGNORE_LEADING_WHITESPACES)
      .setStrictQuotes(STRICT_QUOTES)
      .setEscapeChar(String.valueOf(ESCAPE))
      .setQuoteChar(String.valueOf(QUOTE_CHAR))
      .setSkipLines(SKIP_LINES)
      .setSkipDifferingLines(skipDifferingLines);

    return new FileIterator("some_file",
      new StringReader(
        Joiner.on(',').join(getExpectedFirstParsableLine()) +
          "\nfour,five\n" +
          Joiner.on(',').join(getExpectedSecondParsableLine()) +
          "\nnine,ten,eleven,twelve"),
      setting);
  }

  public ImmutableList<String> getExpectedFirstParsableLine() {
    return ImmutableList.of("one", "two", "three");
  }

  public ImmutableList<String> getExpectedSecondParsableLine() {
    return ImmutableList.of("six", "seven", "eight");
  }

}
