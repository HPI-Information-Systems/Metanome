package de.uni_potsdam.hpi.metanome.algorithms.algorithm_template;

import java.util.List;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.StringParameterAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.TempFileAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

public class AlgorithmTemplate implements UniqueColumnCombinationsAlgorithm, FunctionalDependencyAlgorithm, TempFileAlgorithm, StringParameterAlgorithm {

	@Override
	public List<ConfigurationSpecification> getConfigurationRequirements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void execute() throws AlgorithmExecutionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setConfigurationValue(String identifier, String... values)
			throws AlgorithmConfigurationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTempFileGenerator(FileGenerator tempFileGenerator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setResultReceiver(
			FunctionalDependencyResultReceiver resultReceiver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setResultReceiver(
			UniqueColumnCombinationResultReceiver resultReceiver) {
		// TODO Auto-generated method stub
		
	}
	
}
