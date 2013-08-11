package de.uni_potsdam.hpi.metanome.frontend.client;


import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

import de.uni_potsdam.hpi.metanome.frontend.client.services.ParameterService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ParameterServiceAsync;
import de.uni_potsdam.hpi.metanome.frontend.client.tabs.AlgorithmTab;

/**
 * A UI Widget that allows to choose a JAR containing the algorithm to use
 */
public class JarChooser extends HorizontalPanel {

	private Label label;
	private ListBox listbox;
	private Button button;
	
	private String algorithmSubclass;
	
	private ParameterServiceAsync parameterService;
	
	/**
	 * Constructor. 
	 * 
	 * @param jarFilenames
	 * @param algorithmSubclass
	 */
	public JarChooser(String[] jarFilenames, String algorithmSubclass){
		super();
		this.algorithmSubclass = algorithmSubclass;
		this.parameterService = GWT.create(ParameterService.class);
		
		this.label = new Label("Choose your algorithm:");
		this.add(label);
		
		this.listbox = new ListBox();
		for (String filename : jarFilenames){
			this.listbox.addItem(filename);
		}
		this.add(listbox);
		
		this.button = new Button("OK", new JarChooserClickHandler());
		this.add(button);
	}

	/**
	 * specifies the action undertaken when a jar file is chosen
	 */
	public void submit(){
		String selectedValue = listbox.getValue(listbox.getSelectedIndex());
		
		AsyncCallback<List<InputParameter>> callback = new AsyncCallback<List<InputParameter>>() {
		      public void onFailure(Throwable caught) {
		        // TODO: Do something with errors.
		      }

		      public void onSuccess(List<InputParameter> result) {  	  
		    	  forwardParameters(result);
		      }
		    };

		    // Make the call to the parameter service.
		    parameterService.retrieveParameters(algorithmSubclass, selectedValue, callback);

	}

	private void forwardParameters(List<InputParameter> paramList) {
		((AlgorithmTab) this.getParent()).addParameterTable(paramList);
	}
	
	public int getListItemCount() {
		return this.listbox.getItemCount();
	}
}
