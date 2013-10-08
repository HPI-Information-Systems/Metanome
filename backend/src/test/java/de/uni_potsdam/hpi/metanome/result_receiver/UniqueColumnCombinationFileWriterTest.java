package de.uni_potsdam.hpi.metanome.result_receiver;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;

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
	 * @throws CouldNotReceiveResultException 
	 */
	@Test
	public void testFileWriting() throws IOException, CouldNotReceiveResultException {
		// Setup
		String fileName = "uccTest_" + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date()) + ".txt";
		UniqueColumnCombinationFileWriter writer = new UniqueColumnCombinationFileWriter(fileName, "results/test");
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
		
		// Execute functionality
		writer.receiveResult(columnCombination1);
		writer.receiveResult(columnCombination2);
		
		// Check result
		File actualFile = new File("results/test/" + fileName);
		assertTrue(actualFile.exists());

	    String fileContent = Files.toString(actualFile, Charsets.UTF_8);

		for (String output : expectedOutputs) {
			assertTrue(fileContent.contains(output));
		}
		
		// Cleanup
		actualFile.delete();
	}
	
	/**
	 * 	
	 */
	public void testFileWritingOnExistingFile(){
		//TODO what should happen when file already exists?
	}

}
