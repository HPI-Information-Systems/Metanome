package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;

import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;
import de.uni_potsdam.hpi.metanome.frontend.client.jarchooser.JarChooser;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionServiceAsync;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FinderService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FinderServiceAsync;
import de.uni_potsdam.hpi.metanome.frontend.client.widgets.ParameterTable;

/**
 * Superclass for all algorithm specific tabs on the page.
 * Includes common functionality such as adding a JarChooser or ParameterTable
 */
public abstract class AlgorithmTab extends DockPanel{
	protected BasePage basePage;
	protected ParameterTable parameterTable;
	protected JarChooser jarChooser;
	
	protected FinderServiceAsync finderService;
	protected ExecutionServiceAsync executionService;
	
	protected AsyncCallback<String[]> addJarChooserCallback;
	
	/**
	 * Constructor. Initializes FinderService
	 */
	public AlgorithmTab(BasePage basePage){
		//TODO: style in CSS
		this.setWidth("100%");
		this.setHeight("100px");
		
		this.basePage = basePage;
		
		this.finderService = GWT.create(FinderService.class);
		this.executionService = GWT.create(ExecutionService.class);
		
		addJarChooserCallback = new AsyncCallback<String[]>() {
		      public void onFailure(Throwable caught) {
		        // TODO: Do something with errors.
		    	  caught.printStackTrace();
		      }

		      public void onSuccess(String[] result) { 
		    	  addJarChooser(result);
		      }
		    };
	}
		
	/**
	 * Adds a widget for user's parameter input to the tab 
	 * 
	 * @param paramList	list of required parameters
	 */
	public void addParameterTable(List<InputParameter> paramList){
		if (parameterTable != null) {
			this.remove(parameterTable);
		}
		parameterTable = new ParameterTable(paramList);
		this.add(parameterTable, DockPanel.WEST);
	}

	/**
	 * 
	 * @return	the <link>ParameterTable</link> object of this tab
	 */
	public ParameterTable getParameterTable() {
		return parameterTable;
	}
	
	/**
	 * adds the JarChooser object for this tab.
	 * must be implemented in subclasses to use algorithm specific JarChooser
	 * 
	 * @param filenames	list of filenames (without path) of matching algorithms
	 */
	public void addJarChooser(String... filenames) {
		jarChooser = new JarChooser(filenames);
		this.add(jarChooser, DockPanel.NORTH);
	}
	
	/**
	 * 
	 * @return the <link>JarChooser</link> object of this tab
	 */
	public JarChooser getJarChooser() {
		return jarChooser;
	}
	
	protected String getCurrentlySelectedAlgorithm(){
		return this.jarChooser.getSelectedAlgorithm();
	}
	
	public void callExecutionService(List<InputParameter> parameters) {
		final String algorithmName = getCurrentlySelectedAlgorithm();

		// Switch to results page
		AsyncCallback<Void> callback = basePage.showResults(executionService, algorithmName);
		
		// Make the call to the execution service.
		executionService.executeAlgorithm(algorithmName, parameters, callback);
	}
		
}
