package de.uni_potsdam.hpi.metanome.algorithm_integration.input;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.input.SimpleRelationalInput;

/**
 * Tests for {@link CsvFileGenerator}
 */
public class CsvFileGeneratorTest {

	protected CsvFileFixture csvFileFixture;
	protected String expectedFilePath;
	protected CsvFileGenerator generator;
	
	@Before
	public void setUp() throws Exception {
		this.csvFileFixture = new CsvFileFixture();
		this.expectedFilePath = csvFileFixture.getTestDataPath("test.csv");
		this.generator = new CsvFileGenerator(expectedFilePath);
	}

	@After
	public void tearDown() throws Exception {
		
	}
	
	/**
	 * The generator should store the file path correctly.
	 */
	@Test
	public void testConstructor() {		
		// Check result
		assertEquals(expectedFilePath, generator.inputFilePath);
	}
	
	/**
	 * The generator should generate fresh csv files iterable from the start.
	 * 
	 * @throws IOException 
	 */
	@Test
	public void testGenerateNewCsvFile() throws IOException {
		// Setup
		SimpleRelationalInput csv = generator.generateNewCsvFile();
		
		// Check result
		// The csv should contain both lines and iterate through them with next.
		assertEquals(csvFileFixture.expectedFirstLine(), csv.next());
		assertEquals(csvFileFixture.expectedSecondLine(), csv.next());
		// A new CsvFile should iterate from the start.
		SimpleRelationalInput csv2 = generator.generateNewCsvFile();
		assertEquals(csvFileFixture.expectedFirstLine(), csv2.next());
		assertEquals(csvFileFixture.expectedSecondLine(), csv2.next());		
	}

}
