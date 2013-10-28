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

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;

/**
 * Tests for {@link FunctionalDependencyPrinter}.
 */
public class FunctionalDependencyPrinterTest {

	protected String testResultFilePath;
	protected File testResultDirectory;
	protected ColumnCombination determinant1;
	protected ColumnIdentifier dependent1;
	protected ColumnCombination determinant2;
	protected ColumnIdentifier dependent2;
	protected List<String> expectedOutputs;
	
	@Before
	public void setUp() throws Exception {
		testResultFilePath = "results/test";
		testResultDirectory = new File(testResultFilePath);
		
		determinant1 = new ColumnCombination(
				new ColumnIdentifier("table1", "column1"),
				new ColumnIdentifier("table1", "column2"));
		dependent1 = new ColumnIdentifier("table1", "column3");
		determinant2 = new ColumnCombination(
				new ColumnIdentifier("table1", "column5"),
				new ColumnIdentifier("table1", "column3"));
		dependent2 = new ColumnIdentifier("table1", "column9");
		
		// Expected values
		expectedOutputs = new LinkedList<String>();
		expectedOutputs.add(determinant1 + FunctionalDependencyPrinter.FD_SEPARATOR + dependent1);
		expectedOutputs.add(determinant2 + FunctionalDependencyPrinter.FD_SEPARATOR + dependent2);		
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * The {@link FunctionalDependencyPrinter} should print functional dependencies to the stream set on construction.
	 * 
	 * @throws IOException 
	 * @throws CouldNotReceiveResultException 
	 */
	@Test
	public void testPrinting() throws IOException, CouldNotReceiveResultException {
		// Setup
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		FunctionalDependencyPrinter printer = new FunctionalDependencyPrinter(outStream);
		
		// Execute functionality
		printer.receiveResult(determinant1, dependent1);
		printer.receiveResult(determinant2, dependent2);
		
		// Check result
		for (String output : expectedOutputs) {
			assertTrue(outStream.toString().contains(output));
		}
		
		// Cleanup 
		printer.close();
	}
	
	/**
	 * The {@link FunctionalDependencyPrinter} should write functional dependencies to a new
	 * file with the specified name.
	 * 
	 * @throws IOException 
	 * @throws CouldNotReceiveResultException 
	 */
	@Test
	public void testFileWriting() throws IOException, CouldNotReceiveResultException {
		// Setup
		String fileName = "uccTest_" + new SimpleDateFormat("yyyy-MM-dd'T'HHmmss").format(new Date()) + ".txt";
		FunctionalDependencyPrinter writer = new FunctionalDependencyPrinter(fileName, testResultFilePath);
		
		// Execute functionality
		writer.receiveResult(determinant1, dependent1);
		writer.receiveResult(determinant2, dependent2);
		
		// Check result
		File actualFile = new File(testResultFilePath + "/" + fileName);
		assertTrue(actualFile.exists());

	    String fileContent = Files.toString(actualFile, Charsets.UTF_8);

		for (String output : expectedOutputs) {
			assertTrue(fileContent.contains(output));
		}
		
		// Cleanup
		FileUtils.deleteDirectory(testResultDirectory.getParentFile());
		actualFile.delete();
		writer.close();
	}

}
