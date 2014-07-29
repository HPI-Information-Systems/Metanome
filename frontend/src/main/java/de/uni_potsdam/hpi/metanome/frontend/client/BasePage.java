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

package de.uni_potsdam.hpi.metanome.frontend.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.frontend.client.algorithms.AlgorithmsPage;
import de.uni_potsdam.hpi.metanome.frontend.client.datasources.DataSourcesPage;
import de.uni_potsdam.hpi.metanome.frontend.client.results.ResultsPage;
import de.uni_potsdam.hpi.metanome.frontend.client.results.ResultsTablePage;
import de.uni_potsdam.hpi.metanome.frontend.client.results.ResultsVisualizationPage;
import de.uni_potsdam.hpi.metanome.frontend.client.runs.RunConfigurationPage;
import de.uni_potsdam.hpi.metanome.frontend.client.services.AlgorithmServiceAsync;
import de.uni_potsdam.hpi.metanome.frontend.client.services.ExecutionServiceAsync;
import de.uni_potsdam.hpi.metanome.results_db.Algorithm;

/**
 * Overall Application page that has tabs for the various functions (subpages). It also coordinates
 * control between these subpages. Should be added to RootPanel.
 * 
 * @author Claudia Exeler
 */
public class BasePage extends TabLayoutPanel {

  protected ResultsPage resultsPage;
  protected TabWrapper resultPageTabWrapper;
  protected RunConfigurationPage runConfigurationsPage;

  protected AlgorithmServiceAsync finderService;

  /**
   * Constructor. Initiates creation of subpages.
   */
  public BasePage() {
    super(1, Unit.CM);
    this.addStyleName("basePage");

    this.insert(new TabWrapper(new DataSourcesPage(this)), "Data Sources",
        Tabs.DATA_SOURCES.ordinal());
    this.insert(new TabWrapper(new AlgorithmsPage(this)), "Algorithms", Tabs.ALGORITHMS.ordinal());

    this.runConfigurationsPage = new RunConfigurationPage(this);
    this.insert(new TabWrapper(this.runConfigurationsPage), "Run Configuration",
        Tabs.RUN_CONFIGURATION.ordinal());

    this.resultsPage = new ResultsPage(this);
    this.resultPageTabWrapper = new TabWrapper(this.resultsPage);
    this.insert(this.resultPageTabWrapper, "Results", Tabs.RESULTS.ordinal());

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
   * Hand control from the Run Configuration to displaying Results. Start executing the algorithm
   * and fetch results at a regular interval.
   * 
   * @param executionService the service instance used for executing the algorithm
   * @param algorithmFileName the file name of the algorithm to execute
   * @param parameters the specification with set settings used to configure the algorithm
   */
  public void startExecutionAndResultPolling(ExecutionServiceAsync executionService,
      String algorithmFileName, List<ConfigurationSpecification> parameters) {

    // clear previous errors
    this.resultPageTabWrapper.clearErrors();

    String executionIdentifier = getExecutionIdetifier(algorithmFileName);

    // Create new tab with result table
    // ScrollPanel resultsTab = new ScrollPanel();
    ResultsTablePage resultsTableContent =
        new ResultsTablePage(executionService, executionIdentifier);
    resultsTableContent.setErrorReceiver(this.resultPageTabWrapper);
    executionService.executeAlgorithm(algorithmFileName, executionIdentifier, parameters,
        resultsTableContent.getCancelCallback());

    // Execute algorithm and start fetching results
    executionService.executeAlgorithm(algorithmFileName, executionIdentifier, parameters,
        resultsTableContent.getCancelCallback());
    resultsTableContent.startPolling();

    // Create new tab with visualizations of result
    ResultsVisualizationPage visualizationTab = new ResultsVisualizationPage();

    // remove the content from the result page and set the content to the new fetched result
    this.resultsPage.update(resultsTableContent, visualizationTab);
    this.selectTab(Tabs.RESULTS.ordinal());
  }

  /**
   * Generates a string that uniquely identifies an algorithm execution.
   * 
   * @param algorithmName the name of the algorithm being executed
   * @return a string consisting of the algorithmName and the current date and time
   */
  protected String getExecutionIdetifier(String algorithmName) {
    DateTimeFormat format = DateTimeFormat.getFormat("yyyy-MM-dd'T'HHmmss");
    return algorithmName + format.format(new Date());
  }

  /**
   * Generates a string representing all given data sources by concatenating their names. These are
   * used as titles for the result tabs.
   * 
   * @param dataSources the list of InputParameterDataSources to be descirbed
   * @return a String with the names of all given data source parameters
   */
  // private String getDataSourcesString(
  // List<ConfigurationSpecification> dataSources) {
  // String dataSourcesString = "";
  // for (ConfigurationSpecification dataSource : dataSources){
  // for (Object settingObject : dataSource.getSettings()) {
  // ConfigurationSettingDataSource setting = (ConfigurationSettingDataSource) settingObject;
  // dataSourcesString = dataSourcesString + setting.getValueAsString() + " - ";
  // }
  // }
  // return dataSourcesString;
  // }

  /**
   * Hand control from any page to Run Configurations, and pre-configure the latter with the
   * algorithm and/or data source.
   * 
   * @param algorithmName algorithm that shall be run
   * @param dataSource data source that shall be profiled
   */
  public void jumpToRunConfiguration(String algorithmName, ConfigurationSettingDataSource dataSource) {
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

  public enum Tabs {
    DATA_SOURCES, ALGORITHMS, RUN_CONFIGURATION, RESULTS, ABOUT
  }

}
