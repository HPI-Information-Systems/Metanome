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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;

import de.metanome.backend.results_db.Execution;
import de.metanome.backend.results_db.FileInput;
import de.metanome.backend.results_db.Result;
import de.metanome.frontend.client.BasePage;
import de.metanome.frontend.client.TabContent;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.FilePathHelper;
import de.metanome.frontend.client.services.AlgorithmExecutionRestService;
import de.metanome.frontend.client.services.ResultRestService;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Tab that contains widgets displaying execution results, i.e. tables, visualizations etc.
 * Currently, these widgets are organized in another tab panel.
 *
 * @author Claudia Exeler
 */
public class ResultsPage extends FlowPanel implements TabContent {

  protected BasePage basePage;
  protected TabWrapper messageReceiver;

  protected Image runningIndicator;
  protected ProgressBar progressBar;
  protected Timer executionTimeTimer;
  protected Timer progressTimer;
  protected FlowPanel executionTimePanel;

  protected Label algorithmLabel;

  protected ResultsTablePage tablePage;

  //ID of the execution in hibernate storage
  protected Execution execution;
  protected TabLayoutPanel tabLayoutPanel;

  protected String executionIdentifier;
  private String algorithmFileName;
  private AlgorithmExecutionRestService executionService;

  private Boolean cacheResults;
  private Boolean writeResults;
  private Boolean countResults;

  /**
   * Constructs the tab, creating a full height {@link TabLayoutPanel} with 1cm headers.
   *
   * @param parent the page that this element will be attached to
   */
  public ResultsPage(BasePage parent) {
    super();
    this.basePage = parent;
    this.add(new Label("There are no results yet."));
  }

  /**
   * Adds the Tabs for results and visualization.
   *
   * @param execution the execution
   * @param executionTimeInMs the execution time in milliseconds
   */
  public void updateOnSuccess(Execution execution, Long executionTimeInMs) {
    this.execution = execution;

    this.executionTimeTimer.cancel();
    this.progressTimer.cancel();

    // Fetch the last results or get all results depending on the used result receiver
    if (cacheResults)
      this.tablePage.fetchCacheResults();
    else if (writeResults)
      this.tablePage.fetchPrinterResults();
    else if (countResults)
      this.tablePage.getCounterResults();

    this.remove(this.algorithmLabel);
    if (this.progressBar != null ) this.remove(this.progressBar);
    this.remove(this.runningIndicator);
    this.remove(this.executionTimePanel);

    // Add a label for the execution time
    this.insert(new Label(getExecutionTimeString(this.algorithmFileName, executionTimeInMs)), 0);

    // Add the postprocessing pages
    this.addPostProcessingPages();
  }

  /**
   * If there was an problem with the algorithm execution, display the given error.
   *
   * @param message the error message
   */
  public void updateOnError(String message) {
    if (this.executionTimeTimer != null) this.executionTimeTimer.cancel();
    if (this.progressTimer != null) this.progressTimer.cancel();
    this.clear();

    this.messageReceiver.addErrorHTML("The execution was not successful: " + message);
  }

  /**
   * Displays the current status of execution.
   */
  public void startPolling(final boolean showProgress) {
    this.clear();

    // Add label for the algorithm, which is executed at the moment
    this.algorithmLabel = new Label("Executing algorithm " + this.algorithmFileName);
    this.add(algorithmLabel);

    // Add a running indicator
    this.runningIndicator = new Image("ajax-loader.gif");
    this.add(this.runningIndicator);

    // Add a label showing the execution time
    this.executionTimePanel = new FlowPanel();
    this.executionTimePanel.add(new Label("Execution Time (HH:mm:ss): "));
    final Label executionTimeLabel = new Label("00:00:00");
    this.executionTimePanel.add(executionTimeLabel);
    this.add(this.executionTimePanel);

    // Add a progress bar if the algorithm supports it
    if (showProgress) {
      this.progressBar = new ProgressBar(0, 1);
      this.add(this.progressBar);
    }

    this.addChildPages(this.executionService, this.executionIdentifier);

    final ResultsTablePage resultsTab = tablePage;

    // Start timer for the execution Panel
    this.executionTimeTimer = new Timer() {
      public void run() {
        executionTimeLabel.setText(toTimeString(toTimeInt(executionTimeLabel.getText()) + 1));
      }
    };
    this.executionTimeTimer.scheduleRepeating(1000);

    // Start timer for fetching the progress
    this.progressTimer = new Timer() {
      public void run() {
        if (showProgress) updateProgress();
        if (cacheResults) resultsTab.fetchCacheResults();
      }
    };
    this.progressTimer.scheduleRepeating(10000);
  }

  /**
   * Displays the results of the given execution.
   * @param execution the execution
   */
  public void showResults(Execution execution) {
    this.messageReceiver.clearErrors();
    this.clear();
    AlgorithmExecutionRestService executionRestService = GWT.create(
        AlgorithmExecutionRestService.class);

    this.addChildPages(executionRestService, execution.getIdentifier());

    // Fetch the results
    this.tablePage.readResultsFromFile(execution.getResults());

    // Add a label for the execution time
    long executionTime = execution.getEnd() - execution.getBegin(); // milliseconds
    this.insert(new Label(getExecutionTimeString(execution.getAlgorithm().getFileName(), executionTime)), 0);
  }

