package de.uni_potsdam.hpi.metanome.example_wrong_bootstrap_algorithm;

import java.util.LinkedList;
import java.util.List;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

public class ExampleAlgorithm implements UniqueColumnCombinationsAlgorithm {

	@Override
	public List<ConfigurationSpecification> getConfigurationRequirements() {
		return new LinkedList<ConfigurationSpecification>();
	}

	@Override
	public void execute() throws AlgorithmExecutionException {
		
	}

	@Override
	public void setResultReceiver(UniqueColumnCombinationResultReceiver resultReceiver) {
		
	}

}
