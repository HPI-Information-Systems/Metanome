package de.uni_potsdam.hpi.metanome.example_ind_algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.InclusionDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.RelationalInputParameterAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.StringParameterAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.TempFileAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationString;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.RelationalInputGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.InclusionDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.InclusionDependency;

public class ExampleAlgorithm implements InclusionDependencyAlgorithm, TempFileAlgorithm, StringParameterAlgorithm, RelationalInputParameterAlgorithm {

	protected String tableName = null;
	protected InclusionDependencyResultReceiver resultReceiver;
	protected FileGenerator tempFileGenerator;
	protected boolean relationalInputsSet = false;

	@Override
	public List<ConfigurationSpecification> getConfigurationRequirements() {
		List <ConfigurationSpecification> configurationSpecification = new ArrayList<ConfigurationSpecification>();
		
		configurationSpecification.add(new ConfigurationSpecificationCsvFile("input file"));
		configurationSpecification.add(new ConfigurationSpecificationString("tableName"));
		
		return configurationSpecification;
	}

	@Override
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
		
		if ((tableName != null) && relationalInputsSet) {
			resultReceiver.receiveResult(
					new InclusionDependency(
						new ColumnCombination(
								new ColumnIdentifier(tableName1, "column1"), 
								new ColumnIdentifier("table1", "column2")),
						new ColumnCombination(
								new ColumnIdentifier("table2", "column3"),
								new ColumnIdentifier("table2", "column2"))));		
		}
	}

	@Override
	public void setResultReceiver(InclusionDependencyResultReceiver resultReceiver) {
		this.resultReceiver = resultReceiver;
	}

	@Override
	public void setConfigurationValue(String identifier, String... values) throws AlgorithmConfigurationException {
		if ((identifier.equals("tableName")) && (values.length == 1)) {
			tableName = values[0];
		} else {
			throw new AlgorithmConfigurationException("Incorrect identifier or value list length.");
		}
	}

	@Override
	public void setTempFileGenerator(FileGenerator tempFileGenerator) {
		this.tempFileGenerator = tempFileGenerator;
	}
	
	@Override
	public void setConfigurationValue(String identifier, RelationalInputGenerator... values) throws AlgorithmConfigurationException{
		if ((identifier.equals("input file")) && (values.length == 2)){
			System.out.println("Input file is not being set on algorithm.");
			relationalInputsSet = true;
		} else {
			throw new AlgorithmConfigurationException("Incorrect configuration.");
		}
	}
}
