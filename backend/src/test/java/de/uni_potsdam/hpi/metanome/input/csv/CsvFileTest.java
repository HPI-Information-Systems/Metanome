package de.uni_potsdam.hpi.metanome.input.csv;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CsvFileTest {

	protected CsvFile csvFile;
	
	@Before
	public void setUp() throws Exception {
		this.csvFile = new CsvFile();
	}

	@After
	public void tearDown() throws Exception {
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

		}
		
	}

}
