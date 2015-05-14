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
import de.metanome.frontend.client.services.ParameterRestService;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A UI Widget that allows to choose a JAR containing the algorithm to use
 *
 * @author Claudia Exeler
 */
public class AlgorithmChooser extends FlowPanel {

  protected Label label;
  protected ListBox algorithmListBox;
  protected ListBox categoryListBox;

  protected Map<String, Algorithm> algorithmMap = new HashMap<>();
  protected Map<String, List<String>> categoryMap = new HashMap<>();
  protected String currentCategory;

  protected ParameterRestService parameterService;
  protected TabWrapper messageReceiver;

  public AlgorithmChooser(List<Algorithm> algorithms, TabWrapper tabWrapper) {
    super();

    this.messageReceiver = tabWrapper;
    this.parameterService = GWT.create(ParameterRestService.class);

    this.label = new Label("Choose your algorithm:");
    this.add(label);

    this.algorithmListBox = new ListBox();
    this.categoryListBox = new ListBox(false);

    // Add all categories to the category list box
    initializeCategoryListBoxAndMap();

    if (algorithms != null) {
      for (Algorithm algorithm : algorithms) {
        this.addAlgorithm(algorithm);
      }
    }

    // Add all algorithms to the algorithms list box
    this.currentCategory = AlgorithmCategory.All.name();
    updateAlgorithmListBox();

    this.add(categoryListBox);
    this.add(algorithmListBox);

    this.algorithmListBox.addChangeHandler(new AlgorithmChooserChangeHandler());
    this.categoryListBox.addChangeHandler(new AlgorithmCategoryChangeHandler());
  }

  /**
   * Clears the algorithm list box and adds all algorithms of the current selected algorithm
   * category to it.
   */
  public void updateAlgorithmListBox() {
    this.algorithmListBox.clear();
    this.algorithmListBox.addItem("--");
    this.algorithmListBox.getElement().getFirstChildElement().setAttribute("disabled", "disabled");
    this.algorithmListBox.setSelectedIndex(0);

    for (String name : this.categoryMap.get(this.currentCategory)) {
      sortedInsert(name);
    }
  }

  /**
   * Add all categories to category list box.
   */
  private void initializeCategoryListBoxAndMap() {
    for (String c : AlgorithmCategory.getCategories()) {
      this.categoryListBox.addItem(c);
      this.categoryMap.put(c, new ArrayList<String>());
    }
  }

  /**
   * Specifies the action undertaken when a jar file is chosen.
   */
  public void submit() {
    String selectedValue = getSelectedAlgorithm();
    this.messageReceiver.clearErrors();

    MethodCallback<List<ConfigurationRequirement>> callback = getParameterCallback();

    // Make the call to the parameter service.
    callParameterService(algorithmMap.get(selectedValue).getFileName(), callback);
  }

  /**
   * Calls the service to retrieve parameters to be specified by the user and display corresponding
   * widget
   *
   * @param selectedValue the name of the selected algorithm
   * @param callback      callback object for RPC
   */
  public void callParameterService(String selectedValue,
                                   MethodCallback<List<ConfigurationRequirement>> callback) {
    parameterService.retrieveParameters(selectedValue, callback);
  }

