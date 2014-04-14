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

package de.uni_potsdam.hpi.metanome.frontend.client.runs;


import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterDataSource;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ParameterService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ParameterServiceAsync;

/**
 * A UI Widget that allows to choose a JAR containing the algorithm to use
 */
public class JarChooser extends HorizontalPanel {

	protected Label label;
	protected ListBox listbox;
		
	protected ParameterServiceAsync parameterService;
	
	/**
	 * Constructor. 
	 * 
	 * @param jarFilenames
	 * @param algorithmSubclass
	 */
	public JarChooser(String[] jarFilenames){
		super();
		this.parameterService = GWT.create(ParameterService.class);
		
		this.label = new Label("Choose your algorithm:");
		this.add(label);
		
		this.listbox = new ListBox();
		
		//unselectable default entry
		this.listbox.addItem("--");
		this.listbox.getElement().getFirstChildElement().setAttribute("disabled", "disabled");
		this.listbox.setSelectedIndex(0);
		
		this.addAlgorithms(jarFilenames);
		this.add(listbox);
		this.listbox.addChangeHandler(new JarChooserChangeHandler());
	}

	/**
	 * Specifies the action undertaken when a jar file is chosen.
	 */
	public void submit(){
		String selectedValue = getSelectedAlgorithm();
		
		AsyncCallback<List<InputParameter>> callback = new AsyncCallback<List<InputParameter>>() {
		      public void onFailure(Throwable caught) {
		        // TODO: Do something with errors.
		      }

		      public void onSuccess(List<InputParameter> result) {  	  
		    	  forwardParameters(result);
		      }
		    };

		    // Make the call to the parameter service.
		    callParameterService(selectedValue, callback);
	}

	/**
	 * Calls the service to retrieve parameters to be specified by the user and display 
	 * corresponding widget
	 * 
	 * @param selectedValue	the name of the selected algorithm
	 * @param callback		callback object for RPC
	 */
	public void callParameterService(String selectedValue, AsyncCallback<List<InputParameter>> callback) {
	    parameterService.retrieveParameters(selectedValue, callback);
	}

	/**
	 * Handles the incoming list of parameters by adding a ParameterTable to the corresponding
	 * tab.
	 * @param paramList list of parameters necessary for the chosen algorithm
	 */
	protected void forwardParameters(List<InputParameter> paramList) {
		((RunConfigurationPage) this.getParent()).addParameterTable(paramList);
	}
	
	/**
	 * 
	 * @return the number of items in the listbox, that is, the number of available
	 * algorithms in this JarChooser
	 */
	public int getListItemCount() {
		return this.listbox.getItemCount();
	}

	/**
	 * 
	 * @return the value at the currently selected index
	 */
	public String getSelectedAlgorithm() {
		return listbox.getValue(listbox.getSelectedIndex());
	}
	
	/**
	 * Select the entry with the given value. 
	 * 
	 * @throws IndexOutOfBoundsException	if none of the entries have the given value.
	 * @param algorithmName	value to select
	 */
	public void setSelectedAlgorithm(String algorithmName) {
		for (int i=0; i<listbox.getItemCount(); i++){
			if(listbox.getValue(i).equals(algorithmName)){
				this.listbox.setSelectedIndex(i);
				return;
			}
		}
		
		throw new IndexOutOfBoundsException("The value " + algorithmName + " is not available in this jarChooser");
	}

	/**
	 * Add more entries.
	 * 
	 * @param algorithmNames	array of entries to add
	 */
	public void addAlgorithms(String[] algorithmNames) {
		for (String filename : algorithmNames){
			this.listbox.addItem(filename);
		}		
	}

	/**
	 * Filters the list of algorithms so that only those are displayed that would accept the given data source
	 * 
	 * @param dataSource	the data source that shall be profiled / for which algorithms should be filtered
	 */
	public void filterForPrimaryDataSource(InputParameterDataSource dataSource) {
		this.listbox.setSelectedIndex(0);
		// TODO filter out any algorithms that would not accept the given data source
		System.out.println("Filtering algorithms for a data source is not yet implemented");

	}
	
	
}
