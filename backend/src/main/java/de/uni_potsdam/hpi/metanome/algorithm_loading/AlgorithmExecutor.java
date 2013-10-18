package de.uni_potsdam.hpi.metanome.algorithm_loading;

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
	 */
	public void executeInclusionDependencyAlgorithm(String algorithmName,
			List<ConfigurationValue> configs, InclusionDependencyResultReceiver resultReceiver) {
		AlgorithmJarLoader<InclusionDependencyAlgorithm> loader = 
				new AlgorithmJarLoader<InclusionDependencyAlgorithm>(InclusionDependencyAlgorithm.class);
		InclusionDependencyAlgorithm algorithm = null;
		
		try {
			algorithm = loader.loadAlgorithm(algorithmName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		algorithm.configure(configs);
		algorithm.start(resultReceiver);		
	}

	/**
	 * 
	 * @param algorithmName
	 * @param configs
	 * @param resultReceiver
	 */
	public void executeFunctionalDependencyAlgorithm(String algorithmName,
			List<ConfigurationValue> configs, FunctionalDependencyResultReceiver resultReceiver) {
		AlgorithmJarLoader<FunctionalDependencyAlgorithm> loader = 
				new AlgorithmJarLoader<FunctionalDependencyAlgorithm>(FunctionalDependencyAlgorithm.class);
		FunctionalDependencyAlgorithm algorithm = null;
		
		try {
			algorithm = loader.loadAlgorithm(algorithmName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		algorithm.configure(configs);
		algorithm.start(resultReceiver);
	}

	/**
	 * 
	 * @param algorithmName
	 * @param configs
	 * @param resultReceiver
	 */
	public void executeUniqueColumnCombinationsAlgorithm(String algorithmName,
			List<ConfigurationValue> configs, UniqueColumnCombinationResultReceiver resultReceiver) {
		AlgorithmJarLoader<UniqueColumnCombinationsAlgorithm> loader = 
				new AlgorithmJarLoader<UniqueColumnCombinationsAlgorithm>(UniqueColumnCombinationsAlgorithm.class);
		UniqueColumnCombinationsAlgorithm algorithm = null;
		
		try {
			algorithm = loader.loadAlgorithm(algorithmName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		algorithm.configure(configs);
		algorithm.start(resultReceiver);
	}
}
