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

import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
  protected boolean skipDifferingLines;
  protected List<String> headerLine;
  protected List<String> nextLine;
  protected String relationName;
  protected int numberOfColumns = 0;
  // Initialized to -1 because of lookahead
  protected int currentLineNumber = -1;
  protected int numberOfSkippedLines = 0;

  public FileIterator(String relationName, Reader reader, char separator, char quotechar)
      throws InputIterationException {
    this(relationName, reader, separator, quotechar, CSVReader.DEFAULT_SKIP_LINES);
  }

  public FileIterator(String relationName, Reader reader, char separator, char quoteChar,
                      int skipLines)
      throws InputIterationException {
    this(relationName, reader, separator, quoteChar, skipLines, DEFAULT_HAS_HEADER);
  }

  public FileIterator(String relationName, Reader reader, char separator, char quotechar,
                      int skipLines,
                      boolean hasHeader) throws InputIterationException {
    this(relationName, reader, separator, quotechar, CSVParser.DEFAULT_ESCAPE_CHARACTER, skipLines,
         CSVParser.DEFAULT_STRICT_QUOTES, CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE, hasHeader);
  }

  public FileIterator(String relationName, Reader reader, char separator, char quotechar,
                      char escape,
                      int skipLines, boolean strictQuotes, boolean ignoreLeadingWhiteSpace,
                      boolean hasHeader) throws InputIterationException {
    this(relationName, reader, separator, quotechar, escape, skipLines, strictQuotes,
         ignoreLeadingWhiteSpace, hasHeader, DEFAULT_SKIP_DIFFERING_LINES);
  }

  public FileIterator(String relationName, Reader reader, char separator, char quotechar,
                      char escape,
                      int skipLines, boolean strictQuotes, boolean ignoreLeadingWhiteSpace,
                      boolean hasHeader, boolean skipDifferingLines)
      throws InputIterationException {
    this.relationName = relationName;
    this.csvReader =
        new CSVReader(reader, separator, quotechar, escape, skipLines, strictQuotes,
                      ignoreLeadingWhiteSpace);
    this.skipDifferingLines = skipDifferingLines;

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
  }

  @Override
  public boolean hasNext() {
    return !(this.nextLine == null);
  }

  @Override
  public List<String> next() throws InputIterationException {
    List<String> currentLine = this.nextLine;

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

  protected void failDifferingLine(List<String> currentLine)
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
      this.numberOfSkippedLines++;
      if (!hasNext()) {
        break;
      }
    }
  }

  protected List<String> generateHeaderLine() {
    List<String> headerList = new ArrayList<String>();
    for (Integer i = 1; i <= this.numberOfColumns; i++) {
      headerList.add(DEFAULT_HEADER_STRING + i.toString());
    }
    return Collections.unmodifiableList(headerList);
  }

  protected List<String> readNextLine() throws InputIterationException {
    String[] lineArray;
    try {
      lineArray = this.csvReader.readNext();
      currentLineNumber++;
    } catch (IOException e) {
      throw new InputIterationException("Could not read next line in csv file.", e.getCause());
    }
    if (lineArray == null) {
      return null;
    } else {
      // Convert empty Strings to null
      List<String> list = new ArrayList<String>();
      for (String val : lineArray) {
        if (val.equals("")) {
          list.add(null);
        } else {
          list.add(val);
        }
      }
      // Return an immutable list
      return Collections.unmodifiableList(list);
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
  public List<String> columnNames() {
    return headerLine;
  }

  public int getNumberOfSkippedDifferingLines() {
    return numberOfSkippedLines;
  }
}
