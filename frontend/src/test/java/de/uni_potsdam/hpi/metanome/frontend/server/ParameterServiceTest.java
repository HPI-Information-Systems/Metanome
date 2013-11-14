package de.uni_potsdam.hpi.metanome.frontend.server;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameter;
import junit.framework.TestCase;

public class ParameterServiceTest extends TestCase {
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void testRetrieveUniqueColumnCombinationsParameters() {
		//Setup
		ParameterServiceImpl parameterService = new ParameterServiceImpl();
		
		//Execute
		List<InputParameter> inputParameters = parameterService.retrieveParameters("example_ucc_algorithm-0.0.1-SNAPSHOT.jar");
		
		//Check
		assertNotNull(inputParameters);
		assertFalse(inputParameters.isEmpty());
		assertNotNull(inputParameters.get(0));
	}
}
