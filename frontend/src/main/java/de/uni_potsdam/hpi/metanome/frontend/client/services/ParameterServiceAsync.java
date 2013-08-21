package de.uni_potsdam.hpi.metanome.frontend.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_potsdam.hpi.metanome.frontend.client.InputParameter;

public interface ParameterServiceAsync {

	void retrieveInclusionDependencyParameters(String selectedValue, 
			AsyncCallback<List<InputParameter>> callback);

	void retrieveFunctionalDependencyParameters(String selectedValue,
			AsyncCallback<List<InputParameter>> callback);

	void retrieveUniqueColumnCombinationsParameters(String selectedValue,
			AsyncCallback<List<InputParameter>> callback);

}
