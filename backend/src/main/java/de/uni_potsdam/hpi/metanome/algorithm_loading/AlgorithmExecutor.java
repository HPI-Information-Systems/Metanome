package de.uni_potsdam.hpi.metanome.algorithm_loading;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uni_potsdam.hpi.metanome.algorithm_execution.ProgressCache;
import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.InclusionDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.ProgressEstimatingAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.TempFileAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.configuration.ConfigurationValue;
import de.uni_potsdam.hpi.metanome.result_receiver.CloseableOmniscientResultReceiver;

public class AlgorithmExecutor implements Closeable {

	protected CloseableOmniscientResultReceiver resultReceiver;
	protected ProgressCache progressCache;
	
	protected FileGenerator fileGenerator;
	
	/**
	 * Constructs a new executor with new result receivers and generators.
	 * 
	 * @param resultReceiver
	 * @param fileGenerator
	 */
	public AlgorithmExecutor(
			CloseableOmniscientResultReceiver resultReceiver,
			ProgressCache progressCache,
			FileGenerator fileGenerator) {
		this.resultReceiver = resultReceiver;
		this.progressCache = progressCache;
		
		this.fileGenerator = fileGenerator;
	}
	
	/**
	 * Executes an algorithm. The algorithm is loaded from the jar, 
	 * configured and all receivers and generators are set before execution.
	 * The elapsed time while executing the algorithm in nano seconds is 
	 * returned as long.
	 * 
	 * @param algorithmName
	 * @param configs
	 * @return elapsed time in ns
	 * 
	 * @throws AlgorithmLoadingException
	 * @throws AlgorithmConfigurationException
	 * @throws AlgorithmExecutionException
	 */
	public long executeAlgorithm(String algorithmName, List<ConfigurationValue> configs) throws AlgorithmLoadingException, AlgorithmConfigurationException, AlgorithmExecutionException {
		AlgorithmJarLoader loader = new AlgorithmJarLoader();
		Algorithm algorithm;
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
		
		Set<Class<?>> interfaces = getInterfaces(algorithm);
		
		for (ConfigurationValue configValue : configs) {
			configValue.triggerSetValue(algorithm, interfaces);
		}	
		
		if (interfaces.contains(FunctionalDependencyAlgorithm.class)) {
			FunctionalDependencyAlgorithm fdAlgorithm = (FunctionalDependencyAlgorithm) algorithm;
			fdAlgorithm.setResultReceiver(resultReceiver);
		}
		
		if (interfaces.contains(InclusionDependencyAlgorithm.class)) {
			InclusionDependencyAlgorithm indAlgorithm = (InclusionDependencyAlgorithm) algorithm;
			indAlgorithm.setResultReceiver(resultReceiver);
		}
		
		if (interfaces.contains(UniqueColumnCombinationsAlgorithm.class)) {
			UniqueColumnCombinationsAlgorithm uccAlgorithm = (UniqueColumnCombinationsAlgorithm) algorithm;
			uccAlgorithm.setResultReceiver(resultReceiver);
		}
		
		if (interfaces.contains(TempFileAlgorithm.class)) {
			TempFileAlgorithm tempFileAlgorithm = (TempFileAlgorithm) algorithm;
			tempFileAlgorithm.setTempFileGenerator(fileGenerator);
		}
		
		if (interfaces.contains(ProgressEstimatingAlgorithm.class)) {
			ProgressEstimatingAlgorithm progressEstimatingAlgorithm = (ProgressEstimatingAlgorithm) algorithm;
			progressEstimatingAlgorithm.setProgressReceiver(progressCache);
		}
				
		long before = System.nanoTime();
		algorithm.execute();
		long after = System.nanoTime();
		
		return after - before;
	}

	protected Set<Class<?>> getInterfaces(Object object) {
		Set<Class<?>> interfaces = new HashSet<Class<?>>();
		Collections.addAll(interfaces, object.getClass().getInterfaces());
		return interfaces;
	}

	@Override
	public void close() throws IOException {
		resultReceiver.close();
	}
}