  /**
   * Creates the callback for calling the parameter service.
   *
   * @return the callback
   */
  protected MethodCallback<List<ConfigurationRequirement>> getParameterCallback() {
    return new MethodCallback<List<ConfigurationRequirement>>() {
      public void onFailure(Method method, Throwable caught) {
        messageReceiver.addError(
            "Error while retrieving configuration requirements: " + method.getResponse().getText());
      }

      public void onSuccess(Method method, List<ConfigurationRequirement> result) {
        forwardParameters(result);
      }
    };
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
   * @return the number of items in the algorithm list box, that is, the number of available
   * algorithms in this JarChooser
   */
  public int getListItemCount() {
    return this.algorithmListBox.getItemCount();
  }

  /**
   * @return the value at the currently selected index
   */
  public String getSelectedAlgorithm() {
    return algorithmListBox.getValue(algorithmListBox.getSelectedIndex());
  }

  /**
   * Select the entry with the given value.
   *
   * @param algorithmName value to select
   * @throws IndexOutOfBoundsException if none of the entries have the given value.
   */
  public void setSelectedAlgorithm(String algorithmName) {
    resetListBoxes();

    for (int i = 0; i < algorithmListBox.getItemCount(); i++) {
      if (algorithmListBox.getValue(i).equals(algorithmName)) {
        this.algorithmListBox.setSelectedIndex(i);
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

    if (!this.algorithmMap.containsKey(name)) {
      this.algorithmMap.put(name, algorithm);

      this.categoryMap.get(AlgorithmCategory.All.name()).add(name);

      if (algorithm.isCucc()) {
        this.categoryMap.get(AlgorithmCategory.Conditional_Unique_Column_Combination.name())
            .add(name);
      }
      if (algorithm.isUcc()) {
        this.categoryMap.get(AlgorithmCategory.Unique_Column_Combinations.name()).add(name);
      }
      if (algorithm.isFd()) {
        this.categoryMap.get(AlgorithmCategory.Functional_Dependencies.name()).add(name);
      }
      if (algorithm.isOd()) {
        this.categoryMap.get(AlgorithmCategory.Order_Dependencies.name()).add(name);
      }
      if (algorithm.isInd()) {
        this.categoryMap.get(AlgorithmCategory.Inclusion_Dependencies.name()).add(name);
      }
      if (algorithm.isBasicStat()) {
        this.categoryMap.get(AlgorithmCategory.Basic_Statistics.name()).add(name);
      }
    }
  }

  /**
   * Removes the given name from the algorithm chooser.
   *
   * @param algorithmName the algorithm's name, which should be removed
   */
  public void removeAlgorithm(String algorithmName) {
    this.algorithmMap.remove(algorithmName);

    for (List<String> algorithms : this.categoryMap.values()) {
      for (Iterator<String> iterator = algorithms.iterator(); iterator.hasNext(); ) {
        String name = iterator.next();
        if (name.equals(algorithmName)) {
          iterator.remove();
        }
      }
    }

    updateAlgorithmListBox();
  }

  /**
   * Inserts a new item in alphabetical ordering into the algorithm list box, that is, after before
   * the first item that is lexicographically larger than the argument.
   *
   * @param name The value to be inserted.
   */
  private void sortedInsert(String name) {
    int insertIndex = 0;
    do {
      insertIndex++;
      if (insertIndex >= this.algorithmListBox.getItemCount()) {
        this.algorithmListBox.addItem(name);
        return;
      }
    } while (this.algorithmListBox.getValue(insertIndex).compareTo(name) < 0);
    this.algorithmListBox.insertItem(name, insertIndex);
  }

  /**
   * Filters the list of algorithms so that only those are displayed that would accept the given
   * data source
   *
   * @param dataSource the data source that shall be profiled / for which algorithms should be
   *                   filtered
   */
  public void filterForPrimaryDataSource(ConfigurationSettingDataSource dataSource) {
    resetListBoxes();

    for (Algorithm algorithm : this.algorithmMap.values()) {
      if (dataSource instanceof ConfigurationSettingDatabaseConnection &&
          !algorithm.isDatabaseConnection()) {
        removeAlgorithmFromListBox(algorithm.getName());
      } else if (dataSource instanceof ConfigurationSettingTableInput &&
                 !(algorithm.isTableInput() || algorithm.isRelationalInput())) {
        removeAlgorithmFromListBox(algorithm.getName());
      } else if (dataSource instanceof ConfigurationSettingFileInput &&
                 !(algorithm.isFileInput() || algorithm.isRelationalInput())) {
        removeAlgorithmFromListBox(algorithm.getName());
      }
    }

    this.algorithmListBox.setSelectedIndex(0);
  }

  /**
   * Removes the given algorithm from the algorithm list box.
   *
   * @param algorithmName the algorithm's name, which should be removed
   */
  private void removeAlgorithmFromListBox(String algorithmName) {
    int index = 0;
    while (index < this.algorithmListBox.getItemCount()) {
      if (this.algorithmListBox.getItemText(index).equals(algorithmName)) {
        this.algorithmListBox.removeItem(index);
        return;
      }
      index++;
    }
  }

  /**
   * Resets the list boxes. Sets the category to 'ALL' and adds all available algorithms to the
   * algorithm list box again.
   */
  public void resetListBoxes() {
    this.currentCategory = AlgorithmCategory.All.name();
    this.categoryListBox.setSelectedIndex(0);
    updateAlgorithmListBox();
  }

  /**
   * Sets the current selected category.
   *
   * @param currentCategory the current category
   */
  public void setCurrentCategory(String currentCategory) {
    this.currentCategory = currentCategory;
  }

  /**
   * Updates an algorithm.
   *
   * @param algorithm the algorithm
   * @param oldName   the old name of the algorithm
   */
  public void update(Algorithm algorithm, String oldName) {
    if (this.algorithmMap.containsKey(oldName)) {
      this.removeAlgorithm(oldName);
      this.addAlgorithm(algorithm);
      updateAlgorithmListBox();
    }
  }

  public void setMessageReceiver(TabWrapper receiver) {
    this.messageReceiver = receiver;
  }

  /**
   * Enum that holds all available algorithm categories.
   */
  public enum AlgorithmCategory {
    All,
    Unique_Column_Combinations,
    Conditional_Unique_Column_Combination,
    Functional_Dependencies,
    Order_Dependencies,
    Inclusion_Dependencies,
    Basic_Statistics;

    /**
     * @return a list of category names
     */
    public static List<String> getCategories() {
      List<String> categories = new ArrayList<>();
      for (AlgorithmCategory c : values()) {
        categories.add(c.name());
      }
      return categories;
    }
  }

}
