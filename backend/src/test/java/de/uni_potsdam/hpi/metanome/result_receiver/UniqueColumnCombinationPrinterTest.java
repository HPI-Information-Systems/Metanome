package de.uni_potsdam.hpi.metanome.result_receiver;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;

public class UniqueColumnCombinationPrinterTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * The UniqueColumnCombinationPrinter should print column combinations to the stream set on construction.
	 */
	@Test
	public void testPrinting() {
		// Setup
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		UniqueColumnCombinationPrinter printer = new UniqueColumnCombinationPrinter(new PrintStream(outStream));
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
		printer.receiveResult(columnCombination1);
		printer.receiveResult(columnCombination2);
		
		// Check result
		for (String output : expectedOutputs) {
			assertTrue(outStream.toString().contains(output));
		}
	}

}
