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


import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import de.metanome.algorithm_integration.results.BasicStatistic;
import de.metanome.algorithm_integration.results.ConditionalUniqueColumnCombination;
import de.metanome.algorithm_integration.results.FunctionalDependency;
import de.metanome.algorithm_integration.results.InclusionDependency;
import de.metanome.algorithm_integration.results.OrderDependency;
import de.metanome.algorithm_integration.results.Result;
import de.metanome.algorithm_integration.results.UniqueColumnCombination;
import de.metanome.frontend.client.TabContent;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.services.ExecutionServiceAsync;

import java.util.ArrayList;


/**
 * UI element that displays the results of an algorithm execution in a table.
 */
public class ResultsTablePage extends FlowPanel implements OmniscientResultReceiver, TabContent {

  protected ExecutionServiceAsync executionService;

  protected String executionIdentifier;

  protected FlowPanel resultsPanel;
  protected TabWrapper messageReceiver;

  protected ResultTable uccTable;
  protected ResultTable cuccTable;
  protected ResultTable indTable;
  protected ResultTable fdTable;
  protected ResultTable odTable;
  protected ResultTable basicsTable;

  public ResultsTablePage(ExecutionServiceAsync executionService, String executionIdentifier) {
    this.executionService = executionService;
    this.executionIdentifier = executionIdentifier;

    this.resultsPanel = new FlowPanel();
    this.resultsPanel.addStyleName("left");
    this.add(resultsPanel);

    indTable = new ResultTable("Inclusion Dependencies");
    uccTable = new ResultTable("Unique Column Combinations");
    cuccTable = new ResultTable("Conditional Unique Column Combinations");
    fdTable = new ResultTable("Functional Dependencies");
    odTable = new ResultTable("Order Dependencies");
    basicsTable = new ResultTable("Basic Statistics");

    fetchResults();
  }

  /**
   * Fetches the results from the execution service and displays them on success.
   */
  protected void fetchResults() {
    executionService.fetchNewResults(executionIdentifier, new AsyncCallback<ArrayList<Result>>() {
      @Override
      public void onFailure(Throwable caught) {
      }

      @Override
      public void onSuccess(ArrayList<Result> result) {
        displayResults(result);
      }
    });
  }

  /**
   * Displays the incoming results.
   *
   * @param results the results of algorithm execution
   */
  protected void displayResults(ArrayList<Result> results) {
    for (Result r : results) {
      try {
        r.sendResultTo(this);
      } catch (CouldNotReceiveResultException e) {
        this.messageReceiver.addErrorHTML("Could not display results: " + e.getMessage());
      }
    }
  }

  @Override
  public void receiveResult(BasicStatistic statistic) {
    if (this.resultsPanel.getWidgetIndex(basicsTable) < 0) {
      this.resultsPanel.add(basicsTable);
    }

    int row = basicsTable.getRowCount();
    basicsTable.setText(row, 0, statistic.getColumnCombination().toString());
    basicsTable.setText(row, 1, statistic.getStatisticName());
    basicsTable.setText(row, 2, statistic.getStatisticValue().toString());
  }

  @Override
  public void receiveResult(InclusionDependency inclusionDependency) {
    if (this.resultsPanel.getWidgetIndex(indTable) < 0) {
      this.resultsPanel.add(indTable);
    }

    int row = indTable.getRowCount();
    indTable.setText(row, 0, inclusionDependency.getDependant().toString());
    indTable.setText(row, 1, "\u2286");
    indTable.setText(row, 2, inclusionDependency.getReferenced().toString());
  }

  @Override
  public void receiveResult(UniqueColumnCombination uniqueColumnCombination)
      throws CouldNotReceiveResultException {
    if (this.resultsPanel.getWidgetIndex(uccTable) < 0) {
      this.resultsPanel.add(uccTable);
    }

    int row = uccTable.getRowCount();
    int col = 0;
    for (ColumnIdentifier colId : uniqueColumnCombination.getColumnCombination()
        .getColumnIdentifiers()) {
      uccTable.setText(row, col, colId.toString());
      col++;
    }
  }

  @Override
  public void receiveResult(ConditionalUniqueColumnCombination conditionalUniqueColumnCombination)
      throws CouldNotReceiveResultException {
    if (this.resultsPanel.getWidgetIndex(cuccTable) < 0) {
      this.resultsPanel.add(cuccTable);
    }

    int row = cuccTable.getRowCount();
    cuccTable.setText(row, 0, conditionalUniqueColumnCombination.getColumnCombination().toString());
    cuccTable.setText(row, 1, ConditionalUniqueColumnCombination.CUCC_SEPARATOR);
    cuccTable.setWidget(row, 2, conditionalUniqueColumnCombination.buildPatternTableauTableHtml());
    cuccTable.setText(row, 3, ConditionalUniqueColumnCombination.CUCC_SEPARATOR);
    cuccTable.setText(row, 4, Float.toString(
        conditionalUniqueColumnCombination.getCondition().getCoverage()));
  }

  @Override
  public void receiveResult(FunctionalDependency functionalDependency) {
    if (this.resultsPanel.getWidgetIndex(fdTable) < 0) {
      this.resultsPanel.add(fdTable);
    }

    int row = fdTable.getRowCount();
    fdTable.setText(row, 0, functionalDependency.getDeterminant().toString());
    fdTable.setText(row, 1, "-->");
    fdTable.setText(row, 2, functionalDependency.getDependant().toString());
  }
  
  @Override
  public void receiveResult(OrderDependency orderDependency) throws CouldNotReceiveResultException {
    if (this.resultsPanel.getWidgetIndex(odTable) < 0) {
      this.resultsPanel.add(odTable);
    }

    int row = odTable.getRowCount();
    odTable.setText(row, 0, orderDependency.getLhs().toString());
    odTable.setText(row, 1, OrderDependency.OD_SEPARATOR);
    odTable.setText(row, 2, orderDependency.getRhs().toString());
    
  }

  /* (non-Javadoc)
   * @see de.metanome.frontend.client.TabContent#setMessageReceiver(de.metanome.frontend.client.TabWrapper)
   */
  @Override
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
  }
}
