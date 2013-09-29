package de.uni_potsdam.hpi.metanome.frontend.client.jarchooser;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;


public class BasicStatisticsJarChooser extends JarChooser {

	public BasicStatisticsJarChooser(String[] jarFilenames) {
		super(jarFilenames);
	}
	
	public void callParameterService(String selectedValue, AsyncCallback<List<InputParameter>> callback) {
	    //TODO parameterService.retrieveBasicStatisticsParameters(selectedValue, callback);
	}

}
