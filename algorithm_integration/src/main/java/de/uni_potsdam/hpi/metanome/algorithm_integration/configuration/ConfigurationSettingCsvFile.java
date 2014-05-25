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


/**
 * @author Jakob Zwiener
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
    private int line;
	private boolean header;
	private boolean skipDifferingLines;

    public ConfigurationSettingCsvFile() {
    }

    // TODO add constructor for is not advanced (only fileName and advanced = false)

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
        this.line = line;
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

    public int getLine() {
        return line;
    }

    public void setLine(int value) {
        this.line = value;
    }

    public String getValueAsString() {
        return fileName;
    }

	public boolean hasHeader() {
		return header;
	}

	public boolean isSkipDifferingLines() {
		return skipDifferingLines;
	}
}
