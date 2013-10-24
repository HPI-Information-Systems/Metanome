package de.uni_potsdam.hpi.metanome.frontend.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValueBoolean;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValueSimpleRelationalInputGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValueString;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.InclusionDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_loading.AlgorithmExecutor;
import de.uni_potsdam.hpi.metanome.algorithm_loading.AlgorithmLoadingException;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterBoolean;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterCsvFile;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterString;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionService;
import de.uni_potsdam.hpi.metanome.input.csv.CsvFileGenerator;
import de.uni_potsdam.hpi.metanome.result_receiver.FunctionalDependencyFileWriter;
import de.uni_potsdam.hpi.metanome.result_receiver.InclusionDependencyFileWriter;
import de.uni_potsdam.hpi.metanome.result_receiver.UniqueColumnCombinationFileWriter;

/**
 * Service Implementation for service that triggers algorithm execution
 */
public class ExecutionServiceImpl extends RemoteServiceServlet implements
		ExecutionService {

	private static final long serialVersionUID = 1L;
	
	AlgorithmExecutor executer = new AlgorithmExecutor();
	
	private List<ConfigurationValue> convertInputParameters(
			List<InputParameter> parameters) throws FileNotFoundException {
		List<ConfigurationValue> configValuesList = new LinkedList<ConfigurationValue>();
		for (InputParameter parameter : parameters){
			ConfigurationValue configValue = convertToConfigurationValue(parameter);
			configValuesList.add(configValue);
		}
		return configValuesList;
	}

	public ConfigurationValue convertToConfigurationValue(
			InputParameter parameter) throws FileNotFoundException {
		//TODO all types of ConfigurationValues
		if (parameter instanceof InputParameterString)
			return new ConfigurationValueString(parameter.getIdentifier(), 
					(String) parameter.getValue());
		else if (parameter instanceof InputParameterBoolean)
			return new ConfigurationValueBoolean(parameter.getIdentifier(), 
					(Boolean) parameter.getValue());
		else if (parameter instanceof InputParameterCsvFile)
			return new ConfigurationValueSimpleRelationalInputGenerator(parameter.getIdentifier(), 
					buildCsvFileGenerator((InputParameterCsvFile) parameter));
		return null;
	}

	protected CsvFileGenerator buildCsvFileGenerator(InputParameterCsvFile parameter) throws FileNotFoundException {
		//TODO instantiate CsvFileGenerator correctly
		if (parameter.isAdvanced())
			return null;//new CsvFileGenerator(file, ' ', ' ', ' ', ' ', false, false) ;
		else 
			return new CsvFileGenerator(new File(parameter.getValue()));
	}	
	
	@Override
	public void executeInclusionDependencyAlgorithm(String algorithmName,
			List<InputParameter> parameters) throws AlgorithmConfigurationException, AlgorithmLoadingException, IOException, AlgorithmExecutionException {
		
		List<ConfigurationValue> configs = convertInputParameters(parameters);
		InclusionDependencyResultReceiver resultReceiver = new InclusionDependencyFileWriter(
				getResultFileName(algorithmName), getResultDirectoryName());
		
		executer.executeInclusionDependencyAlgorithm(algorithmName, configs, resultReceiver);
	}

	@Override
	public void executeFunctionalDependencyAlgorithm(String algorithmName,
			List<InputParameter> parameters) throws AlgorithmConfigurationException, AlgorithmLoadingException, IOException, AlgorithmExecutionException {
		
		List<ConfigurationValue> configs = convertInputParameters(parameters);
		FunctionalDependencyResultReceiver resultReceiver = new FunctionalDependencyFileWriter(
				getResultFileName(algorithmName), getResultDirectoryName());
		
		executer.executeFunctionalDependencyAlgorithm(algorithmName, configs, resultReceiver);		
	}


	@Override
	public void executeUniqueColumnCombinationsAlgorithm(String algorithmName,
			List<InputParameter> parameters) throws AlgorithmLoadingException, AlgorithmConfigurationException, IOException, AlgorithmExecutionException {
		
		List<ConfigurationValue> configs = convertInputParameters(parameters);
		UniqueColumnCombinationResultReceiver resultReceiver = new UniqueColumnCombinationFileWriter(
				getResultFileName(algorithmName), getResultDirectoryName());
		
		executer.executeUniqueColumnCombinationsAlgorithm(algorithmName, configs, resultReceiver);
	}


	@Override
	public void executeBasicStatisticsAlgorithm(String algorithmName,
			List<InputParameter> parameters) {
		//List<ConfigurationValue> configs = convertInputParameters(parameters);
		FunctionalDependencyResultReceiver resultReceiver = null; //TODO instantiate
		
		//TODO implement execution logic executer.executeBasicStatisticsAlgorithm(algorithmName, configs, resultReceiver);
				
	}
	
	protected String getResultDirectoryName() {
		return "results";
	}
	
	protected String getResultFileName(String algorithmName) {
		return algorithmName + new SimpleDateFormat("yyyy-MM-dd'T'HHmmss").format(new Date()) + ".txt";
	}


}
