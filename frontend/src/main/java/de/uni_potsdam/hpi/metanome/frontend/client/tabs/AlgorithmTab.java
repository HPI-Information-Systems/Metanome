package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;

import de.uni_potsdam.hpi.metanome.frontend.client.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.JarChooser;
import de.uni_potsdam.hpi.metanome.frontend.client.ParameterTable;
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
	protected AsyncCallback<String[]> addJarChooserCallback;
	
	public AlgorithmTab(){
		this.finderService = GWT.create(FinderService.class);
		addJarChooserCallback = new AsyncCallback<String[]>() {
		      public void onFailure(Throwable caught) {
		        // TODO: Do something with errors.
		      }

		      public void onSuccess(String[] result) {  	  
		    	  addJarChooser(result);
		      }
		    };
	}
		
	public void addParameterTable(List<InputParameter> paramList){
		parameterTable = new ParameterTable(paramList);
		this.add(parameterTable, DockPanel.WEST);
	}

	public ParameterTable getParameterTable() {
		return parameterTable;
	}
	
	public abstract void addJarChooser(String... filenames);
	
	public JarChooser getJarChooser() {
		return jarChooser;
	}
}
