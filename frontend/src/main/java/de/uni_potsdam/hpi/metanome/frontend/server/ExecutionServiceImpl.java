package de.uni_potsdam.hpi.metanome.frontend.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.uni_potsdam.hpi.metanome.algorithm_execution.TempFileGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValueBoolean;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValueSQLInputGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValueRelationalInputGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValueString;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.SQLInputGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.InclusionDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_loading.AlgorithmExecutor;
import de.uni_potsdam.hpi.metanome.algorithm_loading.AlgorithmLoadingException;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterBoolean;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterCsvFile;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterSQLIterator;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterString;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionService;
import de.uni_potsdam.hpi.metanome.input.csv.CsvFileGenerator;
import de.uni_potsdam.hpi.metanome.input.sql.SqlIteratorGenerator;
import de.uni_potsdam.hpi.metanome.result_receiver.FunctionalDependencyPrinter;
import de.uni_potsdam.hpi.metanome.result_receiver.InclusionDependencyPrinter;
import de.uni_potsdam.hpi.metanome.result_receiver.UniqueColumnCombinationPrinter;

/**
 * Service Implementation for service that triggers algorithm execution
 */
public class ExecutionServiceImpl extends RemoteServiceServlet implements ExecutionService {
	
	private static final long serialVersionUID = -2758103927345131933L;
	
	/**
	 * TODO docs
	 * 
	 * @param algorithmName
	 * @return
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	protected AlgorithmExecutor buildExecutor(String algorithmName) throws FileNotFoundException, UnsupportedEncodingException {
		FunctionalDependencyResultReceiver fdResultReceiver = 
				new FunctionalDependencyPrinter(getResultFileName(algorithmName), getResultDirectoryName());
		InclusionDependencyResultReceiver indResultReceiver = 
				new InclusionDependencyPrinter(getResultFileName(algorithmName), getResultDirectoryName());
		UniqueColumnCombinationResultReceiver uccResultReceiver = 
				new UniqueColumnCombinationPrinter(getResultFileName(algorithmName), getResultDirectoryName());
		FileGenerator fileGenerator = new TempFileGenerator();
		
		return new AlgorithmExecutor(fdResultReceiver, indResultReceiver, uccResultReceiver, fileGenerator);
	}
	
	private List<ConfigurationValue> convertInputParameters(
			List<InputParameter> parameters) throws AlgorithmConfigurationException {
		List<ConfigurationValue> configValuesList = new LinkedList<ConfigurationValue>();
		for (InputParameter parameter : parameters){
			ConfigurationValue configValue = convertToConfigurationValue(parameter);
			configValuesList.add(configValue);
		}
		return configValuesList;
	}

	/**
	 * TODO docs
	 * 
	 * @param parameter
	 * @return
	 * @throws AlgorithmConfigurationException
	 */
	public ConfigurationValue convertToConfigurationValue(
			InputParameter parameter) throws AlgorithmConfigurationException {
		//TODO all types of ConfigurationValues
		if (parameter instanceof InputParameterString)
			return new ConfigurationValueString(parameter.getIdentifier(), 
					((InputParameterString) parameter).getValue());
		
		else if (parameter instanceof InputParameterBoolean)
			return new ConfigurationValueBoolean(parameter.getIdentifier(), 
					((InputParameterBoolean) parameter).getValue());
		
		else if (parameter instanceof InputParameterCsvFile)
			return new ConfigurationValueRelationalInputGenerator(parameter.getIdentifier(), 
					buildCsvFileGenerator((InputParameterCsvFile) parameter));
		
		else if (parameter instanceof InputParameterSQLIterator)
			return new ConfigurationValueSQLInputGenerator(parameter.getIdentifier(), 
					buildSQLInputGenerator((InputParameterSQLIterator) parameter));
		
		else
			return null;
	}

	/**
	 * TODO docs
	 * 
	 * @param parameter
	 * @return
	 * @throws AlgorithmConfigurationException
	 */
	private SQLInputGenerator buildSQLInputGenerator(
			InputParameterSQLIterator parameter) throws AlgorithmConfigurationException {
		return new SqlIteratorGenerator(parameter.getDbUrl(), parameter.getUserName(), parameter.getPassword());
	}

	/**
	 * TODO docs
	 * 
	 * @param param
	 * @return
	 * @throws AlgorithmConfigurationException
	 */
	protected CsvFileGenerator buildCsvFileGenerator(InputParameterCsvFile param) throws AlgorithmConfigurationException {
		//TODO advanced CsvFileGenerator construction
		try {
			if (param.isAdvanced())
				return new CsvFileGenerator(new File(param.getFileNameValue()), param.getSeparatorChar(), 
						param.getQuoteChar(), param.getEscapeChar(), param.getLine(), 
						param.isStrictQuotes(), param.isIgnoreLeadingWhiteSpace()) ;
			else
				return new CsvFileGenerator(new File(param.getFileNameValue()));
		} catch (FileNotFoundException e) {
			throw new AlgorithmConfigurationException("Could not find specified CSV file.");		
		}
	}	
	
	@Override
	public void executeAlgorithm(String algorithmName, List<InputParameter> parameters) throws AlgorithmConfigurationException, AlgorithmLoadingException, AlgorithmExecutionException {
		
		List<ConfigurationValue> configs = convertInputParameters(parameters);

		try {
			buildExecutor(algorithmName).executeAlgorithm(algorithmName, configs);
		} catch (FileNotFoundException e) {
			throw new AlgorithmExecutionException("Could not generate result file.");
		} catch (UnsupportedEncodingException e) {
			throw new AlgorithmExecutionException("Could not build temporary file generator.");
		}
	}
	
	protected String getResultDirectoryName() {
		return "results";
	}
	
	protected String getResultFileName(String algorithmName) {
		return algorithmName + new SimpleDateFormat("yyyy-MM-dd'T'HHmmss").format(new Date()) + ".txt";
	}


}
