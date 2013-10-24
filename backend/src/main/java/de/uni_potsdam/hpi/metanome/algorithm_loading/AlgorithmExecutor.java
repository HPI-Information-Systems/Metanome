package de.uni_potsdam.hpi.metanome.algorithm_loading;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.FunctionalDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.InclusionDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.InclusionDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

public class AlgorithmExecutor {

	/**
	 * @param algorithmName
	 * @param configs
	 * @param resultReceiver
	 * @throws AlgorithmConfigurationException
	 * @throws AlgorithmLoadingException 
	 * @throws AlgorithmExecutionException 
	 */
	public void executeInclusionDependencyAlgorithm(String algorithmName,
			List<ConfigurationValue> configs, InclusionDependencyResultReceiver resultReceiver) throws AlgorithmConfigurationException, AlgorithmLoadingException, AlgorithmExecutionException {
		
		AlgorithmJarLoader<InclusionDependencyAlgorithm> loader = 
				new AlgorithmJarLoader<InclusionDependencyAlgorithm>(InclusionDependencyAlgorithm.class);
		InclusionDependencyAlgorithm algorithm = null;
		
		try {
			algorithm = loader.loadAlgorithm(algorithmName);
		} catch (IllegalArgumentException e) {
			throw new AlgorithmLoadingException();
		} catch (SecurityException e) {
			throw new AlgorithmLoadingException();
		} catch (IOException e) {
			throw new AlgorithmLoadingException("IO Exception");
		} catch (ClassNotFoundException e) {
			throw new AlgorithmLoadingException("Class not found.");
		} catch (InstantiationException e) {
			throw new AlgorithmLoadingException("Could not instantiate.");
		} catch (IllegalAccessException e) {
			throw new AlgorithmLoadingException();
		} catch (InvocationTargetException e) {
			throw new AlgorithmLoadingException("Could not invoke.");
		} catch (NoSuchMethodException e) {
			throw new AlgorithmLoadingException("No such method.");
		}
		
		for (ConfigurationValue configValue : configs) {
			configValue.triggerSetValue(algorithm);
		}
		algorithm.setResultReceiver(resultReceiver);
		algorithm.execute();		
	}

	/**
	 * @param algorithmName
	 * @param configs
	 * @param resultReceiver
	 * @throws AlgorithmConfigurationException
	 * @throws AlgorithmLoadingException
	 * @throws AlgorithmExecutionException 
	 */
	public void executeFunctionalDependencyAlgorithm(String algorithmName,
			List<ConfigurationValue> configs, FunctionalDependencyResultReceiver resultReceiver) throws AlgorithmConfigurationException, AlgorithmLoadingException, AlgorithmExecutionException {
		AlgorithmJarLoader<FunctionalDependencyAlgorithm> loader = 
				new AlgorithmJarLoader<FunctionalDependencyAlgorithm>(FunctionalDependencyAlgorithm.class);
		FunctionalDependencyAlgorithm algorithm = null;

		try {
			algorithm = loader.loadAlgorithm(algorithmName);
		} catch (IllegalArgumentException e) {
			throw new AlgorithmLoadingException();
		} catch (SecurityException e) {
			throw new AlgorithmLoadingException();
		} catch (IOException e) {
			throw new AlgorithmLoadingException("IO Exception");
		} catch (ClassNotFoundException e) {
			throw new AlgorithmLoadingException("Class not found.");
		} catch (InstantiationException e) {
			throw new AlgorithmLoadingException("Could not instantiate.");
		} catch (IllegalAccessException e) {
			throw new AlgorithmLoadingException();
		} catch (InvocationTargetException e) {
			throw new AlgorithmLoadingException("Could not invoke.");
		} catch (NoSuchMethodException e) {
			throw new AlgorithmLoadingException("No such method.");
		}
		
		for (ConfigurationValue configValue : configs) {
			configValue.triggerSetValue(algorithm);
		}
		algorithm.setResultReceiver(resultReceiver);
		algorithm.execute();
	}

	/**
	 * @param algorithmName
	 * @param configs
	 * @param resultReceiver
	 * @throws AlgorithmLoadingException
	 * @throws AlgorithmConfigurationException
	 * @throws AlgorithmExecutionException 
	 */
	public void executeUniqueColumnCombinationsAlgorithm(String algorithmName,
			List<ConfigurationValue> configs, UniqueColumnCombinationResultReceiver resultReceiver) throws AlgorithmLoadingException, AlgorithmConfigurationException, AlgorithmExecutionException {
		AlgorithmJarLoader<UniqueColumnCombinationsAlgorithm> loader = 
				new AlgorithmJarLoader<UniqueColumnCombinationsAlgorithm>(UniqueColumnCombinationsAlgorithm.class);
		UniqueColumnCombinationsAlgorithm algorithm = null;
		
		try {
			algorithm = loader.loadAlgorithm(algorithmName);
		} catch (IllegalArgumentException e) {
			throw new AlgorithmLoadingException();
		} catch (SecurityException e) {
			throw new AlgorithmLoadingException();
		} catch (IOException e) {
			throw new AlgorithmLoadingException("IO Exception");
		} catch (ClassNotFoundException e) {
			throw new AlgorithmLoadingException("Class not found.");
		} catch (InstantiationException e) {
			throw new AlgorithmLoadingException("Could not instantiate.");
		} catch (IllegalAccessException e) {
			throw new AlgorithmLoadingException();
		} catch (InvocationTargetException e) {
			throw new AlgorithmLoadingException("Could not invoke.");
		} catch (NoSuchMethodException e) {
			throw new AlgorithmLoadingException("No such method.");
		}
		
		for (ConfigurationValue configValue : configs) {
			configValue.triggerSetValue(algorithm);
		}		
		algorithm.setResultReceiver(resultReceiver);
		algorithm.execute();
	}
}
