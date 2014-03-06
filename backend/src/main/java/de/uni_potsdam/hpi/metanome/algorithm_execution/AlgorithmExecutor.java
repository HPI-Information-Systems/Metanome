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

package de.uni_potsdam.hpi.metanome.algorithm_execution;

import de.uni_potsdam.hpi.metanome.algorithm_integration.Algorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.*;
import de.uni_potsdam.hpi.metanome.algorithm_loading.AlgorithmJarLoader;
import de.uni_potsdam.hpi.metanome.algorithm_loading.AlgorithmLoadingException;
import de.uni_potsdam.hpi.metanome.configuration.ConfigurationValue;
import de.uni_potsdam.hpi.metanome.result_receiver.CloseableOmniscientResultReceiver;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AlgorithmExecutor implements Closeable {

	protected CloseableOmniscientResultReceiver resultReceiver;
	protected ProgressCache progressCache;
	
	protected FileGenerator fileGenerator;
	
	/**
	 * Constructs a new executor with new result receivers and generators.
	 *
     * @param resultReceiver receives all of the algorithms results
     * @param fileGenerator generates temp files
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
     * @param algorithmFileName the algorithm's file name
     * @param configs list of configuration values
     * @return elapsed time in ns
     *
     * @throws de.uni_potsdam.hpi.metanome.algorithm_loading.AlgorithmLoadingException
     * @throws AlgorithmConfigurationException
     * @throws AlgorithmExecutionException
	 */
    public long executeAlgorithm(String algorithmFileName, List<ConfigurationValue> configs) throws AlgorithmLoadingException, AlgorithmExecutionException {
        AlgorithmJarLoader loader = new AlgorithmJarLoader();
		Algorithm algorithm;
		try {
            algorithm = loader.loadAlgorithm(algorithmFileName);
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
