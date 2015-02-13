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

package de.metanome.backend.results_db;

import com.google.common.annotations.GwtCompatible;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

import de.metanome.backend.input.csv.FileIterator;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * Represents file inputs in the database.
 *
 * @author Jakob Zwiener
 */
@Entity
@GwtCompatible
public class FileInput extends Input implements Serializable {

  protected String fileName;
  protected String separator; //Todo: atm needs to be String instead of char for serialization
  protected String quoteChar; //Todo: atm needs to be String instead of char for serialization
  protected String escapeChar; //Todo: atm needs to be String instead of char for serialization
  protected Integer skipLines;
  protected boolean strictQuotes;
  protected boolean ignoreLeadingWhiteSpace;
  protected boolean hasHeader;
  protected boolean skipDifferingLines;
  protected String comment;

  /**
   * Constructs a FileInput with default parser settings.
   */
  public FileInput() {
    this.separator = String.valueOf(CSVParser.DEFAULT_SEPARATOR);
    this.quoteChar = String.valueOf(CSVParser.DEFAULT_QUOTE_CHARACTER);
    this.escapeChar = String.valueOf(CSVParser.DEFAULT_ESCAPE_CHARACTER);
    this.skipLines = CSVReader.DEFAULT_SKIP_LINES;
    this.strictQuotes = CSVParser.DEFAULT_STRICT_QUOTES;
    this.ignoreLeadingWhiteSpace = CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE;
    this.hasHeader = FileIterator.DEFAULT_HAS_HEADER;
    this.skipDifferingLines = FileIterator.DEFAULT_SKIP_DIFFERING_LINES;
  }

  /**
   * Constructs a FileInput with a given file name. Default parser settings are set.
   *
   * @param fileName input's file name
   */
  public FileInput(String fileName) {
    this();
    this.fileName = fileName;

  }

  public String getFileName() {
    return this.fileName;
  }

  public FileInput setFileName(String fileName) {
    this.fileName = fileName;

    return this;
  }

  public Integer getSkipLines() {
    return this.skipLines;
  }

  public FileInput setSkipLines(Integer skipLines) {
    this.skipLines = skipLines;

    return this;
  }

  public boolean isStrictQuotes() {
    return this.strictQuotes;
  }

  public FileInput setStrictQuotes(boolean strictQuotes) {
    this.strictQuotes = strictQuotes;

    return this;
  }

  public boolean isIgnoreLeadingWhiteSpace() {
    return this.ignoreLeadingWhiteSpace;
  }

  public FileInput setIgnoreLeadingWhiteSpace(boolean ignoreLeadingWhiteSpace) {
    this.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;

    return this;
  }

  public boolean isHasHeader() {
    return this.hasHeader;
  }

  public FileInput setHasHeader(boolean hasHeader) {
    this.hasHeader = hasHeader;

    return this;
  }

  public boolean isSkipDifferingLines() {
    return this.skipDifferingLines;
  }

  public FileInput setSkipDifferingLines(boolean skipDifferingLines) {
    this.skipDifferingLines = skipDifferingLines;

    return this;
  }

  public String getComment() {
    return this.comment;
  }

  public FileInput setComment(String comment) {
    this.comment = comment;

    return this;
  }

  @Override
  public FileInput setId(long id) {
    super.setId(id);

    return this;
  }

  @Override
  @Transient
  public String getIdentifier() {
    return this.fileName;
  }

  public FileInput setSeparator(String separator) {
    this.separator = separator;

    return this;
  }

  public String getSeparator() {
    return separator;
  }

  public FileInput setQuoteChar(String quoteChar) {
    this.quoteChar = quoteChar;

    return this;
  }

  public String getQuoteChar() {
    return quoteChar;
  }

  public FileInput setEscapeChar(String escapeChar) {
    this.escapeChar = escapeChar;

    return this;
  }

  public String getEscapeChar() {
    return escapeChar;
  }

  @Transient
  public char getSeparatorAsChar() { return toChar(this.separator); }
  @Transient
  public char getQuoteCharAsChar() {
    return toChar(this.quoteChar);
  }
  @Transient
  public char getEscapeCharAsChar() {
    return toChar(this.escapeChar);
  }
  @Transient
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

}
