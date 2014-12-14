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
import com.google.common.annotations.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;

import de.metanome.backend.input.csv.FileIterator;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Represents file inputs in the database.
 *
 * @author Jakob Zwiener
 */
@Entity
@GwtCompatible
public class FileInput extends Input implements IsSerializable {

  private static final long serialVersionUID = 2320081610461965426L;

  protected String fileName;
  protected String separator; //Todo: atm needs to be String instead of char for serialization
  protected String quotechar; //Todo: atm needs to be String instead of char for serialization
  protected String escapechar; //Todo: atm needs to be String instead of char for serialization
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
    this.quotechar = String.valueOf(CSVParser.DEFAULT_QUOTE_CHARACTER);
    this.escapechar = String.valueOf(CSVParser.DEFAULT_ESCAPE_CHARACTER);
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
    return fileName;
  }

  public FileInput setFileName(String fileName) {
    this.fileName = fileName;

    return this;
  }

  public char getSeparator() {
    return separator.charAt(0);
  }

  @XmlTransient
  public FileInput setSeparator(char separator) {
    this.separator = String.valueOf(separator);

    return this;
  }

  public char getQuotechar() {
    return quotechar.charAt(0);
  }

  @XmlTransient
  public FileInput setQuotechar(char quotechar) {
    this.quotechar = String.valueOf(quotechar);

    return this;
  }

  public char getEscapechar() {
    return escapechar.charAt(0);
  }

  @XmlTransient
  public FileInput setEscapechar(char escapechar) {
    this.escapechar = String.valueOf(escapechar);

    return this;
  }

  public Integer getSkipLines() {
    return skipLines;
  }

  public FileInput setSkipLines(Integer skipLines) {
    this.skipLines = skipLines;

    return this;
  }

  public boolean isStrictQuotes() {
    return strictQuotes;
  }

  public FileInput setStrictQuotes(boolean strictQuotes) {
    this.strictQuotes = strictQuotes;

    return this;
  }

  public boolean isIgnoreLeadingWhiteSpace() {
    return ignoreLeadingWhiteSpace;
  }

  public FileInput setIgnoreLeadingWhiteSpace(boolean ignoreLeadingWhiteSpace) {
    this.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;

    return this;
  }

  public boolean isHasHeader() {
    return hasHeader;
  }

  public FileInput setHasHeader(boolean hasHeader) {
    this.hasHeader = hasHeader;

    return this;
  }

  public boolean isSkipDifferingLines() {
    return skipDifferingLines;
  }

  public FileInput setSkipDifferingLines(boolean skipDifferingLines) {
    this.skipDifferingLines = skipDifferingLines;

    return this;
  }

  public String getComment() {
    return comment;
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
    return fileName;
  }

  public FileInput setSeparator(String separator) {
    this.separator = String.valueOf(separator.charAt(0));

    return this;
  }

  public FileInput setQuotechar(String quotechar) {
    this.quotechar = String.valueOf(quotechar.charAt(0));

    return this;
  }

  public FileInput setEscapechar(String escapechar) {
    this.escapechar = String.valueOf(escapechar.charAt(0));

    return this;
  }

}
