package de.uni_potsdam.hpi.metanome.algorithm_loading;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import de.uni_potsdam.hpi.metanome.algorithm_integration.FunctionalDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.InclusionDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.InclusionDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

public class AlgorithmExecutor {

	/**
	 * 
	 * @param algorithmName
	 * @param configs
	 * @param resultReceiver
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public void executeInclusionDependencyAlgorithm(String algorithmName,
			List<ConfigurationValue> configs, InclusionDependencyResultReceiver resultReceiver) throws IllegalArgumentException, SecurityException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		
		AlgorithmJarLoader<InclusionDependencyAlgorithm> loader = 
				new AlgorithmJarLoader<InclusionDependencyAlgorithm>(InclusionDependencyAlgorithm.class);
		InclusionDependencyAlgorithm algorithm = null;
		
		algorithm = loader.loadAlgorithm(algorithmName);
		
		for (ConfigurationValue configValue : configs) {
			configValue.triggerSetValue(algorithm);
		}
		algorithm.setResultReceiver(resultReceiver);
		algorithm.start();		
	}

	/**
	 * 
	 * @param algorithmName
	 * @param configs
	 * @param resultReceiver
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public void executeFunctionalDependencyAlgorithm(String algorithmName,
			List<ConfigurationValue> configs, FunctionalDependencyResultReceiver resultReceiver) throws IllegalArgumentException, SecurityException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		AlgorithmJarLoader<FunctionalDependencyAlgorithm> loader = 
				new AlgorithmJarLoader<FunctionalDependencyAlgorithm>(FunctionalDependencyAlgorithm.class);
		FunctionalDependencyAlgorithm algorithm = null;

		algorithm = loader.loadAlgorithm(algorithmName);
		
		for (ConfigurationValue configValue : configs) {
			configValue.triggerSetValue(algorithm);
		}
		algorithm.setResultReceiver(resultReceiver);
		algorithm.start();
	}

	/**
	 * 
	 * @param algorithmName
	 * @param configs
	 * @param resultReceiver
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 */
	public void executeUniqueColumnCombinationsAlgorithm(String algorithmName,
			List<ConfigurationValue> configs, UniqueColumnCombinationResultReceiver resultReceiver) throws IllegalArgumentException, SecurityException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		AlgorithmJarLoader<UniqueColumnCombinationsAlgorithm> loader = 
				new AlgorithmJarLoader<UniqueColumnCombinationsAlgorithm>(UniqueColumnCombinationsAlgorithm.class);
		UniqueColumnCombinationsAlgorithm algorithm = null;
		
		algorithm = loader.loadAlgorithm(algorithmName);
		
		for (ConfigurationValue configValue : configs) {
			configValue.triggerSetValue(algorithm);
		}
		algorithm.setResultReceiver(resultReceiver);
		algorithm.start();
	}
}
