package de.uni_potsdam.hpi.metanome.algorithm_loading;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValueString;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.InclusionDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;
import de.uni_potsdam.hpi.metanome.result_receiver.FunctionalDependencyFileWriter;
import de.uni_potsdam.hpi.metanome.result_receiver.InclusionDependencyFileWriter;
import de.uni_potsdam.hpi.metanome.result_receiver.UniqueColumnCombinationFileWriter;

public class AlgorithmExecutorTest {
	
	private AlgorithmExecutor executer;
	
	@Before
	public void setUp(){
		executer = new AlgorithmExecutor();
	}

	/**
	 * TODO docs
	 * 
	 * @throws AlgorithmLoadingException 
	 * @throws AlgorithmConfigurationException 
	 * @throws CouldNotReceiveResultException 
	 * @throws AlgorithmExecutionException 
	 */
	@Test
	public void executeFunctionalDependencyAlgorithmTest() throws AlgorithmConfigurationException, AlgorithmLoadingException, CouldNotReceiveResultException, AlgorithmExecutionException {
		// Setup
		List<ConfigurationValue> configs = new ArrayList<ConfigurationValue>();
		configs.add(new ConfigurationValueString("pathToOutputFile", "path/to/file"));
		FunctionalDependencyResultReceiver resultReceiver = mock(FunctionalDependencyFileWriter.class);
				
		// Execute TODO
		executer.executeFunctionalDependencyAlgorithm("example_fd_algorithm-0.0.1-SNAPSHOT.jar", configs, 
				resultReceiver);
		
		// Check result
		verify(resultReceiver).receiveResult(isA(ColumnCombination.class), isA(ColumnIdentifier.class));
	}
	
	/**
	 * TODO docs
	 * 
	 * @throws AlgorithmConfigurationException
	 * @throws AlgorithmLoadingException
	 * @throws CouldNotReceiveResultException
	 * @throws AlgorithmExecutionException 
	 */
	@Test
	public void executeInclusionDependencyTest() throws AlgorithmConfigurationException, AlgorithmLoadingException, CouldNotReceiveResultException, AlgorithmExecutionException {
		// Setup
		List<ConfigurationValue> configs = new ArrayList<ConfigurationValue>();
		configs.add(new ConfigurationValueString("tableName", "table1"));
		InclusionDependencyResultReceiver resultReceiver = mock(InclusionDependencyFileWriter.class);		
		
		// Execute
		executer.executeInclusionDependencyAlgorithm("example_ind_algorithm-0.0.1-SNAPSHOT.jar", configs, 
				resultReceiver);
		
		// Check result
		verify(resultReceiver).receiveResult(isA(ColumnCombination.class), isA(ColumnCombination.class));
	}
	
	/**
	 * TODO docs
	 * 
	 * @throws AlgorithmConfigurationException 
	 * @throws AlgorithmLoadingException 
	 * @throws CouldNotReceiveResultException 
	 * @throws AlgorithmExecutionException 
	 */
	@Test
	public void executeUniqueColumnCombinationsAlgorithmTest() throws AlgorithmLoadingException, AlgorithmConfigurationException, CouldNotReceiveResultException, AlgorithmExecutionException {
		// Setup
		List<ConfigurationValue> configs = new ArrayList<ConfigurationValue>();
		configs.add(new ConfigurationValueString("pathToInputFile", "path/to/file"));
		UniqueColumnCombinationResultReceiver resultReceiver = mock(UniqueColumnCombinationFileWriter.class);
				
		// Execute
		executer.executeUniqueColumnCombinationsAlgorithm("example_ucc_algorithm-0.0.1-SNAPSHOT.jar", configs, 
				resultReceiver);
		
		// Check result
		verify(resultReceiver).receiveResult(isA(ColumnCombination.class));
	}
}
