package de.uni_potsdam.hpi.metanome.input.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputGenerationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.input.InputIterationException;

public class CsvFileTest {

	CsvFileOneLineFixture fixture;
	CsvFile csvFile;
	
	@Before
	public void setUp() throws Exception {
		this.fixture = new CsvFileOneLineFixture();
		this.csvFile = this.fixture.getTestData();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Has next should be true once and false after calling next once (one csv line).
	 * 
	 * @throws InputIterationException 
	 */
	@Test
	public void testHasNext() throws InputIterationException {
		// Check result
		assertTrue(this.csvFile.hasNext());
		this.csvFile.next();
		assertFalse(this.csvFile.hasNext());
	}
	
	/**
	 * A one line csv should be parsed correctly. And all the values in the line should be equal.
	 * 
	 * @throws InputIterationException 
	 */
	@Test
	public void testNext() throws InputIterationException {
		// Check result
		assertEquals(this.fixture.getExpectedStrings(), this.csvFile.next());
	}
	
	/**
	 * A one line csv with differing should be parsed correctly. And all the values in the line should be equal.
	 * 
	 * @throws InputIterationException 
	 * @throws InputGenerationException 
	 */
	@Test
	public void testNextSeparator() throws InputIterationException, InputGenerationException {
		// Setup
		CsvFileOneLineFixture fixtureSeparator = new CsvFileOneLineFixture(';');
		CsvFile csvFileSeparator = fixtureSeparator.getTestData();
		
		// Check result 
		assertEquals(fixtureSeparator.getExpectedStrings(), csvFileSeparator.next());
	}
	
	/**
	 * When iterating over a csv file with alternating line length an exception should be thrown.
	 * 
	 * @throws InputIterationException 
	 * @throws InputGenerationException 
	 */
	@Test
	public void testShort() throws InputIterationException, InputGenerationException {
		// Setup
		CsvFile shortCsvFile = new CsvFileShortLineFixture().getTestData();
		
		// Check result
		try {
			shortCsvFile.next();
			shortCsvFile.next();
			fail("Expected an InputIterationException to be thrown.");
		} catch (InputIterationException actualException) {
			// Intentionally left blank
		}
	}
	
	/**
	 * Test method for {@link CsvFile#numberOfColumns()}
	 * 
	 * A {@link CsvFile} should return the correct number of columns of the file.
	 * 
	 * @throws InputIterationException
	 * @throws InputGenerationException 
	 */
	@Test
	public void testNumberOfColumns() throws InputIterationException, InputGenerationException {
		// Setup 
		CsvFileOneLineFixture fixtureSeparator = new CsvFileOneLineFixture(';');
		CsvFile csvFile = fixtureSeparator.getTestData();
		
		// Check result
		assertEquals(fixture.getExpectedNumberOfColumns(), csvFile.numberOfColumns());
	}
	
	/**
	 * Test method for {@link CsvFile#relationName()}
	 * 
	 * A {@link CsvFile} should return a relation name.
	 * 
	 * @throws InputGenerationException 
	 * @throws InputIterationException 
	 */
	@Test
	public void testRelationName() throws InputIterationException, InputGenerationException {
		// Setup
		CsvFileOneLineFixture fixtureSeparator = new CsvFileOneLineFixture(';');
		CsvFile csvFile = fixtureSeparator.getTestData();
		
		// Execute functionality
		// Check result
		assertEquals(fixture.getExpectedRelationName(), csvFile.relationName());
	}
	
	/**
	 * Test method for {@link CsvFile#columnNames()}
	 * 
	 * A {@link CsvFile} should return the correct column names.
	 * 
	 * @throws InputIterationException
	 * @throws InputGenerationException 
	 */
	@Test
	public void testColumnNames() throws InputIterationException, InputGenerationException {
		// Setup
		CsvFileOneLineFixture fixtureSeparator = new CsvFileOneLineFixture(';');
		CsvFile csvFile = fixtureSeparator.getTestData();
		
		// Execute functionality
		// Check result
		assertEquals(fixture.getExpectedColumnNames(), csvFile.columnNames());
	}
	
	/**
	 * Test method for {@link CsvFile#generateHeaderLine()}
	 * 
	 * A {@link CsvFile} should return the correct header names.
	 * 
	 * @throws InputIterationException
	 * @throws InputGenerationException 
	 */
	@Test
	public void testGenerateHeaderLine() throws InputIterationException, InputGenerationException {
		// Setup
		CsvFileOneLineFixture fixtureSeparator = new CsvFileOneLineFixture(';');

		CsvFile csvFileWithHeader = fixtureSeparator.getTestData();
		CsvFile csvFileWithoutHeader = fixtureSeparator.getTestDataWithoutHeader();
		
		// Execute functionality
		// Check result
		assertEquals(fixture.getExpectedColumnNames(), csvFileWithHeader.columnNames());
		assertEquals(fixture.getExpectedDefaultColumnNames(), csvFileWithoutHeader.columnNames());
		assertEquals(fixture.getExpectedStrings(), csvFileWithoutHeader.next());
	}
	
	/**
	 * Test method for {@link CsvFile#CsvFile(String, java.io.Reader, char, char)}
	 * 
	 * A {@link CsvFile} generated from an empty file should be constructable without
	 * exceptions, return false on hasNext and return 0 as numberOfColumns.
	 * 
	 * @throws InputIterationException 
	 * @throws IOException 
	 */
	@Test
	public void testConstructWithEmptyFile() throws InputIterationException, IOException {
		// Execute functionality
		// Check result
		// Should not throw exception
		CsvFile csvFile = new CsvFile("testRelation", new StringReader(""), ',', '"');
		assertFalse(csvFile.hasNext());
		assertEquals(0, csvFile.numberOfColumns());
		
		// Cleanup
		csvFile.close();
	}
}
