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
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.InclusionDependency;

/**
 * Tests for {@link InclusionDependencyPrinter}.
 */
public class InclusionDependencyPrinterTest {

	protected String testResultFilePath;
	protected File testResultDirectory;
	protected InclusionDependency ind1;
	protected InclusionDependency ind2;
	protected List<String> expectedOutputs;
	
	@Before
	public void setUp() throws Exception {
		testResultFilePath = "results/test";
		testResultDirectory = new File(testResultFilePath);
		
		ind1 = new InclusionDependency(
				new ColumnCombination(
					new ColumnIdentifier("table1", "column1"),
					new ColumnIdentifier("table1", "column2")),
				new ColumnCombination(
					new ColumnIdentifier("table2", "column5"),
					new ColumnIdentifier("table2", "column2")));
		ind2 = new InclusionDependency(
				new ColumnCombination(
					new ColumnIdentifier("table1", "column5"),
					new ColumnIdentifier("table1", "column3")),
				new ColumnCombination(
					new ColumnIdentifier("table5", "column7"),
					new ColumnIdentifier("table5", "column3")));
		// Expected values
		expectedOutputs = new LinkedList<String>();
		expectedOutputs.add(ind1.toString());
		expectedOutputs.add(ind2.toString());
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * The {@link InclusionDependencyPrinter} should print inclusion dependencies to the stream set on construction.
	 * 
	 * @throws IOException 
	 * @throws CouldNotReceiveResultException 
	 */
	@Test
	public void testPrinting() throws IOException, CouldNotReceiveResultException {
		// Setup
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		InclusionDependencyPrinter printer = new InclusionDependencyPrinter(outStream);
		
		// Execute functionality
		printer.receiveResult(ind1);
		printer.receiveResult(ind2);
		
		// Check result
		for (String output : expectedOutputs) {
			assertTrue(outStream.toString().contains(output));
		}
		
		// Cleanup 
		printer.close();
	}
	
	/**
	 * The {@link InclusionDependencyPrinter} should write inclusion dependencies to a new
	 * file with the specified name.
	 * 
	 * @throws IOException 
	 * @throws CouldNotReceiveResultException 
	 */
	@Test
	public void testFileWriting() throws IOException, CouldNotReceiveResultException {
		// Setup
		String fileName = "uccTest_" + new SimpleDateFormat("yyyy-MM-dd'T'HHmmss").format(new Date()) + ".txt";
		InclusionDependencyPrinter writer = new InclusionDependencyPrinter(fileName, testResultFilePath);
		
		// Execute functionality
		writer.receiveResult(ind1);
		writer.receiveResult(ind2);
		
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
