package de.uni_potsdam.hpi.metanome.algorithm_integration.input;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link CsvFileGenerator}
 */
public class CsvFileGeneratorTest {

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void testConstructor() {
		// Setup
		String expectedFilePath = "some/path";
		CsvFileGenerator generator = new CsvFileGenerator(expectedFilePath);
		
		// Check result
		assertEquals(expectedFilePath, generator.inputFilePath);
	}

}
