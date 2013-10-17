package de.uni_potsdam.hpi.metanome.example_algorithm;

import java.util.ArrayList;
import java.util.List;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.CsvFileGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

public class ExampleAlgorithm extends UniqueColumnCombinationsAlgorithm {

	protected String path = null;

	public List<ConfigurationSpecification> getConfigurationRequirements() {
		List <ConfigurationSpecification> configurationSpecification = new ArrayList<ConfigurationSpecification>();
		
		configurationSpecification.add(new ConfigurationSpecificationString("pathToInputFile"));
		configurationSpecification.add(new ConfigurationSpecificationCsvFile("input file"));
		
		return configurationSpecification;
	}

	
	@Override
	public void start(UniqueColumnCombinationResultReceiver resultReceiver) {
		if (path != null) {
			try {
				resultReceiver.receiveResult(
						new ColumnCombination(
								new ColumnIdentifier("table1", "column1"), 
								new ColumnIdentifier("table2", "column2")));
			} catch (CouldNotReceiveResultException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}

	@Override
	public void setConfigurationValue(String identifier, String value) {
		if (identifier.equals("pathToInputFile")) {
			path = value;
		}
	}

	@Override
	public void setConfigurationValue(String identifier, boolean value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setConfigurationValue(String identifier, CsvFileGenerator value) {
		// TODO Auto-generated method stub
		
	}

}
