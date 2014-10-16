/*
 * Copyright 2014 by the Metanome project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.metanome.frontend.client.results;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.junit.client.GWTTestCase;

import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.results.BasicStatistic;
import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.algorithm_integration.results.InclusionDependency;
import de.metanome.algorithm_integration.results.UniqueColumnCombination;
import de.metanome.frontend.client.services.ExecutionService;
import de.metanome.frontend.client.services.ExecutionServiceAsync;

public class GwtTestResultsTablePage extends GWTTestCase {

  /**
   * Test method for {@link de.metanome.frontend.client.results.ResultsTablePage#ResultsTablePage(de.metanome.frontend.client.services.ExecutionServiceAsync,
   * String)}
   */
  public void testResultsTablePage() {
    // Setup
    // Execute functionality
    ExecutionServiceAsync executionService = GWT.create(ExecutionService.class);
    ResultsTablePage resultsTab = new ResultsTablePage(executionService, "TestAlgorithm");

    // Check result
    assertNotNull(resultsTab.resultsPanel);
    assertNotNull(resultsTab.executionIdentifier);
    assertNotNull(resultsTab.executionService);
    assertNotNull(resultsTab.indTable);
    assertNotNull(resultsTab.uccTable);
    assertNotNull(resultsTab.cuccTable);
    assertNotNull(resultsTab.odTable);
    assertNotNull(resultsTab.fdTable);
    assertNotNull(resultsTab.basicsTable);
  }

  /**
   * Test method for {@link ResultsTablePage#receiveResult(BasicStatistic)} <p/> After receiving a
   * {@link BasicStatistic} the appropriate results table should be updated.
   */
  public void testReceiveResultBasicStatistic() {
    // Setup
    ExecutionServiceAsync executionService = GWT.create(ExecutionService.class);
    ResultsTablePage resultsTab = new ResultsTablePage(executionService, "TestAlgorithm");

    // Execute functionality
    // Check result
    assertEquals(0, resultsTab.basicsTable.getRowCount());
    resultsTab.receiveResult(
        new BasicStatistic("Min", "MinValue", new ColumnIdentifier("table1", "column2")));
    assertEquals(1, resultsTab.basicsTable.getRowCount());
  }

  /**
   * Test method for {@link ResultsTablePage#receiveResult(UniqueColumnCombination)} <p/> After
   * receiving a {@link UniqueColumnCombination} the appropriate results table should be updated.
   */
  public void testReceiveResultUniqueColumnCombinations() throws CouldNotReceiveResultException {
    // Setup
    ExecutionServiceAsync executionService = GWT.create(ExecutionService.class);
    ResultsTablePage resultsTab = new ResultsTablePage(executionService, "TestAlgorithm");

    // Execute functionality
    // Check result
    assertEquals(0, resultsTab.uccTable.getRowCount());
    resultsTab.receiveResult(new UniqueColumnCombination(
        new ColumnIdentifier("table1", "column2")));
    assertEquals(1, resultsTab.uccTable.getRowCount());
  }

  /**
   * Test method for {@link ResultsTablePage#receiveResult(InclusionDependency)} <p/> After
   * receiving a {@link InclusionDependency} the appropriate results table should be updated.
   */
  public void testReceiveResultInclusionDependency() {
    // Setup
    ExecutionServiceAsync executionService = GWT.create(ExecutionService.class);
    ResultsTablePage resultsTab = new ResultsTablePage(executionService, "TestAlgorithm");

    // Execute functionality
    // Check result
    assertEquals(0, resultsTab.indTable.getRowCount());
    resultsTab.receiveResult(new InclusionDependency(
        new ColumnCombination(
            new ColumnIdentifier("table1", "column2")),
        new ColumnCombination(
            new ColumnIdentifier("table2", "column23"))
    ));
    assertEquals(1, resultsTab.indTable.getRowCount());
  }

  /**
   * Test method for {@link ResultsTablePage#receiveResult(FunctionalDependency)} <p/> After
   * receiving a {@link FunctionalDependency} the appropriate results table should be updated.
   */
  public void testReceiveResultFunctionalDependency() {
    // Setup
    ExecutionServiceAsync executionService = GWT.create(ExecutionService.class);
    ResultsTablePage resultsTab = new ResultsTablePage(executionService, "TestAlgorithm");

    // Execute functionality
    // Check result
    assertEquals(0, resultsTab.fdTable.getRowCount());
    resultsTab.receiveResult(new FunctionalDependency(
        new ColumnCombination(
            new ColumnIdentifier("table1", "column2")),
        new ColumnIdentifier("table1", "column23")
    ));
    assertEquals(1, resultsTab.fdTable.getRowCount());
  }

  @Override
  public String getModuleName() {
    return "de.metanome.frontend.MetanomeTest";
  }

}
