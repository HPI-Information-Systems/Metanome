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
 * InputParameters correspond to a ConfigurationSpecification and ConfigurationValue type. 
 * It is used for frontend input of the configuration value, so generally, a ConfigurationSpecification
 * will be converted to an InputParameter, which is used to get the user's value input, and then converted
 * to the ConfigurationValue handed back to the algorithm.
 * 
 * @author Claudia
 *
 */
public abstract class InputParameterDataSource extends InputParameter {
	private static final long serialVersionUID = 8344875059579539858L;

	/**
	 * The identifier is used to maintain the matching between ConfigurationSpecification
	 * and ConfigurationValue while retrieving the values.
	 */
	private String identifier;	
	
	public InputParameterDataSource(){}
	
	public InputParameterDataSource(String identifier){
		this.setIdentifier(identifier);
	}
	
	// **** GETTERS & SETTERS ****
	
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public abstract String getValueAsString();

	/**
	 * Every subclass must implement this and return a Widget which
	 * can retrieve the user input for its value.
	 * 
	 * @return a Widget suited for input of the specific subclass
	 */
	public abstract InputParameterDataSourceWidget createWrappingWidget();

}
