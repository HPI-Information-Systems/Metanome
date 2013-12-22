package de.uni_potsdam.hpi.metanome.frontend.client.runs;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.DockPanel;

import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;
import de.uni_potsdam.hpi.metanome.frontend.client.jarchooser.JarChooser;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionServiceAsync;
import de.uni_potsdam.hpi.metanome.frontend.client.widgets.ParameterTable;

/**
 * Superclass for all algorithm specific tabs on the page.
 * Includes common functionality such as adding a JarChooser or ParameterTable
 */
public class AlgorithmTab extends DockPanel{
	protected BasePage basePage;
	protected ParameterTable parameterTable;
	protected JarChooser jarChooser;
	
	protected ExecutionServiceAsync executionService;
	
	
	/**
	 * Constructor. Initializes FinderService and adds all given algorithms
	 * 
	 * @param algorithmNames 
	 */
	public AlgorithmTab(BasePage basePage, String... algorithmNames){
		this.setWidth("100%");
		
		this.basePage = basePage;
		this.addJarChooser(algorithmNames);
		
		this.executionService = GWT.create(ExecutionService.class);
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
	 * Method to add more algorithms after construction.
	 * 
	 * @param algorithmNames
	 */
	public void addAlgorithms(String... algorithmNames){
//		try {
//			this.remove(jarChooser);
//		} catch (NullPointerException e) {
//			// There was no jarChooser yet, so just add one
//		}
		
		this.jarChooser.addAlgorithms(algorithmNames);
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
	
	public String getCurrentlySelectedAlgorithm(){
		return this.jarChooser.getSelectedAlgorithm();
	}
	
	public void selectAlgorithm(String algorithmName) {
		this.jarChooser.setSelectedAlgorithm(algorithmName);
	}
	
	/**
	 * Execute the currently selected algorithm and switch to results page
	 * @param parameters
	 */
	public void callExecutionService(List<InputParameter> parameters) {
		final String algorithmName = getCurrentlySelectedAlgorithm();
		basePage.startExecutionAndResultPolling(executionService, algorithmName, parameters);
	}

}
