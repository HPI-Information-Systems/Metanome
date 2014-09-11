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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;

import de.metanome.frontend.client.BasePage;
import de.metanome.frontend.client.TabContent;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.services.ExecutionServiceAsync;

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
  protected Timer timer;

  protected String executionIdentifier;
  private String algorithmFileName;
  private ExecutionServiceAsync executionService;

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
   * @param resultsTab            the results tab
   * @param visualizationTab      the visualization tab
   * @param executionTimeNanoSecs the execution time in nanoseconds
   */
  public void updateOnSuccess(ResultsTablePage resultsTab,
                              ResultsVisualizationPage visualizationTab,
                              Long executionTimeNanoSecs) {
    this.timer.cancel();
    this.clear();

    // Add a label for the execution time
    DateTimeFormat format = DateTimeFormat.getFormat("HH:mm:ss.SSS");
    Date date = new Date(Math.round(executionTimeNanoSecs / 1000000d));
    this.add(new Label("Algorithm " + this.algorithmFileName + " executed in " + format
        .format(date, TimeZone.createTimeZone(0))
                       + " (HH:mm:ss.SSS) or " + executionTimeNanoSecs / 1000000d + " ms."));

    // Add the result table and the visualization tab
    TabLayoutPanel panel = new TabLayoutPanel(1, Unit.CM);
    panel.add(new ScrollPanel(resultsTab), "Table");
    panel.add(new ScrollPanel(visualizationTab), "Visualization");
    this.add(panel);
  }

  /**
   * If there was an problem with the algorithm execution, display the given error.
   *
   * @param message the error message
   */
  public void updateOnError(String message) {
    this.timer.cancel();
    this.clear();

    this.add(new Label("The execution was not successful."));
    this.messageReceiver.addError(message);
  }

  /**
   * Displays the current status of execution.
   */
  public void startPolling() {
    this.clear();

    // Add label for the algorithm, which is executed at the moment
    this.add(new Label("Executing algorithm " + this.algorithmFileName));

    // Add a running indicator
    this.runningIndicator = new Image("ajax-loader.gif");
    this.add(this.runningIndicator);

    this.progressBar = new ProgressBar(0, 1);
    this.add(this.progressBar);

    this.timer = new Timer() {
      public void run() {
        updateProgress();
      }
    };

    this.timer.scheduleRepeating(10000);
  }

  /**
   * Fetching the current progress of execution and update the progress bar on success.
   */
  protected void updateProgress() {
    this.executionService.fetchProgress(executionIdentifier, new AsyncCallback<Float>() {
      @Override
      public void onFailure(Throwable caught) {
        messageReceiver.addError("Could not fetch progress.");
      }

      @Override
      public void onSuccess(Float progress) {
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
  public void setExecutionParameter(ExecutionServiceAsync executionService,
                                    String executionIdentifier, String algorithmFileName) {
    this.executionService = executionService;
    this.algorithmFileName = algorithmFileName;
    this.executionIdentifier = executionIdentifier;
  }

  /* (non-Javadoc)
 * @see de.metanome.frontend.client.TabContent#setMessageReceiver(de.metanome.frontend.client.TabWrapper)
 */
  @Override
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
  }
}
