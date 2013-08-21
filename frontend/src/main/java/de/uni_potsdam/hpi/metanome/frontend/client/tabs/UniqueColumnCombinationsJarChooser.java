package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_potsdam.hpi.metanome.frontend.client.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.JarChooser;

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
