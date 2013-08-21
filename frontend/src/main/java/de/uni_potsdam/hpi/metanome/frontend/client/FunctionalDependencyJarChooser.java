package de.uni_potsdam.hpi.metanome.frontend.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;


public class FunctionalDependencyJarChooser extends JarChooser {

	public FunctionalDependencyJarChooser(String[] jarFilenames) {
		super(jarFilenames);
	}
	
	public void callParameterService(String selectedValue, AsyncCallback<List<InputParameter>> callback) {
	    parameterService.retrieveFunctionalDependencyParameters(selectedValue, callback);
	}

}
