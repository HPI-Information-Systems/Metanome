package de.uni_potsdam.hpi.metanome.algorithm_loading;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class InputDataFinderTest {
	
	InputDataFinder inputDataFinder;
	
	@Before
	public void setUp(){
		inputDataFinder = new InputDataFinder();
	}

	@Test
	public void testRetrieveCsvFiles() {
		//Setup
		String pathToFolder = ClassLoader.getSystemResource("algorithms").getPath();
		
		//Execute
		int noOfFoundCsvs = inputDataFinder.retrieveCsvFiles(pathToFolder).length;
		
		//Check
		assertTrue(noOfFoundCsvs == 0);
	}

	@Test
	public void testRetrieveAllCsvFiles() throws IOException, ClassNotFoundException {		
		//Execute
		String[] csvs = inputDataFinder.getAvailableCsvs();
		
		//Check
		assertTrue(csvs.length > 0);
	}
}
