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


import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

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
import de.metanome.backend.results_db.ResultType;
import de.metanome.frontend.client.TabContent;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.services.AlgorithmExecutionRestService;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/**
 * UI element that displays the results of an algorithm execution in a table.
 */
public class ResultsTablePage extends FlowPanel implements OmniscientResultReceiver, TabContent {

  protected AlgorithmExecutionRestService executionService;

  protected String executionIdentifier;

  protected FlowPanel resultsPanel;
  protected TabWrapper messageReceiver;

  protected ResultTable uccTable;
  protected ResultTable cuccTable;
  protected ResultTable indTable;
  protected ResultTable fdTable;
  protected ResultTable odTable;
  protected ResultTable basicsTable;

  public ResultsTablePage(AlgorithmExecutionRestService executionService, String executionIdentifier) {
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
  }

  /**
   * Fetches the results from the execution service and displays them on success.
   */
  protected void fetchPrinterResults() {
    if (executionService == null)
      return;

    executionService.getPrinterResults(executionIdentifier, getResultCallback());
  }

  /**
   * Fetches the results from the execution service and displays them on success.
   */
  protected void fetchCacheResults() {
    if (executionService == null)
      return;

    executionService.getCacheResults(executionIdentifier, getResultCallback());
  }

  /**
   * Fetches the results from the execution service and displays them on success.
   */
  protected void getCounterResults() {
    if (executionService == null)
      return;

    executionService.getCounterResults(executionIdentifier, new MethodCallback<Map<ResultType, Integer>>() {
      @Override
      public void onFailure(Method method, Throwable caught) {
      }

      @Override
      public void onSuccess(Method method, Map<ResultType, Integer> result) {
        if (result.containsKey(ResultType.UCC))
          displayCountResult(result.get(ResultType.UCC), uccTable);

        if (result.containsKey(ResultType.FD))
          displayCountResult(result.get(ResultType.FD), fdTable);

        if (result.containsKey(ResultType.IND))
          displayCountResult(result.get(ResultType.IND), indTable);

        if (result.containsKey(ResultType.CUCC))
          displayCountResult(result.get(ResultType.CUCC), cuccTable);

        if (result.containsKey(ResultType.OD))
          displayCountResult(result.get(ResultType.OD), odTable);

        if (result.containsKey(ResultType.STAT))
          displayCountResult(result.get(ResultType.STAT), basicsTable);

      }
    });
  }

  private void displayCountResult(Integer count, ResultTable table) {
    if (this.resultsPanel.getWidgetIndex(table) < 0) {
      this.resultsPanel.add(table);
    }

    int row = table.getRowCount();
    table.setText(row, 0, "#");
    table.setText(row, 1, String.valueOf(count));
  }

  protected void readResultsFromFile(Set<de.metanome.backend.results_db.Result> resultSet) {
    for (de.metanome.backend.results_db.Result result : resultSet) {
      this.executionService.readResultFromFile(result.getFileName(), result.getType().getName(), getResultCallback());
    }
  }

  private MethodCallback<List<Result>> getResultCallback() {
    return new MethodCallback<List<Result>>() {
      @Override
      public void onFailure(Method method, Throwable throwable) {
        messageReceiver.addError("Could not display all results: " + method.getResponse().getText());
      }

      @Override
      public void onSuccess(Method method, List<Result> results) {
        displayResults(results);
      }
    };
  }

  /**
   * Displays the incoming results.
   *
   * @param results the results of algorithm execution
   */
  protected void displayResults(List<Result> results) {
    for (Result r : results) {
      try {
        r.sendResultTo(this);
      } catch (CouldNotReceiveResultException e) {
        this.messageReceiver.addError("Could not display results: " + e.getMessage());
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
    cuccTable.setWidget(row, 2, buildPatternTableauTableHtml(conditionalUniqueColumnCombination));
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

  public Widget buildPatternTableauTableHtml(ConditionalUniqueColumnCombination cucc) {
    FlexTable table = new FlexTable();

    //build header
    TreeSet<ColumnIdentifier> header = cucc.getCondition().getContainedColumns();
    int i = 0;
    for (ColumnIdentifier headColumn : header) {
      table.setText(0, i, headColumn.toString());
      i++;
    }

    int rowCount = 1;
    List<Map<ColumnIdentifier, String>> conditions = cucc.getCondition().getPatternConditions();
    for (Map<ColumnIdentifier, String> condition : conditions) {
      int columnCount = 0;
      for (ColumnIdentifier column : header) {
        if (condition.containsKey(column)) {
          String value = condition.get(column);
          table.setText(rowCount, columnCount, value);
        } else {
          table.setText(rowCount, columnCount, "-");
        }
        columnCount++;
      }
      rowCount++;
    }
    return table;
  }
}
