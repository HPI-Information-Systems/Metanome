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

package de.uni_potsdam.hpi.metanome.results_db;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;
import de.uni_potsdam.hpi.metanome.input.csv.CsvFile;

import javax.persistence.Entity;

/**
 * Represents file inputs in the database.
 *
 * @author Jakob Zwiener
 */
@Entity
public class FileInput extends Input {

    protected String fileName;
    protected char separator;
    protected char quotechar;
    protected char escapechar;
    protected int skipLines;
    protected boolean strictQuotes;
    protected boolean ignoreLeadingWhiteSpace;
    protected boolean hasHeader;
    protected boolean skipDifferingLines;

    /**
     * Constructs a FileInput with default parser settings.
     */
    public FileInput() {
        this.separator = CSVParser.DEFAULT_SEPARATOR;
        this.quotechar = CSVParser.DEFAULT_QUOTE_CHARACTER;
        this.escapechar = CSVParser.DEFAULT_ESCAPE_CHARACTER;
        this.skipLines = CSVReader.DEFAULT_SKIP_LINES;
        this.strictQuotes = CSVParser.DEFAULT_STRICT_QUOTES;
        this.ignoreLeadingWhiteSpace = CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE;
        this.hasHeader = CsvFile.DEFAULT_HAS_HEADER;
        this.skipDifferingLines = CsvFile.DEFAULT_SKIP_DIFFERING_LINES;
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

    /**
     * Stores an FileInput in the database.
     *
     * @param fileInput the FileInput to store
     * @throws EntityStorageException
     */
    public static void store(FileInput fileInput) throws EntityStorageException {
        HibernateUtil.store(fileInput);
    }

    /**
     * Retrieves a FileInput from the database.
     *
     * @param id the FileInput's id
     * @return the fileInput
     * @throws EntityStorageException
     */
    public static FileInput retrieve(long id) throws EntityStorageException {
        return (FileInput) HibernateUtil.retrieve(FileInput.class, id);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public char getSeparator() {
        return separator;
    }

    public void setSeparator(char separator) {
        this.separator = separator;
    }

    public char getQuotechar() {
        return quotechar;
    }

    public void setQuotechar(char quotechar) {
        this.quotechar = quotechar;
    }

    public char getEscapechar() {
        return escapechar;
    }

    public void setEscapechar(char escapechar) {
        this.escapechar = escapechar;
    }

    public int getSkipLines() {
        return skipLines;
    }

    public void setSkipLines(int skipLines) {
        this.skipLines = skipLines;
    }

    public boolean isStrictQuotes() {
        return strictQuotes;
    }

    public void setStrictQuotes(boolean strictQuotes) {
        this.strictQuotes = strictQuotes;
    }

    public boolean isIgnoreLeadingWhiteSpace() {
        return ignoreLeadingWhiteSpace;
    }

    public void setIgnoreLeadingWhiteSpace(boolean ignoreLeadingWhiteSpace) {
        this.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;
    }

    public boolean isHasHeader() {
        return hasHeader;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public boolean isSkipDifferingLines() {
        return skipDifferingLines;
    }

    public void setSkipDifferingLines(boolean skipDifferingLines) {
        this.skipDifferingLines = skipDifferingLines;
    }
}
