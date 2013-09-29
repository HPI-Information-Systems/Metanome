package de.uni_potsdam.hpi.metanome.frontend.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;

public interface ExecutionServiceAsync {

	public void executeInclusionDependencyAlgorithm(String algorithmName, List<InputParameter> parameters,
			AsyncCallback<Void> callback);

	public void executeFunctionalDependencyAlgorithm(String algorithmName, List<InputParameter> parameters,
			AsyncCallback<Void> callback);

	public void executeUniqueColumnCombinationsAlgorithm(String algorithmName, List<InputParameter> parameters,
			AsyncCallback<Void> callback);

	public void executeBasicStatisticsAlgorithm(String algorithmName, List<InputParameter> parameters, 
			AsyncCallback<Void> callback);
}
