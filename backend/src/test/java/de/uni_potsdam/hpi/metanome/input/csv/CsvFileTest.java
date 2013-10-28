package de.uni_potsdam.hpi.metanome.input.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.input.csv.CsvFile;

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
	 * @throws IOException
	 */
	@Test
	public void testHasNext() throws IOException {
		// Check result
		assertTrue(this.csvFile.hasNext());
		this.csvFile.next();
		assertFalse(this.csvFile.hasNext());
	}
	
	/**
	 * A one line csv should be parsed correctly. And all the values in the line should be equal.
	 * 
	 * @throws IOException 
	 */
	@Test
	public void testNext() throws IOException {
		// Check result
		assertEquals(this.fixture.getExpectedStrings(), this.csvFile.next());
	}
	
	/**
	 * A one line csv with differing should be parsed correctly. And all the values in the line should be equal.
	 * 
	 * @throws IOException 
	 */
	@Test
	public void testNextSeparator() throws IOException {
		// Setup
		CsvFileOneLineFixture fixtureSeparator = new CsvFileOneLineFixture(';');
		CsvFile csvFileSeparator = fixtureSeparator.getTestData();
		
		// Check result 
		assertEquals(fixtureSeparator.getExpectedStrings(), csvFileSeparator.next());
	}
	
	/**
	 * The remove method should always throw an {@link UnsupportedOperationException}.
	 */
	@Test
	public void testRemove() {
		// Check result
		try {
			this.csvFile.remove();
			fail("Expected an UnsupportedOperationException to be thrown.");
		}
		catch (UnsupportedOperationException actualException) {
			// Intentionally left blank
		}
	}
}
