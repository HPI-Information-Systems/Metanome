package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_potsdam.hpi.metanome.frontend.client.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.JarChooser;

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
