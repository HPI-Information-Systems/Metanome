package de.uni_potsdam.hpi.metanome.frontend.client.tabs;

import org.junit.Test;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnCombination;
import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.BasicStatistic;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.FunctionalDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.InclusionDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.UniqueColumnCombination;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionServiceAsync;

public class GwtTestResultsTab extends GWTTestCase {

	/**
	 * Test method for {@link ResultsTab#receiveResult(BasicStatistic)}
	 * 
	 * After receiving a {@link BasicStatistic} the appropriate results table should be updated.
	 */
	@Test
	public void testReceiveResultBasicStatistic() {
		// Setup
		ExecutionServiceAsync executionService = GWT.create(ExecutionService.class);
		ResultsTab resultsTab = new ResultsTab(executionService, "TestAlgorithm");
		
		// Execute functionality
		// Check result
		assertEquals(0, resultsTab.basicsTable.getRowCount());
		resultsTab.receiveResult(new BasicStatistic("Min", "MinValue", new ColumnIdentifier("table1", "column2")));
		assertEquals(1, resultsTab.basicsTable.getRowCount());
	}
	
	/**
	 * Test method for {@link ResultsTab#receiveResult(UniqueColumnCombination)}
	 * 
	 * After receiving a {@link UniqueColumnCombination} the appropriate results table should be updated.
	 */
	@Test
	public void testReceiveResultUniqueColumnCombinations() {
		// Setup
		ExecutionServiceAsync executionService = GWT.create(ExecutionService.class);
		ResultsTab resultsTab = new ResultsTab(executionService, "TestAlgorithm");
		
		// Execute functionality
		// Check result
		assertEquals(0, resultsTab.uccTable.getRowCount());
		resultsTab.receiveResult(new UniqueColumnCombination(
				new ColumnIdentifier("table1", "column2")));
		assertEquals(1, resultsTab.uccTable.getRowCount());
	}
	
	/**
	 * Test method for {@link ResultsTab#receiveResult(InclusionDependency)}
	 * 
	 * After receiving a {@link InclusionDependency} the appropriate results table should be updated.
	 */
	@Test
	public void testReceiveResultInclusionDependency() {
		// Setup
		ExecutionServiceAsync executionService = GWT.create(ExecutionService.class);
		ResultsTab resultsTab = new ResultsTab(executionService, "TestAlgorithm");
		
		// Execute functionality
		// Check result
		assertEquals(0, resultsTab.indTable.getRowCount());
		resultsTab.receiveResult(new InclusionDependency(
				new ColumnCombination(
						new ColumnIdentifier("table1", "column2")),
				new ColumnCombination(
						new ColumnIdentifier("table2", "column23"))));
		assertEquals(1, resultsTab.indTable.getRowCount());
	}
	
	/**
	 * Test method for {@link ResultsTab#receiveResult(FunctionalDependency)}
	 * 
	 * After receiving a {@link FunctionalDependency} the appropriate results table should be updated.
	 */
	@Test
	public void testReceiveResultFunctionalDependency() {
		// Setup
		ExecutionServiceAsync executionService = GWT.create(ExecutionService.class);
		ResultsTab resultsTab = new ResultsTab(executionService, "TestAlgorithm");
		
		// Execute functionality
		// Check result
		assertEquals(0, resultsTab.fdTable.getRowCount());
		resultsTab.receiveResult(new FunctionalDependency(
				new ColumnCombination(
						new ColumnIdentifier("table1", "column2")), 
				new ColumnIdentifier("table1", "column23")));
		assertEquals(1, resultsTab.fdTable.getRowCount());
	}
	
	@Override
	public String getModuleName() {
		return "de.uni_potsdam.hpi.metanome.frontend.Hello";
	}

}
