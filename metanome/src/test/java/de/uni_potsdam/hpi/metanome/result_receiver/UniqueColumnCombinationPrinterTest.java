package de.uni_potsdam.hpi.metanome.result_receiver;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		
		UniqueColumnCombinationPrinter printer = new UniqueColumnCombinationPrinter();
		ColumnCombination columnCombination1 = new ColumnCombination("column1", "column2");
		ColumnCombination columnCombination2 = new ColumnCombination("column2", "column3");
		// Expected values
		String expectedOutput = columnCombination1.toString() + "\n" + columnCombination2.toString() + "\n";
		
		// Execute funtionality
		printer.receiveResult(columnCombination1);
		printer.receiveResult(columnCombination2);
		
		// Check result 
		assertEquals(expectedOutput, outContent.toString());
		
		// Cleanup
		System.setOut(null);
	}

}
