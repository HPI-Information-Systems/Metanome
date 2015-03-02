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
import de.metanome.frontend.client.services.ExecutionRestService;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.Date;
import java.util.List;

public class ExecutionsPage  extends FlowPanel implements TabContent {

  protected BasePage parent;
  protected FlexTable executionsTable;
  protected ExecutionRestService restService;
  protected TabWrapper messageReceiver;

  public ExecutionsPage(BasePage parent) {
    this.parent = parent;
    this.restService = com.google.gwt.core.client.GWT.create(ExecutionRestService.class);

    this.add(new HTML("<h3>Executions</h3>"));
    this.executionsTable = new FlexTable();
    this.add(this.executionsTable);

    this.addHeaderRow();
    updateExecutions();
  }

  private void addHeaderRow() {
    // algorithm, time, input, result type, show button
    this.executionsTable.setWidget(0, 0, new HTML("<b>Algorithm Name</b>"));
    this.executionsTable.setWidget(0, 1, new HTML("<b>Execution Time (HH:mm:ss)</b>"));
    this.executionsTable.setWidget(0, 2, new HTML("<b>Inputs</b>"));
    this.executionsTable.setWidget(0, 3, new HTML("<b>Result Types</b>"));
  }

  /**
   * Request a list of available UCC algorithms and display them in the uccList
   */
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
   * Adds each of the executions to the execution table.
   *
   * @param executions the algorithms to be displayed
   */
  protected void addExecutionsToTable(List<Execution> executions) {
    int row = this.executionsTable.getRowCount();

    for (final Execution execution : executions) {
      Button showButton = new Button("Show");
      showButton.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          parent.showResultsFor(execution);
        }
      });

      // algorithm, time, input, result type, show button
      this.executionsTable.setWidget(row, 0,
                                     new HTML(execution.getAlgorithm().getName()));
      this.executionsTable.setWidget(row, 1, new HTML(this.getExecutionTime(execution)));
      this.executionsTable.setWidget(row, 2, new HTML(this.getInputs(execution)));
      this.executionsTable.setWidget(row, 3, new HTML(this.getResultTypes(execution)));
      this.executionsTable.setWidget(row, 4, showButton);
      row++;
    }
  }

  /**
   * Convert the execution time to a readable format.
   * @param execution the execution
   * @return the execution time string
   */
  protected String getExecutionTime(Execution execution) {
    long time = execution.getEnd() - execution.getBegin();
    DateTimeFormat format = DateTimeFormat.getFormat("HH:mm:ss.SSS");
    Date date = new Date(Math.round(time / 1000000d));
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
      inputString = inputString + input.getName() + "<br>";
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
