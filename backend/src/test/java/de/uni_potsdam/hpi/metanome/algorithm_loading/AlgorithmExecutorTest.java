package de.uni_potsdam.hpi.metanome.algorithm_loading;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_execution.TempFileGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmExecutionException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_execution.FileGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValueString;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.FunctionalDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.InclusionDependencyResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;

public class AlgorithmExecutorTest {
	
	protected FunctionalDependencyResultReceiver fdResultReceiver;
	protected InclusionDependencyResultReceiver indResultReceiver;
	protected UniqueColumnCombinationResultReceiver uccResultReceiver;
	protected FileGenerator fileGenerator;
	
	protected AlgorithmExecutor executer;
	
	@Before
	public void setUp() throws UnsupportedEncodingException{
		fdResultReceiver = mock(FunctionalDependencyResultReceiver.class);
		indResultReceiver = mock(InclusionDependencyResultReceiver.class);
		uccResultReceiver = mock(UniqueColumnCombinationResultReceiver.class);
		fileGenerator = new TempFileGenerator();
		
		executer = new AlgorithmExecutor(fdResultReceiver, indResultReceiver, uccResultReceiver, fileGenerator);
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
				
		// Execute TODO
		executer.executeAlgorithm("example_fd_algorithm-0.0.1-SNAPSHOT.jar", configs);
		
		// Check result
		verify(fdResultReceiver).receiveResult(isA(ColumnCombination.class), isA(ColumnIdentifier.class));
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
		
		// Execute
		executer.executeAlgorithm("example_ind_algorithm-0.0.1-SNAPSHOT.jar", configs);
		
		// Check result
		verify(indResultReceiver).receiveResult(isA(ColumnCombination.class), isA(ColumnCombination.class));
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
				
		// Execute
		executer.executeAlgorithm("example_ucc_algorithm-0.0.1-SNAPSHOT.jar", configs);
		
		// Check result
		verify(uccResultReceiver).receiveResult(isA(ColumnCombination.class));
	}
}
