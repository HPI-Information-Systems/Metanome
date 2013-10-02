package de.uni_potsdam.hpi.metanome.frontend.server;

import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValueString;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.InclusionDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_loading.AlgorithmExecuter;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterString;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionService;
import de.uni_potsdam.hpi.metanome.result_receiver.FunctionalDependencyFileWriter;
import de.uni_potsdam.hpi.metanome.result_receiver.InclusionDependencyFileWriter;
import de.uni_potsdam.hpi.metanome.result_receiver.UniqueColumnCombinationFileWriter;

/**
 * Service Implementation for service that triggers algorithm execution
 */
public class ExecutionServiceImpl extends RemoteServiceServlet implements
		ExecutionService {

	private static final long serialVersionUID = 1L;
	
	AlgorithmExecuter executer = new AlgorithmExecuter();
	
	private List<ConfigurationValue> convertInputParameters(
			List<InputParameter> parameters) {
		List<ConfigurationValue> configValuesList = new LinkedList<ConfigurationValue>();
		for (InputParameter parameter : parameters){
			ConfigurationValue configValue = convertToConfigurationValue(parameter);
			configValuesList.add(configValue);
		}
		return configValuesList;
	}


	private ConfigurationValue convertToConfigurationValue(
			InputParameter parameter) {
		//TODO different types of ConfigurationValues
		if (parameter instanceof InputParameterString)
			return new ConfigurationValueString(parameter.getIdentifier(), 
					(String) parameter.getValue());
		else
			return null;
	}
	

	@Override
	public void executeInclusionDependencyAlgorithm(String algorithmName,
			List<InputParameter> parameters) {
		List<ConfigurationValue> configs = convertInputParameters(parameters);
		InclusionDependencyResultReceiver resultReceiver = new InclusionDependencyFileWriter(
				getResultFileName(algorithmName));
		
		executer.executeInclusionDependencyAlgorithm(algorithmName, configs, resultReceiver);
		
	}

	@Override
	public void executeFunctionalDependencyAlgorithm(String algorithmName,
			List<InputParameter> parameters) {
		List<ConfigurationValue> configs = convertInputParameters(parameters);
		FunctionalDependencyResultReceiver resultReceiver = new FunctionalDependencyFileWriter(
				getResultFileName(algorithmName));
		
		executer.executeFunctionalDependencyAlgorithm(algorithmName, configs, resultReceiver);
		
	}


	protected String getResultFileName(String algorithmName) {
		return "results/" + algorithmName + DateFormat.getInstance().format(new Date()) + ".txt";
	}

	@Override
	public void executeUniqueColumnCombinationsAlgorithm(String algorithmName,
			List<InputParameter> parameters) {
		List<ConfigurationValue> configs = convertInputParameters(parameters);
		UniqueColumnCombinationResultReceiver resultReceiver = new UniqueColumnCombinationFileWriter(
				getResultFileName(algorithmName));
		
		executer.executeUniqueColumnCombinationsAlgorithm(algorithmName, configs, resultReceiver);
	}


	@Override
	public void executeBasicStatisticsAlgorithm(String algorithmName,
			List<InputParameter> parameters) {
		List<ConfigurationValue> configs = convertInputParameters(parameters);
		FunctionalDependencyResultReceiver resultReceiver = null; //TODO instantiate
		
		//TODO implement execution logic executer.executeBasicStatisticsAlgorithm(algorithmName, configs, resultReceiver);
				
	}
	


}
