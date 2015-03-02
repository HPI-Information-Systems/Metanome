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

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;

import de.metanome.frontend.client.BasePage;
import de.metanome.frontend.client.TabContent;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.services.AlgorithmExecutionRestService;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.Date;

/**
 * Tab that contains widgets displaying execution results, i.e. tables, visualizations etc.
 * Currently, these widgets are organized in another tab panel.
 *
 * @author Claudia Exeler
 */
public class ResultsPage extends FlowPanel implements TabContent {

  protected final BasePage basePage;
  protected TabWrapper messageReceiver;

  protected Image runningIndicator;
  protected ProgressBar progressBar;
  protected Timer executionTimeTimer;
  protected Timer progressTimer;
  protected FlowPanel executionTimePanel;

  protected Label algorithmLabel;

  protected ResultsTablePage tablePage;

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
   * @param executionTimeInNanos the execution time in nanoseconds
   */
  public void updateOnSuccess(Long executionTimeInNanos) {
    this.executionTimeTimer.cancel();
    this.progressTimer.cancel();

    // Fetch the last results or get all results depending on the used result receiver
    if (cacheResults)
      this.tablePage.fetchResults();
    else if (writeResults)
      this.tablePage.getPrinterResults();
    else if (countResults)
      this.tablePage.getCounterResults();

    this.remove(this.algorithmLabel);
    if (this.progressBar != null ) this.remove(this.progressBar);
    this.remove(this.runningIndicator);
    this.remove(this.executionTimePanel);

    // Add a label for the execution time
    DateTimeFormat format = DateTimeFormat.getFormat("HH:mm:ss.SSS");
    Date date = new Date(Math.round(executionTimeInNanos / 1000000d));
    String timeString = "Algorithm " + this.algorithmFileName + " executed in " +
                        format.format(date, TimeZone.createTimeZone(0)) +
                        " (HH:mm:ss.SSS) or " + executionTimeInNanos / 1000000d + " ms.";
    this.insert(new Label(timeString), 0);
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

    this.messageReceiver.addError("The execution was not successful: " + message);
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

    // Create new tab with result table
    this.tablePage =
        new ResultsTablePage(this.executionService, this.executionIdentifier);
    tablePage.setMessageReceiver(this.messageReceiver);

    // Create new tab with visualizations of result
    ResultsVisualizationPage visualizationTab = new ResultsVisualizationPage();
    visualizationTab.setMessageReceiver(this.messageReceiver);

    // Add the result table and the visualization tab
    TabLayoutPanel panel = new TabLayoutPanel(1, Unit.CM);
    panel.add(new ScrollPanel(tablePage), "Table");
    panel.add(new ScrollPanel(visualizationTab), "Visualization");
    this.add(panel);

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
        if (cacheResults) resultsTab.fetchResults();
      }
    };
    this.progressTimer.scheduleRepeating(10000);
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
