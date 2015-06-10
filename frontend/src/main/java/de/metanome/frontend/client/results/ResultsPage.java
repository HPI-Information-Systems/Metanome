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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
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
import de.metanome.frontend.client.helpers.FilePathHelper;
import de.metanome.frontend.client.services.AlgorithmExecutionRestService;
import de.metanome.frontend.client.services.ResultStoreRestService;

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
  protected Timer executionTimeTimer;
  protected FlowPanel executionTimePanel;
  protected TabLayoutPanel panel;

  protected Boolean countResults;

  protected Label algorithmLabel;

  protected Button stopButton;

  protected ResultsPaginationTablePage tablePage;

  protected String executionIdentifier;
  private String algorithmFileName;

  private AlgorithmExecutionRestService executionRestService;
  protected ResultStoreRestService resultStoreService;

  private boolean clickedAdvancedResults;

  /**
   * Constructs the tab, creating a full height {@link TabLayoutPanel} with 1cm headers.
   *
   * @param parent the page that this element will be attached to
   */
  public ResultsPage(BasePage parent) {
    super();
    this.basePage = parent;
    this.clickedAdvancedResults = false;
    this.add(new Label("There are no results yet."));
    this.resultStoreService = GWT.create(ResultStoreRestService.class);
  }

  /**
   * Adds the child pages to show the results.
   *
   * @param execution the execution
   */
  public void updateOnSuccess(Execution execution) {
    this.executionTimeTimer.cancel();

    this.remove(this.algorithmLabel);
    this.remove(this.runningIndicator);
    this.remove(this.executionTimePanel);
    if (this.stopButton != null) {
      this.remove(this.stopButton);
    }

    // Add a label for the execution time
    this.insert(new Label(
                    getExecutionTimeString(this.algorithmFileName,
                                           execution.getEnd() - execution.getBegin())),
                0);

    // Add button for calculating data dependent result statistics
    if (!this.countResults && !this.clickedAdvancedResults) {
      this.add(new Label("If you want to calculate some more statistics, please click here (this may take a while):"));
      this.add(getAdvancedResultsButton(execution));
    }

    // Add the result table
    this.addChildPages();

    if (this.countResults) {
      this.tablePage.addCountResults(execution);
    } else {
      this.tablePage.addTables(execution);
    }
  }

  /**
   * Button for calculating advanced result statistics for the results of the
   * given execution..
   * @param execution the execution
   * @return the button
   */
  protected Button getAdvancedResultsButton(final Execution execution) {
    return new Button("More Statistics", new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        clear();
        clickedAdvancedResults = true;
        resultStoreService.loadExecution(execution.getId(), true, new MethodCallback<Void>() {
          @Override
          public void onFailure(Method method, Throwable throwable) {
            messageReceiver.addError(method.getResponse().getText());
          }

          @Override
          public void onSuccess(Method method, Void aVoid) {
            updateOnSuccess(execution);
          }
        });
      }
    });
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
    this.clear();

    this.messageReceiver.addErrorHTML("The execution was not successful: " + message);
  }

  /**
   * Displays the current status of execution.
   */
  public void startPolling() {
    this.clear();
    this.clickedAdvancedResults = false;

    // Add label for the algorithm, which is executed at the moment
    this.algorithmLabel = new Label("Executing algorithm " + this.algorithmFileName);
    this.algorithmLabel.setStyleName("space_bottom");
    this.add(this.algorithmLabel);

    // Add a running indicator
    this.runningIndicator = new Image("ajax-loader.gif");
    this.runningIndicator.setStyleName("space_bottom");
    this.add(this.runningIndicator);

    // Add a label showing the execution time
    this.executionTimePanel = new FlowPanel();
    this.executionTimePanel.setStyleName("space_bottom");
    this.executionTimePanel.add(new Label("Execution Time (HH:mm:ss): "));
    final Label executionTimeLabel = new Label("00:00:00");
    this.executionTimePanel.add(executionTimeLabel);
    this.add(this.executionTimePanel);

    // Add button to stop the execution
    this.stopButton = new Button("Stop Execution", new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        executionRestService.stopExecution(executionIdentifier, getStopCallback());
      }
    });
    this.add(this.stopButton);

    // Start timer for the execution Panel
    this.executionTimeTimer = new Timer() {
      public void run() {
        executionTimeLabel.setText(toTimeString(toTimeInt(executionTimeLabel.getText()) + 1));
      }
    };
    this.executionTimeTimer.scheduleRepeating(1000);
  }

  /**
   * Creates the callback to stop an execution.
   *
   * @return the callback
   */
  private MethodCallback<Void> getStopCallback() {
    return new MethodCallback<Void>() {
      @Override
      public void onFailure(Method method, Throwable throwable) {
        updateOnError(method.getResponse().getText());
      }

      @Override
      public void onSuccess(Method method, Void aVoid) {
        updateOnError("The execution was stopped successfully.");
      }
    };
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
        new Label("All results of the file input " + FilePathHelper.getFileName(input.getName())),
        0);
  }


  /**
   * Adds the child pages.
   */
  private void addChildPages() {
    // Create new tab with result table
    this.tablePage = new ResultsPaginationTablePage();
    tablePage.setMessageReceiver(this.messageReceiver);
    tablePage.setStyleName("result_inner_tab");

    // Create new tab with visualization page
    ResultsVisualizationPage visualizationPage = new ResultsVisualizationPage();
    visualizationPage.setMessageReceiver(this.messageReceiver);
    visualizationPage.setStyleName("result_inner_tab");

    // Add the result table
    this.panel = new TabLayoutPanel(1, Unit.CM);
    panel.add(new ScrollPanel(tablePage), "Table");
    panel.add(new ScrollPanel(visualizationPage), "Visualization");
    this.add(panel);
  }

  /**
   * Sets the parameter needed for execution
   *
   * @param executionIdentifier the execution identifier
   * @param algorithmFileName   the algorithm file name
   */
  public void setExecutionParameter(String executionIdentifier,
                                    String algorithmFileName,
                                    AlgorithmExecutionRestService restService,
                                    Boolean countResults) {
    this.algorithmFileName = algorithmFileName;
    this.executionIdentifier = executionIdentifier;
    this.executionRestService = restService;
    this.countResults = countResults;
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
