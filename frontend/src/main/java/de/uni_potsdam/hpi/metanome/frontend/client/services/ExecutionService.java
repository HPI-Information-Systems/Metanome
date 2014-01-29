package de.uni_potsdam.hpi.metanome.frontend.client.services;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.Result;
import de.uni_potsdam.hpi.metanome.algorithm_loading.AlgorithmLoadingException;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterDataSource;

@RemoteServiceRelativePath("executionService")
public interface ExecutionService extends RemoteService {

	public long executeAlgorithm(String algorithmName, 
			String executionIdentifier, 
			List<InputParameter> parameters, 
			List<InputParameterDataSource> dataSources) 
					throws AlgorithmConfigurationException, AlgorithmLoadingException, AlgorithmExecutionException;

	public ArrayList<Result> fetchNewResults(String algorithmName);

	public float fetchProgress(String executionIdentifier);
}
