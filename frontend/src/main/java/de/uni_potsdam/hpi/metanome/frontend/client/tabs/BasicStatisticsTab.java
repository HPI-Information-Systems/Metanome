package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockPanel;

import de.uni_potsdam.hpi.metanome.frontend.client.jarchooser.BasicStatisticsJarChooser;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;


public class BasicStatisticsTab extends AlgorithmTab {
	
	/**
	 * Constructor.
	 */
	public BasicStatisticsTab(){
		super();
		getBasicStatisticsAlgorithms();
	}

	/**
	 * Service call to retrieve available FD algorithms
	 */
	private void getBasicStatisticsAlgorithms() {
		finderService.listBasicStatisticsAlgorithms(addJarChooserCallback);
	}

	@Override
	public void addJarChooser(String... filenames) {
		jarChooser = new BasicStatisticsJarChooser(filenames);
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
		    	  //TODO: results are displayed by the ResultReceiver somehow
		      }
		    };

		// Make the call to the execution service.
		executionService.executeBasicStatisticsAlgorithm(algorithmName, parameters, callback);
	}
}
