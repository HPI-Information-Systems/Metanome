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

import de.metanome.backend.results_db.Execution;
import de.metanome.backend.results_db.FileInput;
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

  protected BasePage basePage;
  protected TabWrapper messageReceiver;

  protected Image runningIndicator;
  protected ProgressBar progressBar;
  protected Timer executionTimeTimer;
  protected Timer progressTimer;
  protected FlowPanel executionTimePanel;
  protected TabLayoutPanel panel;

  protected Label algorithmLabel;

  protected ResultsPaginationTablePage tablePage;

  protected String executionIdentifier;
  private String algorithmFileName;

  private AlgorithmExecutionRestService executionRestService;

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
   * Adds the child pages to show the results.
   *
   * @param execution the execution
   */
  public void updateOnSuccess(Execution execution) {
    this.executionTimeTimer.cancel();
    this.progressTimer.cancel();

    this.remove(this.algorithmLabel);
    if (this.progressBar != null) {
      this.remove(this.progressBar);
    }
    this.remove(this.runningIndicator);
    this.remove(this.executionTimePanel);

    // Add a label for the execution time
    this.insert(new Label(
        getExecutionTimeString(this.algorithmFileName, execution.getEnd() - execution.getBegin())),
                0);

    // Add the result table
    this.addChildPages();
    this.tablePage.addTables(execution);
  }

  /**
   * If there was an problem with the algorithm execution, display the given error.
   *
   * @param message the error message
   */
  public void updateOnError(String message) {
    if (this.executionTimeTimer != null) {
      this.executionTimeTimer.cancel();
    }
    if (this.progressTimer != null) {
      this.progressTimer.cancel();
    }
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
        if (showProgress) {
          updateProgress();
        }
      }
    };
    this.progressTimer.scheduleRepeating(10000);
  }

  /**
   * Displays the results of the given execution.
   *
   * @param execution the execution
   */
  public void showResults(Execution execution) {
    this.messageReceiver.clearErrors();
    this.clear();

    this.addChildPages();
    this.tablePage.showResultsFor(execution);

    // Add a label for the execution time
    long executionTime = execution.getEnd() - execution.getBegin(); // milliseconds
    this.insert(
        new Label(getExecutionTimeString(execution.getAlgorithm().getFileName(), executionTime)),
        0);
  }

  /**
   * Displays the results of the given file input.
   *
   * @param input the execution
   */
  public void showResults(FileInput input) {
    this.messageReceiver.clearErrors();
    this.clear();

    this.addChildPages();
    this.tablePage.showResultsFor(input);

    this.insert(
        new Label("All results of the file input " + input.getName()), 0);
  }


  /**
   * Adds the child pages.
   */
  private void addChildPages() {
    // Create new tab with result table
    this.tablePage = new ResultsPaginationTablePage();
    tablePage.setMessageReceiver(this.messageReceiver);

    // Create new tab with visualization page
    ResultsVisualizationPage visualizationPage = new ResultsVisualizationPage();
    visualizationPage.setMessageReceiver(this.messageReceiver);

    // Add the result table
    this.panel = new TabLayoutPanel(1, Unit.CM);
    panel.add(new ScrollPanel(tablePage), "Table");
    panel.add(new ScrollPanel(visualizationPage), "Visualization");
    this.add(panel);
  }

  /**
   * Fetching the current progress of execution and update the progress bar on success.
   */
  protected void updateProgress() {
    this.executionRestService.fetchProgress(executionIdentifier, new MethodCallback<Float>() {
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
   * @param executionIdentifier the execution identifier
   * @param algorithmFileName   the algorithm file name
   */
  public void setExecutionParameter(String executionIdentifier,
                                    String algorithmFileName,
                                    AlgorithmExecutionRestService restService) {
    this.algorithmFileName = algorithmFileName;
    this.executionIdentifier = executionIdentifier;
    this.executionRestService = restService;
  }

  /**
   * Helper method to convert a string into an integer of seconds.
   *
   * @param str string containing a time
   * @return time as int
   */
  protected int toTimeInt(String str) {
    String[] parts = str.split(":");
    int h = Integer.parseInt(parts[0]);
    int m = Integer.parseInt(parts[1]);
    int s = Integer.parseInt(parts[2]);
    return h * 3600 + m * 60 + s;
  }

  /**
   * Helper method to convert a int to a time string.
   *
   * @param seconds seconds
   * @return string representing a time
   */
  protected String toTimeString(int seconds) {
    DateTimeFormat format = DateTimeFormat.getFormat("HH:mm:ss");
    Date date = new Date(seconds * 1000);
    return format.format(date, TimeZone.createTimeZone(0));
  }

  /**
   * Helper method to get the execution time string.
   *
   * @param algorithmFileName the algorithm file name
   * @param executionTime     the execution time
   * @return the execution time string
   */
  private String getExecutionTimeString(String algorithmFileName, long executionTime) {
    DateTimeFormat format = DateTimeFormat.getFormat("HH:mm:ss.SSS");
    Date date = new Date(executionTime);
    return "Algorithm " + algorithmFileName + " executed in " +
           format.format(date, TimeZone.createTimeZone(0)) +
           " (HH:mm:ss.SSS) or " + executionTime + " ms.";
  }

  /* (non-Javadoc)
 * @see de.metanome.frontend.client.TabContent#setMessageReceiver(de.metanome.frontend.client.TabWrapper)
 */
  @Override
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
  }
}
