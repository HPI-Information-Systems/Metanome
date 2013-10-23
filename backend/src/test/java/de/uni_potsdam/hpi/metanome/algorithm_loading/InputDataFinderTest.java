package de.uni_potsdam.hpi.metanome.algorithm_loading;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;

public class InputDataFinderTest {
	
	InputDataFinder inputDataFinder;
	
	@Before
	public void setUp(){
		inputDataFinder = new InputDataFinder();
	}

	@Test
	public void testRetrieveCsvFiles() throws UnsupportedEncodingException {
		//Setup
		String pathToAlgorithmsFolder = ClassLoader.getSystemResource("algorithms").getPath();
		String pathToCsvFolder = ClassLoader.getSystemResource("inputData").getPath();
		
		//Execute
		File[] csvsInAlgorithmsFolder = inputDataFinder.retrieveCsvFiles(pathToAlgorithmsFolder);
		File[] csvsInCsvFolder = inputDataFinder.retrieveCsvFiles(pathToCsvFolder);

		//Check
		assertTrue(csvsInAlgorithmsFolder.length == 0);
		assertTrue(csvsInCsvFolder.length > 0);
	}

	@Test
	public void testRetrieveAllCsvFiles() throws IOException, ClassNotFoundException {		
		//Execute
		File[] csvs = inputDataFinder.getAvailableCsvs();
		
		//Check
		assertTrue(csvs.length > 0);
	}
}
