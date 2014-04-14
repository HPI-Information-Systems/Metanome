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

package de.uni_potsdam.hpi.metanome.frontend.client.parameter;



public class InputParameterCsvFile extends InputParameterDataSource {
	private static final long serialVersionUID = -4018145396259206308L;

	/** path of the currently selected input file **/
	private String filePath;
	
	/** advanced means that the parameter does not use default separator chars etc. **/
	private boolean advanced = false;
	
	/** character that marks beginning of a new field */
	private char separatorChar;
	private char quoteChar;
	private char escapeChar;
	private int line;
	private boolean strictQuotes;
	private boolean ignoreLeadingWhiteSpace;
	
	/** Default no-argument constructor. 
	 * Use <link>InputParameterCsvFile(String identifier) instead</link> **/
	public InputParameterCsvFile(){
		super();
	}

	/** 
	 * Creates an instance with the given identifier and no value.
	 * 
	 * @param identifier
	 */
	public InputParameterCsvFile(String identifier){
		super(identifier);
	}
	
	public String getFileNameValue() {
		return filePath;
	}
	public void setFileNameValue(String value) {
		this.filePath = value;
	}

	public boolean isAdvanced() {
		return advanced;
	}
	public void setAdvanced(boolean isAdvanced) {
		this.advanced = isAdvanced;
	}

	public char getSeparatorChar() {
		return separatorChar;
	}
	public void setSeparatorChar(char separatorChar) {
		this.separatorChar = separatorChar;
	}

	public char getQuoteChar() {
		return quoteChar;
	}
	public void setQuoteChar(char quoteChar) {
		this.quoteChar = quoteChar;
	}

	public char getEscapeChar() {
		return escapeChar;
	}
	public void setEscapeChar(char escapeChar){
		this.escapeChar = escapeChar;
	}

	public int getLine() {
		return line;
	}
	public void setLine(int line){
		this.line = line;
	}

	public boolean isStrictQuotes() {
		return strictQuotes;
	}
	public void setStrictQuotes(boolean strictQuotes){
		this.strictQuotes = strictQuotes;
	}

	public boolean isIgnoreLeadingWhiteSpace() {
		return ignoreLeadingWhiteSpace;
	}
	public void setIgnoreLeadingWhiteSpace(boolean ignoreLeadingWhiteSpace){
		this.ignoreLeadingWhiteSpace = ignoreLeadingWhiteSpace;
	}
	
	@Override
	public String getValueAsString() {
		return filePath;
		//TODO why was this? return filePath.substring(filePath.length()-10);
	}

	@Override
	public InputParameterDataSourceWidget createWrappingWidget() {
		return new InputParameterCsvFileWidget(this);
	}

}
