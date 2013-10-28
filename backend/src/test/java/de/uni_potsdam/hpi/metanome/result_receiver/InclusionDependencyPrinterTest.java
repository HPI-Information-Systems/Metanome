package de.uni_potsdam.hpi.metanome.result_receiver;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
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

/**
 * Tests for {@link InclusionDependencyPrinter}.
 */
public class InclusionDependencyPrinterTest {

	protected ColumnCombination determinant1;
	protected ColumnCombination referenced1;
	protected ColumnCombination determinant2;
	protected ColumnCombination referenced2;
	protected List<String> expectedOutputs;
	
	@Before
	public void setUp() throws Exception {
		determinant1 = new ColumnCombination(
				new ColumnIdentifier("table1", "column1"),
				new ColumnIdentifier("table1", "column2"));
		referenced1 = new ColumnCombination(
				new ColumnIdentifier("table2", "column5"),
				new ColumnIdentifier("table2", "column2"));
		determinant2 = new ColumnCombination(
				new ColumnIdentifier("table1", "column5"),
				new ColumnIdentifier("table1", "column3"));
		referenced2 = new ColumnCombination(
				new ColumnIdentifier("table5", "column7"),
				new ColumnIdentifier("table5", "column3"));
		// Expected values
		expectedOutputs = new LinkedList<String>();
		expectedOutputs.add(determinant1 + InclusionDependencyPrinter.IND_SEPARATOR + referenced1);
		expectedOutputs.add(determinant2 + InclusionDependencyPrinter.IND_SEPARATOR + referenced2);
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
		printer.receiveResult(determinant1, referenced1);
		printer.receiveResult(determinant2, referenced2);
		
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
		InclusionDependencyPrinter writer = new InclusionDependencyPrinter(fileName, "results/test");
		
		// Execute functionality
		writer.receiveResult(determinant1, referenced1);
		writer.receiveResult(determinant2, referenced2);
		
		// Check result
		File actualFile = new File("results/test/" + fileName);
		assertTrue(actualFile.exists());

	    String fileContent = Files.toString(actualFile, Charsets.UTF_8);

		for (String output : expectedOutputs) {
			assertTrue(fileContent.contains(output));
		}
		
		// Cleanup
		actualFile.delete();
		writer.close();
	}

}
