package de.uni_potsdam.hpi.metanome.result_receiver;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;

public class UniqueColumnCombinationFileWriterTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * The UniqueColumnCombinationFileWwriter should write column combinations to a new
	 * file with the specified name.
	 * 
	 * @throws IOException 
	 */
	@Test
	public void testFileWriting() throws IOException {
		// Setup
		String fileName = "uccTest_" + DateFormat.getInstance().format(new Date());
		UniqueColumnCombinationFileWriter writer = new UniqueColumnCombinationFileWriter(fileName);
		ColumnCombination columnCombination1 = new ColumnCombination(
				new ColumnIdentifier("table1", "column1"), 
				new ColumnIdentifier("table2", "column2"));
		ColumnCombination columnCombination2 = new ColumnCombination(
				new ColumnIdentifier("table2", "column2"), 
				new ColumnIdentifier("table3", "column3"));
		// Expected values
		List<String> expectedOutputs = new LinkedList<String>();
		expectedOutputs.add(columnCombination1.toString());
		expectedOutputs.add(columnCombination2.toString());
		
		// Execute funtionality
		writer.receiveResult(columnCombination1);
		writer.receiveResult(columnCombination2);
		
		// Check result
		assertTrue(new File(fileName).exists());
				
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
	    String line = null;
	    StringBuilder  stringBuilder = new StringBuilder();
	    while((line = reader.readLine() ) != null)
	        stringBuilder.append( line );
	    reader.close();
	    
		for (String output : expectedOutputs) {
			assertTrue(stringBuilder.toString().contains(output));
		}
		assertTrue(!stringBuilder.toString().contains("\\n"));
	}
	
	/**
	 * 	
	 */
	public void testFileWritingOnExistingFile(){
		//TODO what should happen when file already exists?
	}

}
