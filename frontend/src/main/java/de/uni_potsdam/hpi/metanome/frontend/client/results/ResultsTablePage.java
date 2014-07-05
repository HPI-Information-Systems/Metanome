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

package de.uni_potsdam.hpi.metanome.frontend.client.results;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

import de.uni_potsdam.hpi.metanome.algorithm_integration.ColumnIdentifier;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.result_receiver.OmniscientResultReceiver;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.BasicStatistic;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.FunctionalDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.InclusionDependency;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.Result;
import de.uni_potsdam.hpi.metanome.algorithm_integration.results.UniqueColumnCombination;
import de.uni_potsdam.hpi.metanome.frontend.client.TabContent;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionServiceAsync;

import java.util.ArrayList;
import java.util.Date;

public class ResultsTablePage extends FlowPanel implements
                                                OmniscientResultReceiver, TabContent {

  protected ExecutionServiceAsync executionService;

  protected Timer timer;
  protected String executionIdentifier;

  protected HorizontalPanel resultsPanel;
  protected TabWrapper errorReceiver;

  protected ResultTable uccTable;
  protected ResultTable indTable;
  protected ResultTable fdTable;
  protected ResultTable basicsTable;

  protected Image runningIndicator;

  protected ProgressBar progressBar = null;


  public ResultsTablePage(ExecutionServiceAsync executionService, String executionIdentifier) {
    this.executionService = executionService;
    this.executionIdentifier = executionIdentifier;

    this.setHeight("500px");
    this.resultsPanel = new HorizontalPanel();
    this.add(resultsPanel);

    indTable = new ResultTable("Inclusion Dependencies");
    uccTable = new ResultTable("Unique Column Combinations");
    fdTable = new ResultTable("Functional Dependencies");
    basicsTable = new ResultTable("Basic Statistics");

    runningIndicator = new Image("ajax-loader.gif");
    this.add(runningIndicator);
  }

  public void startPolling() {
    this.timer = new Timer() {
      public void run() {
        fetchNewResults();
        updateStatus();
      }
    };

    this.timer.scheduleRepeating(10000);
  }

  public AsyncCallback<Long> getCancelCallback() {
    AsyncCallback<Long> callback = new AsyncCallback<Long>() {
      public void onFailure(Throwable caught) {
        cancelTimerOnFail(caught);
      }

      public void onSuccess(Long executionTime) {
        cancelTimerOnSuccess(executionTime);
      }
    };
    return callback;
  }

  public void cancelTimerOnSuccess(Long executionTimeNanoSecs) {
    this.timer.cancel();
    this.remove(runningIndicator);
    fetchNewResults();
    updateStatus();
    DateTimeFormat format = DateTimeFormat.getFormat("HH:mm:ss.SSS");
    Date date = new Date(Math.round(executionTimeNanoSecs / 1000000d));
    this.add(new Label("Algorithm executed in " + format.format(date, TimeZone.createTimeZone(0))
                       + " (HH:mm:ss.SSS) or " + executionTimeNanoSecs / 1000000d + " ms."));
  }

  public void cancelTimerOnFail(Throwable caught) {
    this.timer.cancel();
    this.clear();
    this.errorReceiver.addError("Algorithm did not execute successfully: " + caught.getMessage());
  }

  protected void fetchNewResults() {
    executionService.fetchNewResults(executionIdentifier, new AsyncCallback<ArrayList<Result>>() {

      @Override
      public void onFailure(Throwable caught) {
        errorReceiver.addError("Could not fetch results.");
      }

      @Override
      public void onSuccess(ArrayList<Result> result) {
        displayResults(result);
      }
    });
  }

  protected void updateStatus() {
    executionService.fetchProgress(executionIdentifier, new AsyncCallback<Float>() {

      @Override
      public void onFailure(Throwable caught) {
        errorReceiver.addError("Could not fetch progress.");
      }

      @Override
      public void onSuccess(Float progress) {
        updateProgress(progress);
      }
    });
  }

  protected void displayResults(ArrayList<Result> results) {
    for (Result r : results) {
      try {
        r.sendResultTo(this);
      } catch (CouldNotReceiveResultException e) {
        this.errorReceiver.addError(e.getMessage());
        e.printStackTrace();    //TODO remove after testing
      }
    }
  }

  protected void updateProgress(Float progress) {
    if (progress > 0) {
      if (progressBar == null) {
        this.remove(runningIndicator);
        progressBar = new ProgressBar(0, 1);
        this.add(progressBar);
      }
      progressBar.setProgress(progress);
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
    indTable.setText(row, 1, inclusionDependency.getReferenced().toString());
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
  public void receiveResult(FunctionalDependency functionalDependency) {
    if (this.resultsPanel.getWidgetIndex(fdTable) < 0) {
      this.resultsPanel.add(fdTable);
    }

    int row = fdTable.getRowCount();
    fdTable.setText(row, 0, functionalDependency.getDeterminant()
        .toString());
    fdTable.setText(row, 1, "-->");
    fdTable.setText(row, 2, functionalDependency.getDependant().toString());
  }

  /* (non-Javadoc)
   * @see de.uni_potsdam.hpi.metanome.frontend.client.TabContent#setErrorReceiver(de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper)
   */
  @Override
  public void setErrorReceiver(TabWrapper tab) {
    this.errorReceiver = tab;
  }
}
