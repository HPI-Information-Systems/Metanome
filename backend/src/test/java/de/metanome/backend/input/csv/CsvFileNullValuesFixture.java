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

import com.google.common.base.Joiner;

import de.metanome.algorithm_integration.input.InputGenerationException;
import de.metanome.algorithm_integration.input.InputIterationException;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A fixture generating a csv file with 4 rows. Rows 2 and 4 have differing lengths (2 (short) and 4
 * (long)).
 *
 * @author Tanja Bergmann
 */
public class CsvFileNullValuesFixture {

  public FileIterator getTestData() throws InputGenerationException, InputIterationException {
    return getTestData(false);
  }

  public FileIterator getTestData(boolean skipDifferingLines)
      throws InputIterationException, InputGenerationException {
    return new FileIterator("some_file")
        .setEscapeChar('\\')
        .setHasHeader(false)
        .setSkipDifferingLines(skipDifferingLines)
        .setStrictQuotes(false)
        .setIgnoreLeadingWhiteSpace(true)
        .setQuoteChar('\'')
        .setSeparator(',')
        .setSkipLines(0)
        .setReader(new StringReader(Joiner.on(',').join(getFirstLineWithEmptyStrings()) + "\n" +
                         Joiner.on(',').join(getSecondLineWithEmptyStrings())));
  }

  public List<String> getFirstLineWithEmptyStrings() {
    List<String> list = new ArrayList<>();
    list.add("one");
    list.add("");
    list.add("three");
    return Collections.unmodifiableList(list);
  }
  public List<String> getSecondLineWithEmptyStrings() {
    List<String> list = new ArrayList<>();
    list.add("four");
    list.add("five");
    list.add("");
    return Collections.unmodifiableList(list);
  }

  public List<String> getFirstLineWithNullValues() {
    List<String> list = new ArrayList<>();
    list.add("one");
    list.add(null);
    list.add("three");
    return Collections.unmodifiableList(list);
  }
  public List<String> getSecondLineWithNullValues() {
    List<String> list = new ArrayList<>();
    list.add("four");
    list.add("five");
    list.add(null);
    return Collections.unmodifiableList(list);
  }

}
