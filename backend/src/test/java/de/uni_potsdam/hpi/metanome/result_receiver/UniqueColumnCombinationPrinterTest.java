package de.uni_potsdam.hpi.metanome.result_receiver;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;

public class UniqueColumnCombinationPrinterTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * TODO: docs
	 */
	@Test
	public void test() {
		// Setup
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		
		UniqueColumnCombinationPrinter printer = new UniqueColumnCombinationPrinter(new PrintStream(outStream));
		ColumnCombination columnCombination1 = new ColumnCombination("column1", "column2");
		ColumnCombination columnCombination2 = new ColumnCombination("column2", "column3");
		// Expected values
		List<String> expectedOutputs = new LinkedList<String>();
		expectedOutputs.add(columnCombination1.toString());
		expectedOutputs.add(columnCombination2.toString());
		
		// Execute funtionality
		printer.receiveResult(columnCombination1);
		printer.receiveResult(columnCombination2);
		
		// Check result
		for (String output : expectedOutputs) {
			assertTrue(outStream.toString().contains(output));
		}
	}

}
