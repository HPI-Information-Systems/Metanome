package de.uni_potsdam.hpi.metanome.frontend.server;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValue;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValueBoolean;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValueRelationalInputGenerator;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationValueString;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterBoolean;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterCsvFile;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterSQLIterator;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterString;

public class ExecutionServiceTest extends TestCase {
	
	ExecutionServiceImpl executionService = new ExecutionServiceImpl();
	InputParameterString stringParam = new InputParameterString("test");
	InputParameterBoolean boolParam = new InputParameterBoolean("boolean");
	InputParameterCsvFile csvParam = new InputParameterCsvFile("inputFile");
	InputParameterSQLIterator sqlParam = new InputParameterSQLIterator("db connection");
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	
	/**
	 * Test the conversion of InputParameters (frontend) to ConfigurationValues (backend)
	 * 
	 * @throws FileNotFoundException
	 * @throws AlgorithmConfigurationException 
	 */
	@Test
	public void testConvertToInputParameter() throws AlgorithmConfigurationException {
		//Setup
		csvParam.setFileNameValue(Thread.currentThread().getContextClassLoader().getResource("inputData").getPath() + "/inputA.csv");
		
		//Execute
		ConfigurationValue confString = executionService.convertToConfigurationValue(stringParam);
		ConfigurationValue confBool = executionService.convertToConfigurationValue(boolParam);
		ConfigurationValue confCsv = executionService.convertToConfigurationValue(csvParam);
		
		//Check
		assertTrue(confString instanceof ConfigurationValueString);
		assertTrue(confBool instanceof ConfigurationValueBoolean);
		assertTrue(confCsv instanceof ConfigurationValueRelationalInputGenerator);
	}	

	/**
	 * Make sure an exception is thrown when trying to build a CsvFileGenerator on an invalid path
	 */
	@Test
	public void testBuildCsvFileGenerator() {		
		//Setup
		csvParam.setFileNameValue("some/file/path");
		
		//Execute
		try {
			executionService.buildCsvFileGenerator(csvParam);
			fail("Expected ConfigurationException due to unavailable file was not thrown.");
		} catch (AlgorithmConfigurationException e) {
			//Test successful.
		}

	}
	
	/**
	 * TODO docs
	 */
	@Test
	public void testBuildSqlIteratorGenerator() {
		try{
			executionService.convertToConfigurationValue(sqlParam);
			fail("Expected ConfigurationException due to failed DB connection was not thrown.");
		} catch(AlgorithmConfigurationException e) {
			//Test successful.
		}
		
		//TODO: Test whether correct parameter values are given (using mock)

	}
	
	/**
	 * Test method for {@link ExecutionServiceImpl#fetchProgress(String)}
	 * 
	 * When fetching the current progress for an execution the correct progress should be returned.
	 * 
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testFetchProgress() throws FileNotFoundException, UnsupportedEncodingException {
		// Setup
		// Expected values
		String expectedExecutionIdentifier = "executionIdentifier";
		executionService.buildExecutor("someAlgo", expectedExecutionIdentifier);
		float expectedProgress = 0.42f;
		
		// Execute functionality
		executionService.currentProgressCaches.get(expectedExecutionIdentifier).updateProgress(expectedProgress);
		float actualProgress = executionService.fetchProgress(expectedExecutionIdentifier);
		
		// Check result
		assertEquals(expectedProgress, actualProgress);
	}
	
}
