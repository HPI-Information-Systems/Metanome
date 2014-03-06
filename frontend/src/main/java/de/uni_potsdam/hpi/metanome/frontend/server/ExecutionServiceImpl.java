/*
 * Copyright 2014 by the Metanome project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uni_potsdam.hpi.metanome.frontend.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.uni_potsdam.hpi.metanome.algorithm_execution.ProgressCache;
import de.uni_potsdam.hpi.metanome.algorithm_execution.TempFileGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.SQLInputGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.Result;
import de.uni_potsdam.hpi.metanome.algorithm_loading.AlgorithmExecutor;
import de.uni_potsdam.hpi.metanome.algorithm_loading.AlgorithmLoadingException;
import de.uni_potsdam.hpi.metanome.configuration.ConfigurationValue;
import de.uni_potsdam.hpi.metanome.configuration.ConfigurationValueBoolean;
import de.uni_potsdam.hpi.metanome.configuration.ConfigurationValueRelationalInputGenerator;
import de.uni_potsdam.hpi.metanome.configuration.ConfigurationValueSQLInputGenerator;
import de.uni_potsdam.hpi.metanome.configuration.ConfigurationValueString;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterBoolean;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterCsvFile;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterDataSource;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterSQLIterator;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterString;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionService;
import de.uni_potsdam.hpi.metanome.input.csv.CsvFileGenerator;
import de.uni_potsdam.hpi.metanome.input.sql.SqlIteratorGenerator;
import de.uni_potsdam.hpi.metanome.result_receiver.ResultPrinter;
import de.uni_potsdam.hpi.metanome.result_receiver.ResultsCache;
import de.uni_potsdam.hpi.metanome.result_receiver.ResultsHub;

/**
 * Service Implementation for service that triggers algorithm execution
 */
public class ExecutionServiceImpl extends RemoteServiceServlet implements ExecutionService {
	
	private static final long serialVersionUID = -2758103927345131933L;
	
	protected HashMap<String, ResultsCache> currentResultReceiver = new HashMap<String, ResultsCache>();
	protected HashMap<String, ProgressCache> currentProgressCaches = new HashMap<String, ProgressCache>();
	
	/**
	 * Builds an {@link AlgorithmExecutor} with stacked {@link OmniscientResultReceiver}s to write result files and 
	 * cache results for the frontend.
	 * 
	 * @param algorithmName
	 * @param executionIdentifier
	 * 
	 * @return an {@link AlgorithmExecutor}
	 * 
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	protected AlgorithmExecutor buildExecutor(String algorithmName, String executionIdentifier) throws FileNotFoundException, UnsupportedEncodingException {		
		ResultPrinter resultPrinter = new ResultPrinter(executionIdentifier, "results");
		ResultsCache resultsCache = new ResultsCache();
		ResultsHub resultsHub = new ResultsHub();
		resultsHub.addSubscriber(resultPrinter);
		resultsHub.addSubscriber(resultsCache);
		
		FileGenerator fileGenerator = new TempFileGenerator();
		
		ProgressCache progressCache = new ProgressCache();
		
		AlgorithmExecutor executor = new AlgorithmExecutor(resultsHub, progressCache, fileGenerator);
		currentResultReceiver.put(executionIdentifier, resultsCache);
		currentProgressCaches.put(executionIdentifier, progressCache);
		return executor;
	}
	
	/**
	 * Converts given parameters and data sources to configuration values, the format used in backend and algorithms.
	 * 
	 * @param parameters	the list of "standard" InputParameters to be converted
	 * @param dataSources	the list of "data source" InputParameters to be converted
	 * @return	a joint list of ConfigurationValues containing "standard" as well as "data source" parameters
	 * @throws AlgorithmConfigurationException
	 */
	private List<ConfigurationValue> convertInputParameters(
			List<InputParameter> parameters, List<InputParameterDataSource> dataSources) 
					throws AlgorithmConfigurationException {
		List<ConfigurationValue> configValuesList = new LinkedList<ConfigurationValue>();
		
		for (InputParameter parameter : parameters){
			configValuesList.add(convertToConfigurationValue(parameter));
		}
		for (InputParameter parameter : dataSources){
			configValuesList.add(convertToConfigurationValue(parameter));
		}
		
		return configValuesList;
	}

