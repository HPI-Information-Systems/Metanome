/*
 * Copyright 2015 by the Metanome project
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

package de.metanome.frontend.client.executions;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

import de.metanome.backend.results_db.Execution;
import de.metanome.backend.results_db.Input;
import de.metanome.backend.results_db.Result;
import de.metanome.frontend.client.BasePage;
import de.metanome.frontend.client.TabContent;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.FilePathHelper;
import de.metanome.frontend.client.services.ExecutionRestService;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ExecutionsPage  extends FlowPanel implements TabContent {

  protected BasePage parent;
  protected FlexTable executionsTable;
  protected ExecutionRestService restService;
  protected TabWrapper messageReceiver;

  public ExecutionsPage(BasePage parent) {
    this.parent = parent;
    this.restService = com.google.gwt.core.client.GWT.create(ExecutionRestService.class);

    this.add(new HTML("<h3>List of all Executions</h3>"));
    this.executionsTable = new FlexTable();
    this.executionsTable.addStyleName("execution-table");
    this.add(this.executionsTable);

    this.addHeaderRow();
    updateExecutions();
  }

  private void addHeaderRow() {
    // algorithm, time, input, result type, show button
    this.executionsTable.setWidget(0, 0, new HTML("<b>Algorithm Name</b>"));
    this.executionsTable.setWidget(0, 1, new HTML("<b>Date</b>"));
    this.executionsTable.setWidget(0, 2, new HTML("<b>Execution Time (HH:mm:ss)</b>"));
    this.executionsTable.setWidget(0, 3, new HTML("<b>Inputs</b>"));
    this.executionsTable.setWidget(0, 4, new HTML("<b>Result Types</b>"));
  }

  private void updateExecutions() {
    this.restService.listExecutions(getRetrieveCallback());
  }

  private MethodCallback<List<Execution>> getRetrieveCallback() {
    return new MethodCallback<List<Execution>>() {
      @Override
      public void onFailure(Method method, Throwable throwable) {
        messageReceiver.addError("Could not list previous executions: " + method.getResponse().getText());
      }

      @Override
      public void onSuccess(Method method, List<Execution> executions) {
        addExecutionsToTable(executions);
      }
    };
  }

  /**
   * Adds the given execution to the top of the table.
   * @param execution the execution
   */
  public void addExecution(Execution execution) {
    int index = this.executionsTable.insertRow(1);
    this.addExecutionToTable(execution, index);
  }

  /**
   * Adds each of the executions to the execution table.
   * @param executionList the executions
   */
  protected void addExecutionsToTable(List<Execution> executionList) {
    // Hibernate performs a left outer join, so that duplicates can be present.
    // Converting the result into a set, removes these duplicates.
    Set<Execution> executions = new TreeSet<>(executionList);
    int row;
    for (final Execution execution : executions) {
      row = this.executionsTable.getRowCount();
      if(!execution.isAborted()) {
        addExecutionToTable(execution, row);
      }
      else{
        addAbortedExecutionToTable(execution, row);
      }
    }
  }

  /**
   * Adds an executions to the execution table.
   *
   * @param execution the execution to be displayed
   * @param row       the row, in which the execution should be inserted
   */
  protected void addExecutionToTable(final Execution execution, int row) {
    Button showButton = new Button("Show");
    showButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        parent.showResultsFor(execution);
      }
    });

    Button deleteButton = new Button("Delete");
    deleteButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        deleteExecution(execution);
      }
    });

    // algorithm, date, time, input, result type, show button
    this.executionsTable.setWidget(row, 0,
                                   new HTML(execution.getAlgorithm().getName()));
    this.executionsTable.setWidget(row, 1, new HTML(this.getDate(execution)));
    this.executionsTable.setWidget(row, 2, new HTML(this.getExecutionTime(execution)));
    this.executionsTable.setWidget(row, 3, new HTML(this.getInputs(execution)));
    this.executionsTable.setWidget(row, 4, new HTML(this.getResultTypes(execution)));
    this.executionsTable.setWidget(row, 5, showButton);
    this.executionsTable.setWidget(row, 6, deleteButton);
  }

  /**
   * Adds an executions to the execution table.
   *
   * @param execution the aborted execution to be displayed
   * @param row       the row, in which the execution should be inserted
   */
  protected void addAbortedExecutionToTable(final Execution execution, int row) {

    Button deleteButton = new Button("Delete");
    deleteButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        deleteExecution(execution);
      }
    });

    // algorithm, date, time, input, result type, show button
    this.executionsTable.setWidget(row, 0,
                                   new HTML(execution.getAlgorithm().getName()));
    this.executionsTable.setWidget(row, 1, new HTML(this.getDate(execution)));
    this.executionsTable.setWidget(row, 2, new HTML("-"+"<br>"));
    this.executionsTable.setWidget(row, 3, new HTML(this.getInputs(execution)));
    this.executionsTable.setWidget(row, 4, new HTML("EXECUTION ABORTED"+"<br>"));
    this.executionsTable.setWidget(row, 6, deleteButton);
  }

  /**
   * Sends a delete request to the backend.
   * @param execution the execution, which should be deleted
   */
  private void deleteExecution(Execution execution) {
    messageReceiver.clearErrors();
    this.restService.deleteExecution(execution.getId(), getDeleteCallback(execution));
  }

  protected MethodCallback<Void> getDeleteCallback(final Execution execution) {
    return new MethodCallback<Void>() {
      @Override
      public void onFailure(Method method, Throwable throwable) {
        messageReceiver.addError("Could not delete the execution: " + method.getResponse().getText());
      }

      @Override
      public void onSuccess(Method method, Void aVoid) {
        executionsTable.removeRow(findRow(execution));
      }
    };
  }

  /**
   * Find the row in the table, which contains the given execution.
   * @param execution the execution
   * @return the row number
   */
  private int findRow(Execution execution) {
    int row = 0;

    String algorithm = execution.getAlgorithm().getName();
    String date = this.getDate(execution);

    while (row < this.executionsTable.getRowCount()) {
      HTML algorithmWidget = (HTML) this.executionsTable.getWidget(row, 0);
      HTML dateWidget = (HTML) this.executionsTable.getWidget(row, 1);

      if (algorithmWidget != null && algorithm.equals(
          algorithmWidget.getText()) &&
          dateWidget != null && date.equals(dateWidget.getText())) {
        return row;
      }
      row++;
    }
    return -1;
  }

  /**
   * Get the time, when the execution was started.
   * @param execution the execution
   * @return the date string
   */
  private String getDate(Execution execution) {
    return new Date(execution.getBegin()).toString();
  }

  /**
   * Convert the execution time to a readable format.
   * @param execution the execution
   * @return the execution time string
   */
  protected String getExecutionTime(Execution execution) {
    long time = execution.getEnd() - execution.getBegin(); // milliseconds
    DateTimeFormat format = DateTimeFormat.getFormat("HH:mm:ss.SSS");
    Date date = new Date(time);
    return format.format(date, TimeZone.createTimeZone(0));
  }

  /**
   *
   * @param execution the execution
   * @return an HTML string listing all inputs
   */
  protected String getInputs(Execution execution) {
    String inputString = "";
    for (Input input : execution.getInputs()) {
      String inputName = input.getName();
      if (inputName.endsWith(".csv") || inputName.endsWith(".tsv")) {
        inputName = FilePathHelper.getFileName(inputName);
      }
      inputString = inputString + inputName + "<br>";
    }
    return inputString;
  }

  /**
   *
   * @param execution the execution
   * @return an HTML string listing all result types
   */
  private String getResultTypes(Execution execution) {
    String resultTypeString = "";
    for (Result result : execution.getResults()) {
      resultTypeString = resultTypeString + result.getType().getName() + "<br>";
    }
    return resultTypeString;
  }

  @Override
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
  }


}
