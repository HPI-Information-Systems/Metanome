package de.uni_potsdam.hpi.metanome.example_algorithm;

import java.util.ArrayList;
import java.util.List;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

public class ExampleAlgorithm extends UniqueColumnCombinationsAlgorithm {

	protected String path = null;

	public List<ConfigurationSpecification> getConfigurationRequirements() {
		List <ConfigurationSpecification> configurationSpecification = new ArrayList<ConfigurationSpecification>();
		
		configurationSpecification.add(new ConfigurationSpecificationString("pathToInputFile"));
		
		return configurationSpecification;
	}

	@Override
	public void setConfigurationValue(String identifier, String value) {
		if (identifier.equals("pathToInputFile")) {
			path = value;
		}
	}
	
	@Override
	public void start(UniqueColumnCombinationResultReceiver resultReceiver) {
		if (path != null) {
			resultReceiver.receiveResult(
					new ColumnCombination(
							new ColumnIdentifier("table1", "column1"), 
							new ColumnIdentifier("table2", "column2")));			
		}
	}

}
