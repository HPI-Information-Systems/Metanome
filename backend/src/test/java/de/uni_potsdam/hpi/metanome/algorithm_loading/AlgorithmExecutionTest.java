package de.uni_potsdam.hpi.metanome.algorithm_loading;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValueString;
import de.uni_potsdam.hpi.metanome.result_receiver.UniqueColumnCombinationPrinter;

public class AlgorithmExecutionTest {

	@Test
	public void executeUniqueColumnCombinationsAlgorithmTest() {
		// Setup
		AlgorithmExecuter executer = new AlgorithmExecuter();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		List<ConfigurationValue> configs = new ArrayList<ConfigurationValue>();
		configs.add(new ConfigurationValueString("pathToInputFile", "blub"));

		// Expected values
		ColumnCombination columnCombination = new ColumnCombination(
				new ColumnIdentifier("table1", "column1"), 
				new ColumnIdentifier("table2", "column2"));
				
		// Execute
		executer.executeUniqueColumnCombinationsAlgorithm("example_algorithm-0.0.1-SNAPSHOT-jar-with-dependencies.jar", configs, 
				new UniqueColumnCombinationPrinter(new PrintStream(outStream)));
		
		// Check result
		assertTrue(outStream.toString().contains(columnCombination.toString()));
	}
	
	@Test
	public void executeFunctionalDependencyAlgorithmTest() {
		// Setup
		AlgorithmExecuter executer = new AlgorithmExecuter();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		List<ConfigurationValue> configs = new ArrayList<ConfigurationValue>();
		configs.add(new ConfigurationValueString("pathToInputFile", "blub"));

		// Expected values
		ColumnCombination columnCombination = new ColumnCombination(
				new ColumnIdentifier("table1", "column1"), 
				new ColumnIdentifier("table2", "column2"));
				
		// Execute TODO
//		executer.executeFunctionalDependencyAlgorithm("example_algorithm-0.0.1-SNAPSHOT-jar-with-dependencies.jar", configs, 
//				null);
		
		// Check result
		//assertTrue(outStream.toString().contains(columnCombination.toString()));
	}
	
	@Test
	public void executeInclusionDependencyTest() {
		// Setup
		AlgorithmExecuter executer = new AlgorithmExecuter();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		List<ConfigurationValue> configs = new ArrayList<ConfigurationValue>();
		configs.add(new ConfigurationValueString("pathToInputFile", "blub"));
				
		// Execute
		// TODO executer.executeInclusionDependencyAlgorithm("example_algorithm-0.0.1-SNAPSHOT-jar-with-dependencies.jar", configs, 
				//null);
		
		// Check result
		
	}
}
