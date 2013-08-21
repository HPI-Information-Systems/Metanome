package de.uni_potsdam.hpi.metanome.frontend.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class InclusionDependencyJarChooser extends JarChooser {

	public InclusionDependencyJarChooser(String[] jarFilenames) {
		super(jarFilenames);
	}

	@Override
	protected void callParameterService(String selectedValue,
			AsyncCallback<List<InputParameter>> callback) {
	    parameterService.retrieveInclusionDependencyParameters(selectedValue, callback);
	}

}
