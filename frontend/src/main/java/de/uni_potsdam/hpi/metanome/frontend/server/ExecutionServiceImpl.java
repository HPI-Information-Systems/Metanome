package de.uni_potsdam.hpi.metanome.frontend.server;

import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.uni_potsdam.hpi.metanome.algorithm_loading.AlgorithmExecuter;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionService;

/**
 * Service Implementation for service that triggers algorithm execution
 */
public class ExecutionServiceImpl extends RemoteServiceServlet implements
		ExecutionService {

	private static final long serialVersionUID = 1L;
	
	AlgorithmExecuter executer = new AlgorithmExecuter();
	
	private List<ConfigurationValue> convertInputParameters(
			List<InputParameter> parameters) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public void executeInclusionDependencyAlgorithm(String algorithmName,
			List<InputParameter> parameters) {
		List<ConfigurationValue> configs = convertInputParameters(parameters);
		executer.executeInclusionDependencyAlgorithm(algorithmName, configs);
		// TODO retrieve and forward the result
		
	}

	@Override
	public void executeFunctionalDependencyAlgorithm(String algorithmName,
			List<InputParameter> parameters) {
		List<ConfigurationValue> configs = convertInputParameters(parameters);
		executer.executeFunctionalDependencyAlgorithm(algorithmName, configs);
		// TODO retrieve and forward the result
		
	}

	@Override
	public void executeUniqueColumnCombinationsAlgorithm(String algorithmName,
			List<InputParameter> parameters) {
		List<ConfigurationValue> configs = convertInputParameters(parameters);
		executer.executeUniqueColumnCombinationsAlgorithm(algorithmName, configs);
		// TODO retrieve and forward the result
		
	}
	


}
