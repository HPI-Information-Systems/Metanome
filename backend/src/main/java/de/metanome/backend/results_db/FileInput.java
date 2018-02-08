/**
 * Copyright 2014-2016 by Metanome Project
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

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.annotations.GwtCompatible;
import de.metanome.backend.input.file.FileIterator;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * Represents file inputs in the database.
 *
 * @author Jakob Zwiener
 */
@Entity
@GwtCompatible
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.PROPERTY,
  property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = FileInput.class, name = "fileInput")
})
public class FileInput extends Input implements Serializable {

  private static final long serialVersionUID = 235437870676053281L;

  protected String fileName;
  protected String separator;
  protected String quoteChar;
  protected String escapeChar;
  protected Integer skipLines;
  protected boolean strictQuotes;
  protected boolean ignoreLeadingWhiteSpace;
  protected boolean hasHeader;
  protected boolean skipDifferingLines;
  protected String comment;
  protected String nullValue;

  // Exists for Serialization
  public FileInput() {
  }

  /**
   * Constructs a FileInput with a given file name. Default parser settings are set.
   *
   * @param fileName input's file name
   */
  public FileInput(String fileName) {
    super(fileName);

    this.fileName = fileName;
    this.separator = String.valueOf(CSVParser.DEFAULT_SEPARATOR);
    this.quoteChar = String.valueOf(CSVParser.DEFAULT_QUOTE_CHARACTER);
    this.escapeChar = String.valueOf(CSVParser.DEFAULT_ESCAPE_CHARACTER);
    this.skipLines = CSVReader.DEFAULT_SKIP_LINES;
    this.strictQuotes = CSVParser.DEFAULT_STRICT_QUOTES;
    this.ignoreLeadingWhiteSpace = CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE;
    this.hasHeader = FileIterator.DEFAULT_HAS_HEADER;
    this.skipDifferingLines = FileIterator.DEFAULT_SKIP_DIFFERING_LINES;
    this.nullValue = FileIterator.DEFAULT_NULL_VALUE;
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
  @JsonIgnore
  public String getIdentifier() {
    return this.fileName;
  }

  public String getSeparator() {
    return separator;
  }

  public FileInput setSeparator(String separator) {
    this.separator = separator;

    return this;
  }

  public String getQuoteChar() {
    return quoteChar;
  }

  public FileInput setQuoteChar(String quoteChar) {
    this.quoteChar = quoteChar;

    return this;
  }

  public String getEscapeChar() {
    return escapeChar;
  }

  public FileInput setEscapeChar(String escapeChar) {
    this.escapeChar = escapeChar;

    return this;
  }

  public String getNullValue() {
    return nullValue;
  }

  public FileInput setNullValue(String nullValue) {
    this.nullValue = nullValue;

    return this;
  }

  @Transient
  @JsonIgnore
  public char getSeparatorAsChar() {
    return toChar(this.separator);
  }

  @Transient
  @JsonIgnore
  public char getQuoteCharAsChar() {
    return toChar(this.quoteChar);
  }

  @Transient
  @JsonIgnore
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

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 31)
            .append(super.hashCode())
            .append(fileName)
            .append(separator)
            .append(quoteChar)
            .append(escapeChar)
            .append(skipLines)
            .append(strictQuotes)
            .append(ignoreLeadingWhiteSpace)
            .append(hasHeader)
            .append(skipDifferingLines)
            .append(comment)
            .append(nullValue)
            .toHashCode();
  }
}
