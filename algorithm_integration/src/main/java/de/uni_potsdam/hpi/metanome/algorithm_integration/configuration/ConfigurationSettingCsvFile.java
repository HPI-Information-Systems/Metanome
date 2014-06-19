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

package de.uni_potsdam.hpi.metanome.algorithm_integration.configuration;

import au.com.bytecode.opencsv.CSVParser;
import au.com.bytecode.opencsv.CSVReader;


/**
 * @author Jakob Zwiener
 * 
 * Stores one CSV file configuration.
 */
public class ConfigurationSettingCsvFile extends ConfigurationSettingDataSource {
    private static final long serialVersionUID = -770650561337139324L;

    private String fileName;
    private boolean advanced;
    private char separatorChar;
    private char quoteChar;
    private char escapeChar;
    private boolean strictQuotes;
    private boolean ignoreLeadingWhiteSpace;
    private int skipLines;
	private boolean header;
	private boolean skipDifferingLines;
	
	public final static char DEFAULT_SEPARATOR = CSVParser.DEFAULT_SEPARATOR;
	public final static char DEFAULT_QUOTE = CSVParser.DEFAULT_QUOTE_CHARACTER;
	public final static char DEFAULT_ESCAPE = CSVParser.DEFAULT_ESCAPE_CHARACTER;
	public final static boolean DEFAULT_STRICTQUOTES = CSVParser.DEFAULT_STRICT_QUOTES;
	public final static boolean DEFAULT_IGNORELEADINGWHITESPACE = CSVParser.DEFAULT_IGNORE_LEADING_WHITESPACE;
	public final static int DEFAULT_SKIPLINES = CSVReader.DEFAULT_SKIP_LINES;
	public final static boolean DEFAULT_HEADER = true;
	public final static boolean DEFAULT_SKIPDIFFERINGLINES = false;


    /**
     * Exists for GWT serialization.
     */
    public ConfigurationSettingCsvFile() {
    }

    /**
     * Simple constructor, uses default values.
     * 
     * @param fileName	the name of the CSV file
     */
    public ConfigurationSettingCsvFile(String fileName) {
    	this(fileName, false, DEFAULT_SEPARATOR, DEFAULT_QUOTE, DEFAULT_ESCAPE, DEFAULT_STRICTQUOTES, 
    			DEFAULT_IGNORELEADINGWHITESPACE, DEFAULT_SKIPLINES, DEFAULT_HEADER, DEFAULT_SKIPDIFFERINGLINES);
    }

    /**
     * Advanced constructor.
     * 
     * @param fileName	the name of the CSV file
     * @param advanced	true if the custom configurations should be used; that is, if one of the following parameters differs
     * from the default value
     * @param separator
     * @param quote
     * @param escape
     * @param strictQuotes
     * @param ignoreLeadingWhiteSpace
     * @param line
     * @param header
     * @param skipDifferingLines
     */
    public ConfigurationSettingCsvFile(String fileName, boolean advanced, char separator, char quote,
                                       char escape, boolean strictQuotes, boolean ignoreLeadingWhiteSpace, int line,
                                       boolean header, boolean skipDifferingLines) {
        this.fileName = fileName;
        this.advanced = advanced;
        this.separatorChar = separator;
        this.quoteChar = quote;
        this.escapeChar = escape;
        this.strictQuotes = strictQuotes;
        this.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;
        this.skipLines = line;
        this.header = header;
        this.skipDifferingLines  = skipDifferingLines;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String value) {
        this.fileName = value;
    }

    public boolean isAdvanced() {
        return advanced;
    }

    public void setAdvanced(boolean value) {
        this.advanced = value;
    }

    public char getSeparatorChar() {
        return separatorChar;
    }

    public void setSeparatorChar(char value) {
        this.separatorChar = value;
    }

    public char getQuoteChar() {
        return quoteChar;
    }

    public void setQuoteChar(char value) {
        this.quoteChar = value;
    }

    public char getEscapeChar() {
        return escapeChar;
    }

    public void setEscapeChar(char value) {
        this.escapeChar = value;
    }

    public boolean isStrictQuotes() {
        return strictQuotes;
    }

    public void setStrictQuotes(boolean value) {
        this.strictQuotes = value;
    }

    public boolean isIgnoreLeadingWhiteSpace() {
        return ignoreLeadingWhiteSpace;
    }

    public void setIgnoreLeadingWhiteSpace(boolean value) {
        this.ignoreLeadingWhiteSpace = value;
    }

    public int getSkipLines() {
        return skipLines;
    }

    public void setSkipLines(int value) {
        this.skipLines = value;
    }

	public boolean hasHeader() {
		return header;
	}

	public void setHeader(boolean header) {
		this.header = header;
	}
	
	public boolean isSkipDifferingLines() {
		return skipDifferingLines;
	}

	public void setSkipDifferingLines(boolean skipDifferingLines) {
		this.skipDifferingLines = skipDifferingLines;
	}
	
	@Override
	public String getValueAsString() {
		return fileName;
	}
}
