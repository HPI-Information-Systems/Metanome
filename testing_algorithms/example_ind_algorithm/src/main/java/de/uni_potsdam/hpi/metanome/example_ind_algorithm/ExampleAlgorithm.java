package de.uni_potsdam.hpi.metanome.example_ind_algorithm;

import java.util.ArrayList;
import java.util.List;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.InclusionDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.SimpleRelationalInputGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.InclusionDependencyResultReceiver;

public class ExampleAlgorithm implements InclusionDependencyAlgorithm {

	protected String tableName = null;
	protected InclusionDependencyResultReceiver resultReceiver;

	public List<ConfigurationSpecification> getConfigurationRequirements() {
		List <ConfigurationSpecification> configurationSpecification = new ArrayList<ConfigurationSpecification>();
		
		configurationSpecification.add(new ConfigurationSpecificationCsvFile("input file"));
		configurationSpecification.add(new ConfigurationSpecificationString("tableName"));
		
		return configurationSpecification;
	}

	public void execute() {
		if (tableName != null) {
			try {
				resultReceiver.receiveResult(
						new ColumnCombination(
								new ColumnIdentifier("table1", "column1"), 
								new ColumnIdentifier("table1", "column2")),
						new ColumnCombination(
								new ColumnIdentifier("table2", "column3"),
								new ColumnIdentifier("table2", "column2")));
			} catch (CouldNotReceiveResultException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
	}

	public void setResultReceiver(InclusionDependencyResultReceiver resultReceiver) {
		this.resultReceiver = resultReceiver;
	}

	public void setConfigurationValue(String identifier, String value) {
		if (identifier.equals("tableName")) {
			tableName = value;
		}		
	}

	public void setConfigurationValue(String identifier, boolean value) {
		throw new UnsupportedOperationException();		
	}

	public void setConfigurationValue(String identifier, SimpleRelationalInputGenerator value) {
		throw new UnsupportedOperationException();		
	}
}
