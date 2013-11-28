package de.uni_potsdam.hpi.metanome.result_receiver;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.UniqueColumnCombination;

/**
 * Tests for {@link UniqueColumnCombinationPrinter}.
 */
public class UniqueColumnCombinationPrinterTest {
	
	protected String testResultFilePath;
	protected File testResultDirectory;
	protected UniqueColumnCombination uniqueColumnCombination1;
	protected UniqueColumnCombination uniqueColumnCombination2;
	protected List<String> expectedOutputs;

	@Before
	public void setUp() throws Exception {
		testResultFilePath = "results/test";
		testResultDirectory = new File(testResultFilePath);
		
		uniqueColumnCombination1 = new UniqueColumnCombination(
				new ColumnIdentifier("table1", "column1"), 
				new ColumnIdentifier("table2", "column2"));
		uniqueColumnCombination2 = new UniqueColumnCombination(
				new ColumnIdentifier("table2", "column2"), 
				new ColumnIdentifier("table3", "column3"));
		// Expected values
		expectedOutputs = new LinkedList<String>();
		expectedOutputs.add(uniqueColumnCombination1.toString());
		expectedOutputs.add(uniqueColumnCombination2.toString());
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * The {@link UniqueColumnCombinationPrinter} should print column combinations to the stream set on construction.
	 * 
	 * @throws IOException 
	 */
	@Test
	public void testPrinting() throws IOException {
		// Setup
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		UniqueColumnCombinationPrinter printer = new UniqueColumnCombinationPrinter(outStream);
		
		// Execute functionality
		printer.receiveResult(uniqueColumnCombination1);
		printer.receiveResult(uniqueColumnCombination2);
		
		// Check result
		for (String output : expectedOutputs) {
			assertTrue(outStream.toString().contains(output));
		}
		
		// Cleanup 
		printer.close();
	}
	
	/**
	 * The {@link UniqueColumnCombinationPrinter} should write column combinations to a new
	 * file with the specified name.
	 * 
	 * @throws IOException 
	 * @throws CouldNotReceiveResultException 
	 */
	@Test
	public void testFileWriting() throws IOException, CouldNotReceiveResultException {
		// Setup
		String fileName = "uccTest_" + new SimpleDateFormat("yyyy-MM-dd'T'HHmmss").format(new Date()) + ".txt";
		UniqueColumnCombinationPrinter writer = new UniqueColumnCombinationPrinter(fileName, testResultFilePath);
		
		// Execute functionality
		writer.receiveResult(uniqueColumnCombination1);
		writer.receiveResult(uniqueColumnCombination2);
		
		// Check result
		File actualFile = new File(testResultFilePath + "/" + fileName);
		assertTrue(actualFile.exists());

	    String fileContent = Files.toString(actualFile, Charsets.UTF_8);

		for (String output : expectedOutputs) {
			assertTrue(fileContent.contains(output));
		}
		
		// Cleanup
		writer.close();
		FileUtils.deleteDirectory(testResultDirectory.getParentFile());
		actualFile.delete();
	}

}
