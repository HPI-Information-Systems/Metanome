package de.uni_potsdam.hpi.metanome.frontend.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;

public class UniqueColumnCombinationsJarChooser extends JarChooser {

	public UniqueColumnCombinationsJarChooser(String[] jarFilenames) {
		super(jarFilenames);
	}

	@Override
	protected void callParameterService(String selectedValue,
			AsyncCallback<List<InputParameter>> callback) {
	    parameterService.retrieveUniqueColumnCombinationsParameters(selectedValue, callback);
	}

}