	/**
	 * Finds the ConfigurationValue class that corresponds to the given InputParameter and creates an instance
	 * of it with the same parameter values.
	 * 
	 * @param parameter the InputParameter to be converted
	 * @return ConfigurationValue instance representing the same information as parameter
	 * @throws AlgorithmConfigurationException
	 */
	public ConfigurationValue convertToConfigurationValue(
			InputParameter parameter) throws AlgorithmConfigurationException {
		if (parameter instanceof InputParameterString)
			return new ConfigurationValueString(parameter.getIdentifier(), 
					((InputParameterString) parameter).getValues());
		
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
			throw new AlgorithmConfigurationException("The InputParameter cannot be converted to a ConfigurationValue.");
	}

	/**
	 * Creates a SqlInputGenerator from the corresponding InputParameter.
	 * 
	 * @param parameter	user's input for the database connection parameters
	 * @return SqlIteratorGenerator instance with the values from parameter
	 * @throws AlgorithmConfigurationException
	 */
	private SQLInputGenerator buildSQLInputGenerator(
			InputParameterSQLIterator parameter) throws AlgorithmConfigurationException {
		return new SqlIteratorGenerator(parameter.getDbUrl(), parameter.getUserName(), parameter.getPassword());
	}

	/**
	 * Creates a CsvFileGenerator from the corresponding InputParameter. Uses default separators etc. unless
	 * the param is set to advanced configuration.
	 * 
	 * @param param	user's input for the csv file parameters
	 * @return CsvFileGenerator instance with the values from param
	 * 
	 * @throws AlgorithmConfigurationException
	 */
	protected CsvFileGenerator buildCsvFileGenerator(InputParameterCsvFile param) throws AlgorithmConfigurationException {
		try {
			if (param.isAdvanced())
				return new CsvFileGenerator(new File(param.getFileNameValue()), param.getSeparatorChar(), 
						param.getQuoteChar(), param.getEscapeChar(), param.getLine(), 
						param.isStrictQuotes(), param.isIgnoreLeadingWhiteSpace()) ;
			else
				return new CsvFileGenerator(new File(param.getFileNameValue()));
		} catch (FileNotFoundException e) {
			throw new AlgorithmConfigurationException("Error opening specified CSV file.");		
		}
	}	
	
	@Override
	public long executeAlgorithm(String algorithmName, String executionIdentifier, List<InputParameter> parameters, 
			List<InputParameterDataSource> dataSources) 
			throws AlgorithmConfigurationException, AlgorithmLoadingException, AlgorithmExecutionException {
		List<ConfigurationValue> configs = convertInputParameters(parameters, dataSources);
		AlgorithmExecutor executor = null;
		
		try {
			executor = buildExecutor(algorithmName, executionIdentifier);
		} catch (FileNotFoundException e) {
			throw new AlgorithmExecutionException("Could not generate result file.");
		} catch (UnsupportedEncodingException e) {
			throw new AlgorithmExecutionException("Could not build temporary file generator.");
		}
		long executionTime = executor.executeAlgorithm(algorithmName, configs);
		try {
			executor.close();
		} catch (IOException e) {
			throw new AlgorithmExecutionException("Could not close algorithm executor.");
		}
		
		return executionTime;
	}
	
	@Override
	public ArrayList<Result> fetchNewResults(String executionIdentifier){	
		// FIXME return exception when algorithm name is not in map
		return currentResultReceiver.get(executionIdentifier).getNewResults();
	}

	@Override
	public float fetchProgress(String executionIdentifier) {
		// FIXME return exception when algorithm name is not in map
		return currentProgressCaches.get(executionIdentifier).getProgress();
	}
}
