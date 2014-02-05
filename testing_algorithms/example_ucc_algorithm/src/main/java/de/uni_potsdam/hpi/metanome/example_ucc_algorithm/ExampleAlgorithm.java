package de.uni_potsdam.hpi.metanome.example_ucc_algorithm;

import java.util.ArrayList;
import java.util.List;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution.ProgressReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.ProgressEstimatingAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.RelationalInputParameterAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.StringParameterAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.UniqueColumnCombination;

public class ExampleAlgorithm implements UniqueColumnCombinationsAlgorithm,
		StringParameterAlgorithm, RelationalInputParameterAlgorithm,
		ProgressEstimatingAlgorithm {

	protected String path1, path2 = null;
	protected UniqueColumnCombinationResultReceiver resultReceiver;
	protected ProgressReceiver progressReceiver;

	@Override
	public List<ConfigurationSpecification> getConfigurationRequirements() {
		List<ConfigurationSpecification> configurationSpecification = new ArrayList<ConfigurationSpecification>();

		configurationSpecification.add(new ConfigurationSpecificationString(
				"pathToInputFile", 2));
		configurationSpecification.add(new ConfigurationSpecificationCsvFile(
				"input file"));

		return configurationSpecification;
	}

	@Override
	public void execute() {
		if ((path1 != null) && (path2 != null)) {
			System.out.println("UCC Algorithm executing");
			try {
				resultReceiver.receiveResult(new UniqueColumnCombination(
						new ColumnIdentifier("table1", "column1"),
						new ColumnIdentifier("table2", "column2")));
			} catch (CouldNotReceiveResultException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			for (int i = 0; i < 10; i++) {
				Thread.sleep(500);
				progressReceiver.updateProgress(1f/(10 - i));
			}
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		
		
	}

	@Override
	public void setResultReceiver(
			UniqueColumnCombinationResultReceiver resultReceiver) {
		this.resultReceiver = resultReceiver;
	}

	@Override
	public void setConfigurationValue(String identifier, String... values) throws AlgorithmConfigurationException {
		System.out.println("setting value for " + identifier);
		if ((identifier.equals("pathToInputFile")) && (values.length == 2)) {
			path1 = values[0];
			path2 = values[1];
		} else {
			throw new AlgorithmConfigurationException("Incorrect identifier or value list length.");
		}
	}

	@Override
	public void setConfigurationValue(String identifier,
			RelationalInputGenerator... values)
			throws AlgorithmConfigurationException {
		if (identifier.equals("input file")) {
			System.out.println("Input file is not being set on algorithm.");
		}
	}

	@Override
	public void setProgressReceiver(ProgressReceiver progressReceiver) {
		this.progressReceiver = progressReceiver;
	}
}
