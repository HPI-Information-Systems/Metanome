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


/**
 * Client compatible parameter for a requested SQLInputGenerator
 */
public class InputParameterSQLIterator extends InputParameterDataSource {

	private static final long serialVersionUID = -8258047430500209172L;

	private String dbUrl;
	private String userName;
	private String password;
	
	/** 
	 * Default no-argument constructor. 
	 * Use <link>InputParameterSQLIterator(String identifier) instead</link> 
	 **/
	public InputParameterSQLIterator(){
		super();
	}
	
	public InputParameterSQLIterator(String identifier) {
		super(identifier);
	}

	public String getDbUrl() {
		return dbUrl;
	}
	public void setDbUrl(String dbUrl){
		this.dbUrl = dbUrl;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password){
		this.password = password;
	}
	
	@Override
	public String getValueAsString() {
		//TODO use a configured name for each data source
		return dbUrl;
	}

	@Override
	public InputParameterDataSourceWidget createWrappingWidget() {
		return new InputParameterSQLIteratorWidget(this);
	}


}