  private void addPostProcessingPages(){
    // Create the ranking tab
    if(execution.getAlgorithm().isFd()){
      FDResultsRankingTablePage fdRankingTab = new FDResultsRankingTablePage(execution.getId());
      tabLayoutPanel.add(new ScrollPanel(fdRankingTab), "Ranking Table");
      // Create new tab with visualizations of result
      FDResultsVisualizationPage visualizationTab = new FDResultsVisualizationPage(execution.getId());
      visualizationTab.setMessageReceiver(messageReceiver);
      tabLayoutPanel.add(new ScrollPanel(visualizationTab), "Visualization");
    }
    if(execution.getAlgorithm().isInd()){
      INDResultsRankingTablePage indRankingTab = new INDResultsRankingTablePage(execution.getId());
      tabLayoutPanel.add(new ScrollPanel(indRankingTab), "Ranking Table");
    }
    if(execution.getAlgorithm().isUcc()){
      UCCResultsRankingTablePage uccRankingTab = new UCCResultsRankingTablePage(execution.getId());
      tabLayoutPanel.add(new ScrollPanel(uccRankingTab), "Ranking Table");
      // Create new tab with visualizations of result
      UCCVisualizationPage uccVisualizationTab = new UCCVisualizationPage(execution.getId());
        uccVisualizationTab.setMessageReceiver(messageReceiver);
      tabLayoutPanel.add(new ScrollPanel(uccVisualizationTab), "UCCVisualization");

    }
}

  /**
   * Displays the results of the given file input.
   * @param input the execution
   */
  public void showResults(FileInput input) {
    this.messageReceiver.clearErrors();
    this.clear();

    ResultRestService resultRestService = GWT.create(ResultRestService.class);
    AlgorithmExecutionRestService algorithmExecutionRestService = GWT.create(AlgorithmExecutionRestService.class);

    this.addChildPages(algorithmExecutionRestService, "");

    resultRestService.getResultsForFileInput(input.getId(), getShowResultCallback());
    this.insert(
        new Label("All results for input " + FilePathHelper.getFileName(input.getName()) + "."), 0);
  }

  /**
   * Sends a call to the backend for obtaining all executions for a specific input.
   * If the callback is successful, the results of the executions are sent to the table page.
   * @return the method callback
   */
  private MethodCallback<List<Result>> getShowResultCallback() {
    return new MethodCallback<List<Result>>() {
      @Override
      public void onFailure(Method method, Throwable throwable) {
        messageReceiver.addError("Could not display results: " +  method.getResponse().getText());
      }

      @Override
      public void onSuccess(Method method, List<Result> results) {
        if (results.isEmpty()) {
          tablePage.add(new Label("There are no results yet."));
          return;
        }
        tablePage.readResultsFromFile(new HashSet<>(results));
      }
    };
  }

  private void addChildPages(AlgorithmExecutionRestService executionService, String executionIdentifier) {
      // Create new tab with result table
      this.tablePage =
          new ResultsTablePage(executionService, executionIdentifier);
      tablePage.setMessageReceiver(this.messageReceiver);

      // Create new tab with visualizations of result
      //ResultsVisualizationPage visualizationTab = new ResultsVisualizationPage();
      //visualizationTab.setMessageReceiver(this.messageReceiver);

      // Add the result table and the visualization tab
      tabLayoutPanel = new TabLayoutPanel(1, Unit.CM);
      tabLayoutPanel.add(new ScrollPanel(tablePage), "Table");
//      tabLayoutPanel.add(new ScrollPanel(visualizationTab), "Visualization");
      this.add(tabLayoutPanel);
  }

  protected int toTimeInt(String str) {
    String[] parts = str.split(":");
    int h = Integer.parseInt(parts[0]);
    int m = Integer.parseInt(parts[1]);
    int s = Integer.parseInt(parts[2]);
    return h * 3600 + m * 60 + s;
  }

  protected String toTimeString(int seconds) {
    DateTimeFormat format = DateTimeFormat.getFormat("HH:mm:ss");
    Date date = new Date(seconds * 1000);
    return format.format(date, TimeZone.createTimeZone(0));
  }
  
  private String getExecutionTimeString(String algorithmFileName, long executionTime) {
    DateTimeFormat format = DateTimeFormat.getFormat("HH:mm:ss.SSS");
    Date date = new Date(executionTime);
    return "Algorithm " + algorithmFileName + " executed in " +
           format.format(date, TimeZone.createTimeZone(0)) +
           " (HH:mm:ss.SSS) or " + executionTime + " ms.";
  }

  /**
   * Fetching the current progress of execution and update the progress bar on success.
   */
  protected void updateProgress() {
    this.executionService.fetchProgress(executionIdentifier, new MethodCallback<Float>() {
      @Override
      public void onFailure(Method method, Throwable caught) {
        messageReceiver.addError(method.getResponse().getText());
      }

      @Override
      public void onSuccess(Method method, Float progress) {
        updateProgress(progress);
      }
    });
  }

  /**
   * Updates the progress of the progress bar.
   *
   * @param progress the current progress
   */
  protected void updateProgress(Float progress) {
    if (progress > 0) {
      progressBar.setProgress(progress);
    }
  }

  /**
   * Sets the parameter needed for execution
   *
   * @param executionService    the execution service
   * @param executionIdentifier the execution identifier
   * @param algorithmFileName   the algorithm file name
   */
  public void setExecutionParameter(AlgorithmExecutionRestService executionService,
                                    String executionIdentifier,
                                    String algorithmFileName,
                                    Boolean cacheResults,
                                    Boolean writeResults,
                                    Boolean countResults) {
    this.executionService = executionService;
    this.algorithmFileName = algorithmFileName;
    this.executionIdentifier = executionIdentifier;
    this.cacheResults = cacheResults;
    this.writeResults = writeResults;
    this.countResults = countResults;
  }

  /* (non-Javadoc)
 * @see de.metanome.frontend.client.TabContent#setMessageReceiver(de.metanome.frontend.client.TabWrapper)
 */
  @Override
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
  }
}
