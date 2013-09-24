package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;

import de.uni_potsdam.hpi.metanome.frontend.client.FunctionalDependencyJarChooser;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;


public class FunctionalDependencyTab extends AlgorithmTab {
	
	/**
	 * Constructor.
	 */
	public FunctionalDependencyTab(){
		super();
		getFunctionalDependencyAlgorithms();
	}

	/**
	 * Service call to retrieve available FD algorithms
	 */
	private void getFunctionalDependencyAlgorithms() {
		finderService.listFunctionalDependencyAlgorithms(addJarChooserCallback);
	}

	@Override
	public void addJarChooser(String... filenames) {
		jarChooser = new FunctionalDependencyJarChooser(filenames);
		this.add(jarChooser, DockPanel.NORTH);
	}

	@Override
	public void callExecutionService(List<InputParameter> parameters) {
		String algorithmName = getCurrentlySelectedAlgorithm();

		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
		      public void onFailure(Throwable caught) {
		    	  // TODO: Do something with errors.
		      }

		      public void onSuccess(Void v) {  	  
		    	  //TODO: results are displayed by the ResultReceiver through double dispatch
		      }
		    };

		// Make the call to the parameter service.
		executionService.executeFunctionalDependencyAlgorithm(algorithmName, parameters, callback);
	}
}
