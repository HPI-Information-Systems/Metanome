package de.uni_potsdam.hpi.metanome.example_fd_algorithm;

import java.util.ArrayList;
import java.util.List;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.FunctionalDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.SQLInputGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.SimpleRelationalInputGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;

public class ExampleAlgorithm implements FunctionalDependencyAlgorithm {

	protected String path = null;
	protected FunctionalDependencyResultReceiver resultReceiver;

	public List<ConfigurationSpecification> getConfigurationRequirements() {
		List <ConfigurationSpecification> configurationSpecification = new ArrayList<ConfigurationSpecification>();
		
		configurationSpecification.add(new ConfigurationSpecificationString("pathToOutputFile"));
		configurationSpecification.add(new ConfigurationSpecificationCsvFile("input file"));
		
		return configurationSpecification;
	}

	public void execute() throws CouldNotReceiveResultException {
		if (path != null) {
			resultReceiver.receiveResult(
					new ColumnCombination(
							new ColumnIdentifier("table1", "column1"), 
							new ColumnIdentifier("table1", "column2")),
							new ColumnIdentifier("table1", "column5"));		
		}
	}

	public void setResultReceiver(FunctionalDependencyResultReceiver resultReceiver) {
		this.resultReceiver = resultReceiver;
	}

	public void setConfigurationValue(String identifier, String value) {
		if (identifier.equals("pathToOutputFile")) {
			path = value;
		}		
	}

	public void setConfigurationValue(String identifier, boolean value) {
		throw new UnsupportedOperationException();		
	}

	public void setConfigurationValue(String identifier, SimpleRelationalInputGenerator value) {
		throw new UnsupportedOperationException();	
	}

	public void setConfigurationValue(String identifier, SQLInputGenerator value) {
		throw new UnsupportedOperationException();				
	}

}
