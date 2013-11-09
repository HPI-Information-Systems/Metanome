package de.uni_potsdam.hpi.metanome.example_ind_algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.InclusionDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.TempFileAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.SimpleRelationalInputGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.InclusionDependencyResultReceiver;

public class ExampleAlgorithm implements InclusionDependencyAlgorithm, TempFileAlgorithm {

	protected String tableName = null;
	protected InclusionDependencyResultReceiver resultReceiver;
	protected FileGenerator tempFileGenerator;

	public List<ConfigurationSpecification> getConfigurationRequirements() {
		List <ConfigurationSpecification> configurationSpecification = new ArrayList<ConfigurationSpecification>();
		
		configurationSpecification.add(new ConfigurationSpecificationCsvFile("input file"));
		configurationSpecification.add(new ConfigurationSpecificationString("tableName"));
		
		return configurationSpecification;
	}

	public void execute() throws AlgorithmExecutionException {
		File tempFile = tempFileGenerator.getTemporaryFile();
		PrintWriter tempWriter;
		try {
			tempWriter = new PrintWriter(tempFile);
		} catch (FileNotFoundException e) {
			throw new AlgorithmExecutionException("File not found.");
		}
		tempWriter.write("table1");
		tempWriter.close();
		
		String tableName1;
		try {
			tableName1 = FileUtils.readFileToString(tempFile);
		} catch (IOException e) {
			throw new AlgorithmExecutionException("Could not read from file.");
		}
		
		if (tableName != null) {
			resultReceiver.receiveResult(
					new ColumnCombination(
							new ColumnIdentifier(tableName1, "column1"), 
							new ColumnIdentifier("table1", "column2")),
					new ColumnCombination(
							new ColumnIdentifier("table2", "column3"),
							new ColumnIdentifier("table2", "column2")));		
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

	@Override
	public void setTempFileGenerator(FileGenerator tempFileGenerator) {
		this.tempFileGenerator = tempFileGenerator;
	}
}
