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

package de.metanome.frontend.client.runs;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.backend.results_db.Algorithm;
import de.metanome.frontend.client.BasePage;
import de.metanome.frontend.client.TabContent;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.FilePathHelper;
import de.metanome.frontend.client.parameter.ParameterTable;
import de.metanome.frontend.client.services.AlgorithmExecutionRestService;

import java.util.List;

/**
 * The Run Configuration page allow to specify all parameters for an algorithm execution: The
 * algorithm itself is chosen through a JarChooser widget, and the ParameterTable allows to specify
 * algorithm specific parameters. The page can be referenced (and switched to) by other pages with
 * pre-set values. Executing an algorithm navigates to the corresponding Results page.
 */
public class RunConfigurationPage extends DockLayoutPanel implements TabContent {

  public ConfigurationSettingDataSource primaryDataSource;
  public ParameterTable parameterTable;
  protected BasePage basePage;
  protected TabWrapper messageReceiver;
  protected AlgorithmChooser algorithmChooser;
  protected Label primaryDataSourceLabel;
  protected AlgorithmExecutionRestService executionService;


  /**
   * Initializes ExecutionService and registers given algorithms. However, more algorithms can be
   * registered whenever they become available, through {@link RunConfigurationPage#addAlgorithms(java.util.List)}
   *
   * @param basePage the base page this sub page
   */
  public RunConfigurationPage(BasePage basePage) {
    super(Style.Unit.EM);
    this.basePage = basePage;

    FlowPanel panel = new FlowPanel();
    Button refreshButton = new Button("Refresh");
    refreshButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        algorithmChooser.resetListBoxes();
        primaryDataSourceLabel.setText("");
        removeParameterTable();
      }
    });
    panel.add(refreshButton);

    this.addNorth(panel, 3);

    this.primaryDataSourceLabel = new Label();
    this.addNorth(this.primaryDataSourceLabel, 2);

    this.algorithmChooser = new AlgorithmChooser(null, new TabWrapper());
    this.addNorth(this.algorithmChooser, 4);

    this.executionService = com.google.gwt.core.client.GWT.create(AlgorithmExecutionRestService.class);
  }


  /**
   * Adds a widget for user's parameter input to the tab. The content of the tab depends on the
   * requested parameters.
   *
   * @param paramList list of required parameters
   */
  public void addParameterTable(List<ConfigurationRequirement> paramList) {
    removeParameterTable();
    parameterTable = new ParameterTable(paramList, primaryDataSource, this.messageReceiver);
    this.addNorth(parameterTable, 40);
  }

  /**
   * Method to add more algorithms after construction.
   *
   * @param algorithms a list of algorithms to be added
   */
  public void addAlgorithms(List<Algorithm> algorithms) {
    for (Algorithm algorithm : algorithms) {
      this.algorithmChooser.addAlgorithm(algorithm);
    }
    this.algorithmChooser.updateAlgorithmListBox();
  }

  /**
   * Removes a algorithm from the algorithm chooser.
   *
   * @param algorithmName the algorithm name, which should be removed
   */
  public void removeAlgorithm(String algorithmName) {
    this.algorithmChooser.removeAlgorithm(algorithmName);
  }

  /**
   * Select the given algorithm on the underlying JarChooser.
   *
   * @param algorithmName the value to select
   */
  public void selectAlgorithm(String algorithmName) {
    this.messageReceiver.clearErrors();
    this.messageReceiver.clearInfos();

    this.algorithmChooser.setSelectedAlgorithm(algorithmName);
    this.algorithmChooser.submit();
  }

  /**
   * @return the name of the algorithm that is currently selected on this page's JarChooser
   */
  public String getCurrentlySelectedAlgorithm() {
    return this.algorithmChooser.getSelectedAlgorithm();
  }

  /**
   * Sets the data source that should be profiled ("Primary Data Source"), displays its value in a
   * label, and triggers the jarChooser to filter for applicable algorithms.
   *
   * @param dataSource the data source to profile
   */
  public void setPrimaryDataSource(ConfigurationSettingDataSource dataSource) {
    this.messageReceiver.clearErrors();
    this.messageReceiver.clearInfos();

    String dataSourceName = dataSource.getValueAsString();
    if (dataSource instanceof ConfigurationSettingFileInput)
      dataSourceName = FilePathHelper.getFileName(dataSourceName);

    this.primaryDataSource = dataSource;
    this.primaryDataSourceLabel.setText(
        "This should filter for algorithms applicable on " + dataSourceName);
    removeParameterTable();

    this.algorithmChooser.filterForPrimaryDataSource(dataSource);
  }

  /**
   * Remove the parameterTable from UI if it was present
   */
  public void removeParameterTable() {
    if (parameterTable != null) {
      this.remove(parameterTable);
    }
  }

  /**
   * Execute the currently selected algorithm and switch to results page.
   *
   * @param parameters    parameters to use for the algorithm execution
   * @param configuration the configuration to start executing with
   */
  public void startExecution(List<ConfigurationRequirement> parameters,
                             List<ConfigurationRequirement> configuration) {

    final String algorithmName = getCurrentlySelectedAlgorithm();
    final Algorithm algorithm = getAlgorithm(algorithmName);
    parameters.addAll(configuration);

    basePage.startAlgorithmExecution(executionService, algorithm, parameters);
  }

  /**
   * Forwards the update call to the parameter table.
   */
  public void updateDataSources() {
    if (this.parameterTable != null) {
      this.parameterTable.updateDataSources();
    }
  }

  // Getters & Setters
  public AlgorithmChooser getJarChooser() {
    return algorithmChooser;
  }


  /* (non-Javadoc)
   * @see de.metanome.frontend.client.TabContent#setMessageReceiver(de.metanome.frontend.client.TabWrapper)
   */
  @Override
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
    this.algorithmChooser.setMessageReceiver(tab);
  }

  /**
   * Returns the algorithm with the given name
   */
  private Algorithm getAlgorithm(String name) {
    return this.algorithmChooser.algorithmMap.get(name);
  }

  /**
   * Updates an algorithm on the algorithm chooser.
   * Removes the old algorithm and add the new one.
   *
   * @param algorithm the algorithm name, which was updated
   * @param oldName   the old algorithm name
   */
  public void updateAlgorithm(Algorithm algorithm, String oldName) {
    this.algorithmChooser.update(algorithm, oldName);
    this.algorithmChooser.updateAlgorithmListBox();
  }
}
