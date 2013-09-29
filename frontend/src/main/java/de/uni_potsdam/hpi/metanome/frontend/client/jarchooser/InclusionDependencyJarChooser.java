package de.uni_potsdam.hpi.metanome.frontend.client.jarchooser;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;

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
