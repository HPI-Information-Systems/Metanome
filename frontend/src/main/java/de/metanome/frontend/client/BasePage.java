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

package de.metanome.frontend.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.metanome.backend.resources.AlgorithmExecutionParams;
import de.metanome.backend.results_db.Algorithm;
import de.metanome.backend.results_db.Execution;
import de.metanome.backend.results_db.FileInput;
import de.metanome.frontend.client.algorithms.AlgorithmsPage;
import de.metanome.frontend.client.datasources.DataSourcePage;
import de.metanome.frontend.client.executions.ExecutionsPage;
import de.metanome.frontend.client.results.ResultsPage;
import de.metanome.frontend.client.runs.RunConfigurationPage;
import de.metanome.frontend.client.services.AlgorithmExecutionRestService;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.Date;
import java.util.List;

/**
 * Overall Application page that has tabs for the various functions (subpages). It also coordinates
 * control between these subpages. Should be added to RootPanel.
 *
 * @author Claudia Exeler
 */
public class BasePage extends TabLayoutPanel {

  public RunConfigurationPage runConfigurationsPage;
  protected ResultsPage resultsPage;
  protected TabWrapper resultPageTabWrapper;
  protected DataSourcePage dataSourcePage;
  protected AlgorithmsPage algorithmPage;
  protected ExecutionsPage executionPage;

  /**
   * Constructor. Initiates creation of subpages.
   */
  public BasePage() {
    super(1, Unit.CM);
    this.addStyleName("basePage");

    // Add data source tab
    this.dataSourcePage = new DataSourcePage(this);
    this.insert(new ScrollPanel(new TabWrapper(this.dataSourcePage)), "Data Sources",
                Tabs.DATA_SOURCES.ordinal());

    // Add algorithm tab
    this.algorithmPage = new AlgorithmsPage(this);
    this.insert(new ScrollPanel(new TabWrapper(this.algorithmPage)), "Algorithms",
                Tabs.ALGORITHMS.ordinal());

    // Add execution tab
    this.executionPage = new ExecutionsPage(this);
    this.insert(new ScrollPanel(new TabWrapper(this.executionPage)), "Executions",
                Tabs.EXECUTIONS.ordinal());

    // Add run configuration tab
    this.runConfigurationsPage = new RunConfigurationPage(this);
    this.insert(new ScrollPanel(new TabWrapper(this.runConfigurationsPage)), "Run Configuration",
                Tabs.RUN_CONFIGURATION.ordinal());

    // Add result tab
    this.resultsPage = new ResultsPage(this);
    this.resultPageTabWrapper = new TabWrapper(this.resultsPage);
    this.insert(this.resultPageTabWrapper, "Results", Tabs.RESULTS.ordinal());

    // Add about tab
    this.insert(createAboutPage(), "About", Tabs.ABOUT.ordinal());
  }

  /**
   * Create the "About" Page, which should include information about the project.
   *
   * @return Widget with contents to be placed on the page.
   */
  private Widget createAboutPage() {
    SimplePanel content = new SimplePanel();
    Label temporaryContent = new Label();
    temporaryContent.setText("Metanome Version 0.0.2.");
    content.add(temporaryContent);
    // content.addStyleName("aboutPage");
    return content;
  }

  /**
   * Hand control from the Run Configuration to displaying Results. Start algorithm execution.
   *
   * @param executionService the service instance used for executing the algorithm
   * @param algorithm        the algorithm to execute
   * @param parameters       the specification with set settings used to configure the algorithm
   * @param cacheResults     true, if the results should be cached and written to disk after the
   *                         algorithm is finished
   * @param writeResults     true, if the results should be written to disk immediately
   * @param countResults     true, if the results should be counted
   */
  public void startAlgorithmExecution(AlgorithmExecutionRestService executionService,
                                      Algorithm algorithm,
                                      List<ConfigurationRequirement> parameters,
                                      Boolean cacheResults,
                                      Boolean writeResults,
                                      Boolean countResults,
                                      String memory) {

    // clear previous errors
    this.resultPageTabWrapper.clearErrors();

    String executionIdentifier = getExecutionIdentifier(algorithm.getFileName());

    // Execute algorithm
    AlgorithmExecutionParams params = new AlgorithmExecutionParams()
        .setAlgorithmId(algorithm.getId())
        .setExecutionIdentifier(executionIdentifier)
        .setRequirements(parameters)
        .setCacheResults(cacheResults)
        .setWriteResults(writeResults)
        .setCountResults(countResults)
        .setMemory(memory);

    executionService.executeAlgorithm(params, this.getExecutionCallback());
    // During execution the progress is shown on the result page
    this.resultsPage
        .setExecutionParameter(executionIdentifier, algorithm.getFileName(), executionService,
                               countResults);
    this.resultsPage.startPolling(algorithm.isProgressEstimating());

    this.selectTab(Tabs.RESULTS.ordinal());
  }

