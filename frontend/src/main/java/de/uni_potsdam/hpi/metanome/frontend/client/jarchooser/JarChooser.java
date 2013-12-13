package de.uni_potsdam.hpi.metanome.frontend.client.jarchooser;


import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.runs.AlgorithmTab;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ParameterService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ParameterServiceAsync;

/**
 * A UI Widget that allows to choose a JAR containing the algorithm to use
 */
public class JarChooser extends HorizontalPanel {

	private Label label;
	private ListBox listbox;
		
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
		
		for (String filename : jarFilenames){
			this.listbox.addItem(filename);
		}
		this.add(listbox);
		this.listbox.addChangeHandler(new JarChooserChangeHandler());
	}

	/**
	 * specifies the action undertaken when a jar file is chosen
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
	private void forwardParameters(List<InputParameter> paramList) {
		((AlgorithmTab) this.getParent()).addParameterTable(paramList);
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
	 * TODO: docs
	 * @return
	 */
	public String getSelectedAlgorithm() {
		return listbox.getValue(listbox.getSelectedIndex());
	}
	
	
}
