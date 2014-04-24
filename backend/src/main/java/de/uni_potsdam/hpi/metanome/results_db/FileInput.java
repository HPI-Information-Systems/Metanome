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

import javax.persistence.Entity;

/**
 * Represents file inputs in the database.
 *
 * @author Jakob Zwiener
 */
@Entity
public class FileInput extends Input {

    protected String fileName;
    protected String separator;
    protected String quotechar;
    protected String escapechar;
    protected int skipLines;
    protected boolean strictQuotes;
    protected boolean ignoreLeadingWhiteSpace;
    protected boolean hasHeader;

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

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getQuotechar() {
        return quotechar;
    }

    public void setQuotechar(String quotechar) {
        this.quotechar = quotechar;
    }

    public String getEscapechar() {
        return escapechar;
    }

    public void setEscapechar(String escapechar) {
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
}
