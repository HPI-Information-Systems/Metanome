package de.uni_potsdam.hpi.metanome.frontend.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;

public interface ParameterServiceAsync {

	public void retrieveInclusionDependencyParameters(String selectedValue, 
			AsyncCallback<List<InputParameter>> callback);

	public void retrieveFunctionalDependencyParameters(String selectedValue,
			AsyncCallback<List<InputParameter>> callback);

	public void retrieveUniqueColumnCombinationsParameters(String selectedValue,
			AsyncCallback<List<InputParameter>> callback);

	public void retrieveBasicStatisticsParameters(String selectedValue,
			AsyncCallback<List<InputParameter>> callback);
}
