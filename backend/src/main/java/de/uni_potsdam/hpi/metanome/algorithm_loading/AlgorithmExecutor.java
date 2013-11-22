package de.uni_potsdam.hpi.metanome.algorithm_loading;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.InclusionDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.TempFileAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.InclusionDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;
import de.uni_potsdam.hpi.metanome.result_receiver.ResultPrinter;

public class AlgorithmExecutor {

	protected FunctionalDependencyResultReceiver fdResultReceiver;
	protected InclusionDependencyResultReceiver indResultReceiver;
	protected UniqueColumnCombinationResultReceiver uccResultReceiver;
	
	protected FileGenerator fileGenerator;
	
	/**
	 * Constructs a new executor with new result receivers and generators.
	 * 
	 * @param fdResultReceiver
	 * @param indResultReceiver
	 * @param uccResultReceiver
	 * @param fileGenerator
	 */
	public AlgorithmExecutor(
			FunctionalDependencyResultReceiver fdResultReceiver,
			InclusionDependencyResultReceiver indResultReceiver, 
			UniqueColumnCombinationResultReceiver uccResultReceiver, 
			FileGenerator fileGenerator) {
		this.fdResultReceiver = fdResultReceiver;
		this.indResultReceiver = indResultReceiver;
		this.uccResultReceiver = uccResultReceiver;
		
		this.fileGenerator = fileGenerator;
	}
	
	/**
	 * Executes an algorithm. The algorithm is loaded from the jar, 
	 * configured and all receivers and generators are set before execution.
	 * 
	 * @param algorithmName
	 * @param configs
	 * @throws AlgorithmLoadingException
	 * @throws AlgorithmConfigurationException
	 * @throws AlgorithmExecutionException
	 */
	public void executeAlgorithm(String algorithmName, List<ConfigurationValue> configs) throws AlgorithmLoadingException, AlgorithmConfigurationException, AlgorithmExecutionException {
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
			fdAlgorithm.setResultReceiver(fdResultReceiver);
		}
		
		if (interfaces.contains(InclusionDependencyAlgorithm.class)) {
			InclusionDependencyAlgorithm indAlgorithm = (InclusionDependencyAlgorithm) algorithm;
			indAlgorithm.setResultReceiver(indResultReceiver);
		}
		
		if (interfaces.contains(UniqueColumnCombinationsAlgorithm.class)) {
			UniqueColumnCombinationsAlgorithm uccAlgorithm = (UniqueColumnCombinationsAlgorithm) algorithm;
			uccAlgorithm.setResultReceiver(uccResultReceiver);
		}
		
		if (interfaces.contains(TempFileAlgorithm.class)) {
			TempFileAlgorithm tempFileAlgorithm = (TempFileAlgorithm) algorithm;
			tempFileAlgorithm.setTempFileGenerator(fileGenerator);
		}
				
		algorithm.execute();
		
		//FIXME
		//TODO: close opened result file
//		if(this.fdResultReceiver != null) {
//			try {
//				((ResultPrinter)this.fdResultReceiver).close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		else if(this.indResultReceiver != null) {
//			try {
//				((ResultPrinter)this.indResultReceiver).close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		else if(this.uccResultReceiver != null) {
//			try {
//				((ResultPrinter)this.uccResultReceiver).close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}

	protected Set<Class<?>> getInterfaces(Object object) {
		Set<Class<?>> interfaces = new HashSet<Class<?>>();
		Collections.addAll(interfaces, object.getClass().getInterfaces());
		return interfaces;
	}
}
