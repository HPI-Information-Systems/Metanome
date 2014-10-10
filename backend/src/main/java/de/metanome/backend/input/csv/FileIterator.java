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

import com.google.common.collect.ImmutableList;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;

import java.io.IOException;
import java.io.Reader;

/**
 * {@link FileIterator}s are Iterators over lines in a csv file.
 *
 * @author Jakob Zwiener
 */
public class FileIterator implements RelationalInput {

  public static final boolean DEFAULT_HAS_HEADER = false;
  public static final boolean DEFAULT_SKIP_DIFFERING_LINES = false;

  protected static final String DEFAULT_HEADER_STRING = "column";

  protected CSVReader csvReader;
  protected ImmutableList<String> headerLine;
  protected ImmutableList<String> nextLine;
  protected String relationName;
  protected int numberOfColumns = 0;
  // Initialized to -1 because of lookahead
  protected int currentLineNumber = -1;

  protected char separator = CSVParser.DEFAULT_SEPARATOR;
  protected char quotechar = CSVParser.DEFAULT_QUOTE_CHARACTER;
  protected char escape = CSVParser.DEFAULT_ESCAPE_CHARACTER;
  protected int skipLines = CSVReader.DEFAULT_SKIP_LINES;
  protected boolean strictQuotes = CSVParser.DEFAULT_STRICT_QUOTES;
  protected boolean ignoreLeadingWhiteSpace = CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE;
  protected boolean hasHeader = FileIterator.DEFAULT_HAS_HEADER;
  protected boolean skipDifferingLines = FileIterator.DEFAULT_SKIP_DIFFERING_LINES;

  public FileIterator(String relationName) {
    this.relationName = relationName;
  }

  /**
   * Initialize the CSV Reader.
   * This method should be called after all other parameters (e.g. separator, quote char) are set.
   * @param reader the reader
   * @return the file iterator
   * @throws InputIterationException if the next line could not be read
   */
  public FileIterator setReader(Reader reader) throws InputIterationException {
    this.csvReader =
        new CSVReader(reader,
                      this.separator,
                      this.quotechar,
                      this.escape,
                      this.skipLines,
                      this.strictQuotes,
                      this.ignoreLeadingWhiteSpace);

    this.nextLine = readNextLine();
    if (this.nextLine != null) {
      this.numberOfColumns = this.nextLine.size();
    }

    if (hasHeader) {
      this.headerLine = this.nextLine;
      next();
    }

    // If the header is still null generate a standard header the size of number of columns.
    if (this.headerLine == null) {
      this.headerLine = generateHeaderLine();
    }

    return this;
  }

  @Override
  public boolean hasNext() {
    return !(this.nextLine == null);
  }

  @Override
  public ImmutableList<String> next() throws InputIterationException {
    ImmutableList<String> currentLine = this.nextLine;

    if (currentLine == null) {
      return null;
    }
    this.nextLine = readNextLine();

    if (this.skipDifferingLines) {
      readToNextValidLine();
    } else {
      failDifferingLine(currentLine);
    }

    return currentLine;
  }

  protected void failDifferingLine(ImmutableList<String> currentLine)
      throws InputIterationException {
    if (currentLine.size() != this.numberOfColumns()) {
      throw new InputIterationException(
          "Csv line length did not match on line " + currentLineNumber);
    }
  }

  protected void readToNextValidLine() throws InputIterationException {
    if (!hasNext()) {
      return;
    }

    while (this.nextLine.size() != this.numberOfColumns()) {
      this.nextLine = readNextLine();
      if (!hasNext()) {
        break;
      }
    }
  }

  protected ImmutableList<String> generateHeaderLine() {
    String[] headerArray = new String[this.numberOfColumns];
    for (Integer i = 1; i <= this.numberOfColumns; i++) {
      headerArray[i - 1] = DEFAULT_HEADER_STRING + i.toString();
    }
    return ImmutableList.copyOf(headerArray);
  }

  protected ImmutableList<String> readNextLine() throws InputIterationException {
    String[] lineArray;
    try {
      lineArray = this.csvReader.readNext();
      currentLineNumber++;
    } catch (IOException e) {
      throw new InputIterationException("Could not read next line in csv file.");
    }
    if (lineArray == null) {
      return null;
    } else {
      return ImmutableList.copyOf(lineArray);
    }
  }

  @Override
  public void close() throws IOException {
    csvReader.close();
  }

  @Override
  public int numberOfColumns() {
    return numberOfColumns;
  }

  @Override
  public String relationName() {
    return relationName;
  }

  @Override
  public ImmutableList<String> columnNames() {
    return headerLine;
  }


  protected FileIterator setSeparator(char separator) {
    this.separator = separator;
    return this;
  }

  protected FileIterator setQuoteChar(char quoteChar) {
    this.quotechar = quoteChar;
    return this;
  }

  protected FileIterator setEscapeChar(char escape) {
    this.escape = escape;
    return this;
  }

  protected FileIterator setSkipLines(int skipLines) {
    this.skipLines = skipLines;
    return this;
  }

  protected FileIterator setStrictQuotes(boolean strictQuotes) {
    this.strictQuotes = strictQuotes;
    return this;
  }

  protected FileIterator setIgnoreLeadingWhiteSpace(boolean ignoreLeadingWhiteSpace) {
    this.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;
    return this;
  }

  protected FileIterator setHasHeader(boolean hasHeader) {
    this.hasHeader = hasHeader;
    return this;
  }

  protected FileIterator setSkipDifferingLines(boolean skipDifferingLines) {
    this.skipDifferingLines = skipDifferingLines;
    return this;
  }
}
