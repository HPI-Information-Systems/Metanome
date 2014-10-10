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

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDatabaseConnection;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingFileInput;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingTableInput;
import de.metanome.backend.results_db.Algorithm;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.services.ParameterService;
import de.metanome.frontend.client.services.ParameterServiceAsync;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A UI Widget that allows to choose a JAR containing the algorithm to use
 *
 * @author Claudia Exeler
 */
public class AlgorithmChooser extends FlowPanel {

  protected Label label;
  protected ListBox listbox;

  protected Map<String, Algorithm> algorithms = new HashMap<String, Algorithm>();

  protected ParameterServiceAsync parameterService;
  protected TabWrapper messageReceiver;

  public AlgorithmChooser(List<Algorithm> algorithms, TabWrapper tabWrapper) {
    super();

    this.messageReceiver = tabWrapper;
    this.parameterService = GWT.create(ParameterService.class);

    this.label = new Label("Choose your algorithm:");
    this.add(label);

    this.listbox = new ListBox();

    //unselectable default entry
    this.listbox.addItem("--");
    this.listbox.getElement().getFirstChildElement().setAttribute("disabled", "disabled");
    this.listbox.setSelectedIndex(0);

    if (algorithms != null) {
      for (Algorithm algorithm : algorithms) {
        this.addAlgorithm(algorithm);
      }
    }
    this.add(listbox);
    this.listbox.addChangeHandler(new AlgorithmChooserChangeHandler());
  }

  /**
   * Specifies the action undertaken when a jar file is chosen.
   */
  public void submit() {
    String selectedValue = getSelectedAlgorithm();
    this.messageReceiver.clearErrors();

    AsyncCallback<List<ConfigurationRequirement>> callback =
        new AsyncCallback<List<ConfigurationRequirement>>() {
          public void onFailure(Throwable caught) {
            messageReceiver.addError("Error while retrieving configuration requirements.");
          }

          public void onSuccess(List<ConfigurationRequirement> result) {
            forwardParameters(result);
          }
        };

    // Make the call to the parameter service.
    callParameterService(algorithms.get(selectedValue).getFileName(), callback);
  }

  /**
   * Calls the service to retrieve parameters to be specified by the user and display corresponding
   * widget
   *
   * @param selectedValue the name of the selected algorithm
   * @param callback      callback object for RPC
   */
  public void callParameterService(String selectedValue,
                                   AsyncCallback<List<ConfigurationRequirement>> callback) {
    parameterService.retrieveParameters(selectedValue, callback);
  }

  /**
   * Handles the incoming list of parameters by adding a ParameterTable to the corresponding tab.
   *
   * @param paramList list of parameters necessary for the chosen algorithm
   */
  protected void forwardParameters(List<ConfigurationRequirement> paramList) {
    ((RunConfigurationPage) this.getParent()).addParameterTable(paramList);
  }

  /**
   * @return the number of items in the listbox, that is, the number of available algorithms in this
   * JarChooser
   */
  public int getListItemCount() {
    return this.listbox.getItemCount();
  }

  /**
   * @return the value at the currently selected index
   */
  public String getSelectedAlgorithm() {
    return listbox.getValue(listbox.getSelectedIndex());
  }

  /**
   * Select the entry with the given value.
   *
   * @param algorithmName value to select
   * @throws IndexOutOfBoundsException if none of the entries have the given value.
   */
  public void setSelectedAlgorithm(String algorithmName) {
    for (int i = 0; i < listbox.getItemCount(); i++) {
      if (listbox.getValue(i).equals(algorithmName)) {
        this.listbox.setSelectedIndex(i);
        return;
      }
    }

    throw new IndexOutOfBoundsException(
        "The value " + algorithmName + " is not available in this jarChooser.");
  }

  /**
   * Add another entry, but only if it is not yet present. (Using algorithm's name as key)
   *
   * @param algorithm The algorithm to be added
   */
  public void addAlgorithm(Algorithm algorithm) {
    String name = algorithm.getName();
    if (name == null) {
      name = algorithm.getFileName();
    }

    if (!this.algorithms.containsKey(name)) {
      this.algorithms.put(name, algorithm);
      sortedInsert(name);
    }
  }

  /**
   * Removes the given name from the algorithm chooser.
   *
   * @param algorithmName the algorithm's name, which should be removed
   */
  public void removeAlgorithm(String algorithmName) {
    this.algorithms.remove(algorithmName);
    removeAlgorithmFromListBox(algorithmName);
  }

  /**
   * Inserts a new item in alphabetical ordering, that is, after before the first item that is
   * lexicographically larger than the argument.
   *
   * @param name The value to be inserted.
   */
  private void sortedInsert(String name) {
    int insertIndex = 0;
    do {
      insertIndex++;
      if (insertIndex >= this.listbox.getItemCount()) {
        this.listbox.addItem(name);
        return;
      }
    } while (this.listbox.getValue(insertIndex).compareTo(name) < 0);
    this.listbox.insertItem(name, insertIndex);
  }

  /**
   * Filters the list of algorithms so that only those are displayed that would accept the given
   * data source
   *
   * @param dataSource the data source that shall be profiled / for which algorithms should be
   *                   filtered
   */
  public void filterForPrimaryDataSource(ConfigurationSettingDataSource dataSource) {
    for (Algorithm algorithm : this.algorithms.values()) {
      if (dataSource instanceof ConfigurationSettingDatabaseConnection &&
          !algorithm.isDatabaseConnection()) {
        removeAlgorithmFromListBox(algorithm.getName());
      }
      else if (dataSource instanceof ConfigurationSettingTableInput &&
               !(algorithm.isTableInput() || algorithm.isRelationalInput())) {
        removeAlgorithmFromListBox(algorithm.getName());
      }
      else if (dataSource instanceof ConfigurationSettingFileInput &&
               !(algorithm.isFileInput() || algorithm.isRelationalInput())) {
        removeAlgorithmFromListBox(algorithm.getName());
      }
    }

    this.listbox.setSelectedIndex(0);
  }

  /**
   * Removes the given algorithm from the list box.
   * @param algorithmName the algorithm's name, which should be removed
   */
  private void removeAlgorithmFromListBox(String algorithmName) {
    int index = 0;
    while (index < this.listbox.getItemCount()) {
      if (this.listbox.getItemText(index).equals(algorithmName)) {
        this.listbox.removeItem(index);
        return;
      }
      index++;
    }
  }

  /**
   * Resets the list box. Adds all available algorithms to the list box again.
   */
  public void resetListBox() {
    this.listbox.clear();

    this.listbox.addItem("--");
    this.listbox.getElement().getFirstChildElement().setAttribute("disabled", "disabled");
    this.listbox.setSelectedIndex(0);

    for (Algorithm algorithm : this.algorithms.values()) {
      String name = algorithm.getName();
      if (name == null) {
        name = algorithm.getFileName();
      }
      sortedInsert(name);
    }
  }

  public void setMessageReceiver(TabWrapper receiver) {
    this.messageReceiver = receiver;
  }


}
