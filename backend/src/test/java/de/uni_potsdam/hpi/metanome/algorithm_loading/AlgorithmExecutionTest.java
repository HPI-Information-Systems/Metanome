package de.uni_potsdam.hpi.metanome.algorithm_loading;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValueString;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.InclusionDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;
import de.uni_potsdam.hpi.metanome.result_receiver.FunctionalDependencyFileWriter;
import de.uni_potsdam.hpi.metanome.result_receiver.InclusionDependencyFileWriter;
import de.uni_potsdam.hpi.metanome.result_receiver.UniqueColumnCombinationFileWriter;

public class AlgorithmExecutionTest {
	
	private AlgorithmExecuter executer;
	
	@Before
	public void setUp(){
		executer = new AlgorithmExecuter();
	}

	@Test
	public void executeUniqueColumnCombinationsAlgorithmTest() {
		// Setup
		List<ConfigurationValue> configs = new ArrayList<ConfigurationValue>();
		configs.add(new ConfigurationValueString("pathToInputFile", "blub"));
		UniqueColumnCombinationResultReceiver resultReceiver = mock(UniqueColumnCombinationFileWriter.class);
				
		// Execute
		executer.executeUniqueColumnCombinationsAlgorithm("example_algorithm-0.0.1-SNAPSHOT-jar-with-dependencies.jar", configs, 
				resultReceiver);
		
		// Check result
		verify(resultReceiver).receiveResult(isA(ColumnCombination.class));
	}
	
	@Test
	public void executeFunctionalDependencyAlgorithmTest() {
		// Setup
		List<ConfigurationValue> configs = new ArrayList<ConfigurationValue>();
		configs.add(new ConfigurationValueString("pathToOutputFile", "blub"));
		FunctionalDependencyResultReceiver resultReceiver = mock(FunctionalDependencyFileWriter.class);
				
		// Execute TODO
		executer.executeFunctionalDependencyAlgorithm("example_fd_algorithm_0.0.1.jar", configs, 
				resultReceiver);
		
		// Check result
		verify(resultReceiver).receiveResult(isA(ColumnCombination.class), isA(ColumnIdentifier.class));
	}
	
	@Test
	public void executeInclusionDependencyTest() {
		// Setup
		List<ConfigurationValue> configs = new ArrayList<ConfigurationValue>();
		configs.add(new ConfigurationValueString("tableName", "blub"));
		InclusionDependencyResultReceiver resultReceiver = mock(InclusionDependencyFileWriter.class);		
		
		// Execute
		executer.executeInclusionDependencyAlgorithm("example_ind_algorithm-0.0.1-SNAPSHOT-jar-with-dependencies.jar", configs, 
				resultReceiver);
		
		// Check result
		verify(resultReceiver).receiveResult(isA(ColumnCombination.class), isA(ColumnCombination.class));

	}
}
