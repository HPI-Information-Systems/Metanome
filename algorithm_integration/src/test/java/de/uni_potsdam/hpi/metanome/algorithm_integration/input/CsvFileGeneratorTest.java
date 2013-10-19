package de.uni_potsdam.hpi.metanome.algorithm_integration.input;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link CsvFileGenerator}
 */
public class CsvFileGeneratorTest {

	protected CsvFileFixture csvFileFixture;
	protected File expectedFile;
	protected char expectedSeparator;
	protected char expectedQuotechar;
	protected char expectedEscape;
	protected int expectedLine;
	protected boolean expectedStrictQuotes;
	protected boolean expectedIgnoreLeadingWhiteSpace;
	protected CsvFileGenerator generator;
	
	@Before
	public void setUp() throws Exception {
		this.csvFileFixture = new CsvFileFixture();
		this.expectedFile = csvFileFixture.getTestDataPath("test.csv");
		this.expectedSeparator = CsvFileFixture.SEPARATOR;
		this.expectedQuotechar = CsvFileFixture.QUOTE_CHAR;
		this.expectedEscape = '\\';
		this.expectedLine = 0;
		this.expectedStrictQuotes = true;
		this.expectedIgnoreLeadingWhiteSpace = true;
		this.generator = new CsvFileGenerator(expectedFile, expectedSeparator, expectedQuotechar, expectedEscape, expectedLine, expectedStrictQuotes, expectedIgnoreLeadingWhiteSpace);
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
		assertEquals(expectedFile, generator.inputFile);
		assertEquals(expectedSeparator, generator.separator);
		assertEquals(expectedQuotechar, generator.quotechar);
		assertEquals(expectedEscape, generator.escape);
		assertEquals(expectedLine, generator.line);
		assertEquals(expectedStrictQuotes, generator.strictQuotes);
		assertEquals(expectedIgnoreLeadingWhiteSpace, generator.ignoreLeadingWhiteSpace);
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
