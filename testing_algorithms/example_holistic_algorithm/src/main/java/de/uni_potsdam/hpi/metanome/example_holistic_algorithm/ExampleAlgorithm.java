package de.uni_potsdam.hpi.metanome.example_holistic_algorithm;

import java.util.ArrayList;
import java.util.List;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.StringParameterAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.FunctionalDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.UniqueColumnCombination;

public class ExampleAlgorithm implements FunctionalDependencyAlgorithm, UniqueColumnCombinationsAlgorithm, StringParameterAlgorithm {

	protected String path = null;
	protected FunctionalDependencyResultReceiver fdResultReceiver;
	protected UniqueColumnCombinationResultReceiver uccResultReceiver;

	@Override
	public List<ConfigurationSpecification> getConfigurationRequirements() {
		List<ConfigurationSpecification> configurationSpecification = new ArrayList<ConfigurationSpecification>();

		configurationSpecification.add(new ConfigurationSpecificationString(
				"pathToOutputFile"));

		return configurationSpecification;
	}

	@Override
	public void execute() throws CouldNotReceiveResultException {
		if (path != null) {
			fdResultReceiver.receiveResult(
					new FunctionalDependency(
							new ColumnCombination(
								new ColumnIdentifier("table1", "column1"),
								new ColumnIdentifier("table1", "column2")),
							new ColumnIdentifier("table1", "column5")));
			uccResultReceiver.receiveResult(new UniqueColumnCombination(
					new ColumnIdentifier("table1", "column5"),
					new ColumnIdentifier("table1", "column6")));
		}
	}

	@Override
	public void setResultReceiver(
			FunctionalDependencyResultReceiver resultReceiver) {
		this.fdResultReceiver = resultReceiver;
	}

	@Override
	public void setResultReceiver(
			UniqueColumnCombinationResultReceiver resultReceiver) {
		this.uccResultReceiver = resultReceiver;

	}

	@Override
	public void setConfigurationValue(String identifier, String... values) throws AlgorithmConfigurationException {
		if ((identifier.equals("pathToOutputFile")) && (values.length == 1)) {
			path = values[0];
		} else {
			throw new AlgorithmConfigurationException("Incorrect identifier or value list length.");
		}
	}

}
