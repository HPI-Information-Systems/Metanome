package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;


public class InclusionDependencyTab extends AlgorithmTab {

	public InclusionDependencyTab() {
		super();
		getInclusionDependencyAlgorithms();
	}

	private void getInclusionDependencyAlgorithms() {
		finderService.listInclusionDependencyAlgorithms(addJarChooserCallback);
	}
	@Override
	public void addJarChooser(String... filenames) {
		jarChooser = new InclusionDependencyJarChooser(filenames);
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
		    	  //TODO results are displayed by the ResultReceiver through double dispatch
		      }
		    };

		// Make the call to the parameter service.
		executionService.executeInclusionDependencyAlgorithm(algorithmName, parameters, callback);
			
	}
}