  /**
   * Switches to the result page and shows the results of the given execution.
   *
   * @param execution the execution
   */
  public void showResultsFor(Execution execution) {
    this.resultsPage.showResults(execution);
    this.selectTab(Tabs.RESULTS.ordinal());
  }

  /**
   * Switches to the result page and shows the results of the given input.
   *
   * @param input the execution
   */
  public void showResultsFor(FileInput input) {
    this.resultsPage.showResults(input);
    this.selectTab(Tabs.RESULTS.ordinal());
  }

  /**
   * If the algorithm execution is successful, the results will be shown. otherwise the reason of
   * failure will be displayed.
   *
   * @return the callback
   */
  private MethodCallback<Execution> getExecutionCallback() {
    return new MethodCallback<Execution>() {
      public void onFailure(Method method, Throwable caught) {
        resultsPage
            .updateOnError(caught.getMessage() + " " + caught.getCause() + caught.toString());
      }

      public void onSuccess(Method method, Execution execution) {
        if (!execution.isAborted()) {
          resultsPage.updateOnSuccess(execution);
        }
        executionPage.addExecution(execution);
      }
    };
  }

  /**
   * Generates a string that uniquely identifies an algorithm execution.
   *
   * @param algorithmName the name of the algorithm being executed
   * @return a string consisting of the algorithmName and the current date and time
   */
  protected String getExecutionIdentifier(String algorithmName) {
    DateTimeFormat format = DateTimeFormat.getFormat("yyyy-MM-dd'T'HHmmss");
    return algorithmName + format.format(new Date());
  }

  /**
   * Hand control from any page to Run Configurations, and pre-configure the latter with the
   * algorithm and/or data source.
   *
   * @param algorithmName algorithm that shall be run
   * @param dataSource    data source that shall be profiled
   */
  public void switchToRunConfiguration(String algorithmName,
                                       ConfigurationSettingDataSource dataSource) {
    this.selectTab(Tabs.RUN_CONFIGURATION.ordinal());
    if (algorithmName != null) {
      this.runConfigurationsPage.selectAlgorithm(algorithmName);
    }
    if (dataSource != null) {
      this.runConfigurationsPage.setPrimaryDataSource(dataSource);
    }
  }

  /**
   * Forwards any algorithms found by AlgorithmPage to be available in RunConfigurations
   *
   * @param algorithms list of available algorithms
   */
  public void addAlgorithmsToRunConfigurations(List<Algorithm> algorithms) {
    this.runConfigurationsPage.addAlgorithms(algorithms);
  }

  /**
   * Forwards the update call to the run configuration page.
   */
  public void updateDataSourcesOnRunConfiguration() {
    this.runConfigurationsPage.updateDataSources();
  }

  /**
   * Forwards an algorithm, which should be removed, from the AlgorithmPage to the
   * RunConfigurations
   *
   * @param algorithmName the name of the algorithm, which should be removed
   */
  public void removeAlgorithmFromRunConfigurations(String algorithmName) {
    this.runConfigurationsPage.removeAlgorithm(algorithmName);
  }

  /**
   * Forwards an algorithm, which was updated, from the AlgorithmPage to the RunConfigurations
   *
   * @param algorithm the algorithm, which was updated
   * @param oldName   the old name of the algorithm
   */
  public void updateAlgorithmOnRunConfigurations(Algorithm algorithm, String oldName) {
    this.runConfigurationsPage.updateAlgorithm(algorithm, oldName);
  }

  public enum Tabs {
    DATA_SOURCES, ALGORITHMS, EXECUTIONS, RUN_CONFIGURATION, RESULTS, ABOUT
  }

}
