package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;

import de.uni_potsdam.hpi.metanome.frontend.client.JarChooser;
import de.uni_potsdam.hpi.metanome.frontend.client.ParameterTable;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionServiceAsync;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FinderService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FinderServiceAsync;

/**
 * Superclass for all algorithm specific tabs on the page.
 * Includes common functionality such ass adding a JarChooser or ParameterTable
 */
public abstract class AlgorithmTab extends DockPanel{
	protected ParameterTable parameterTable;
	protected JarChooser jarChooser;
	
	protected FinderServiceAsync finderService;
	protected ExecutionServiceAsync executionService;
	
	protected AsyncCallback<String[]> addJarChooserCallback;
	
	/**
	 * Constructor. Initializes FinderService
	 */
	public AlgorithmTab(){
		//TODO: style in CSS
		this.setWidth("100%");
		this.setHeight("100px");
		
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
		try {
			this.remove(parameterTable);
		} catch (NullPointerException e) {
			//NullPointer is ok. It just means there was no parameterTable so far
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
	public abstract void addJarChooser(String... filenames);
	
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

	/**
	 * adds the JarChooser object for this tab.
	 * must be implemented in subclasses to use algorithm specific JarChooser
	 * 
	 * @param filenames	list of filenames (without path) of matching algorithms
	 */
	public abstract void callExecutionService(List<InputParameter> parameters);
}
