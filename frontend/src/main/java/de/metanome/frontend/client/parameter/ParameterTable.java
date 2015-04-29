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

package de.metanome.frontend.client.parameter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.algorithm_integration.configuration.ConfigurationSettingDataSource;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.input_fields.InputField;
import de.metanome.frontend.client.runs.RunConfigurationPage;

import java.util.LinkedList;
import java.util.List;

/**
 * Depending on the currently selected algorithm this widget displays the needed parameters. The
 * parameters can be configured and the algorithm can be executed.
 */
public class ParameterTable extends FlowPanel {

  protected List<InputParameterDataSourceWidget> dataSourceChildWidgets = new LinkedList<>();
  private List<InputParameterWidget> childWidgets = new LinkedList<>();
  private TabWrapper messageReceiver;
  private RadioButton rbCache;
  private RadioButton rbDisk;
  private RadioButton rbCount;
  private TextBox memoryTextBox;
  protected FlexTable table;

  /**
   * Creates a ParameterTable for user input for the given parameters. Prompt and type specific
   * input field are created for each parameter, and a button added at the bottom that triggers
   * algorithm execution.
   *
   * @param paramList         the list of parameters asked for by the algorithm.
   * @param primaryDataSource the primary data source
   * @param messageReceiver   to send errors to
   */
  public ParameterTable(List<ConfigurationRequirement> paramList,
                        ConfigurationSettingDataSource primaryDataSource,
                        TabWrapper messageReceiver) {
    super();
    this.messageReceiver = messageReceiver;

    this.table = new FlexTable();
    int row = 0;
    for (ConfigurationRequirement param : paramList) {
      if (param.isRequired()) {
        this.table.setText(row, 0, param.getIdentifier() + " *");
      } else {
        this.table.setText(row, 0, param.getIdentifier());
      }

      InputParameterWidget currentWidget = WidgetFactory.buildWidget(param, messageReceiver);
      this.table.setWidget(row, 1, currentWidget);

      if (currentWidget.isDataSource()) {
        InputParameterDataSourceWidget dataSourceWidget =
            (InputParameterDataSourceWidget) currentWidget;
        if (dataSourceWidget.accepts(primaryDataSource)) {
          try {
            dataSourceWidget.setDataSource(primaryDataSource);
          } catch (AlgorithmConfigurationException e) {
            this.messageReceiver.addError("Could not select " + primaryDataSource.getValueAsString()
                                          + " as data source. Please choose one of the available ones below.");
          }
        }
        this.dataSourceChildWidgets.add(dataSourceWidget);
      } else {
        this.childWidgets.add(currentWidget);
      }
      row++;
    }

    this.table.setText(row, 0, "* are required fields");

    // Radio Buttons to select way of result handling
    FlexTable radioButtonTable = new FlexTable();
    Label label = new Label("How to handle results?");
    this.rbCache =
        new RadioButton("resultReceiver",
                        "Cache result and write it to disk when the algorithm is finished.");
    this.rbDisk = new RadioButton("resultReceiver", "Write result immediately to disk.");
    this.rbCount = new RadioButton("resultReceiver", "Just count the results.");
    this.rbCache.setValue(true);
    radioButtonTable.setWidget(1, 0, label);
    radioButtonTable.setWidget(2, 0, this.rbCache);
    radioButtonTable.setWidget(3, 0, this.rbDisk);
    radioButtonTable.setWidget(4, 0, this.rbCount);

    // Input field for memory
    FlowPanel memoryPanel = new FlowPanel();
    memoryPanel.add(new Label("Memory (in MB):"));
    this.memoryTextBox = new TextBox();
    memoryPanel.add(memoryTextBox);

    // run button
    Button executeButton = new Button("Run");
    executeButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        submit();
      }
    });

    // add some css
    this.table.addStyleName("space_bottom");
    radioButtonTable.addStyleName("space_bottom");
    memoryPanel.addStyleName("space_bottom");

    this.add(this.table);
    this.add(radioButtonTable);
    this.add(memoryPanel);
    this.add(executeButton);
  }

  /**
   * When parameter values are submitted, their values are set and used to call the execution
   * service corresponding to the current tab.
   */
  public void submit() {
    Boolean cacheResult = this.rbCache.getValue();
    Boolean writeResult = this.rbDisk.getValue();
    Boolean countResult = this.rbCount.getValue();
    String memory = this.memoryTextBox.getValue();

    try {
      List<ConfigurationRequirement> parameters = getConfigurationSpecificationsWithValues();
      List<ConfigurationRequirement> dataSources =
          getConfigurationSpecificationDataSourcesWithValues();
      getAlgorithmTab()
          .startExecution(parameters, dataSources, cacheResult, writeResult, countResult, memory);
    } catch (InputValidationException | AlgorithmConfigurationException e) {
      this.messageReceiver.clearErrors();
      // mark required input fields
      for (InputParameterWidget widget : this.childWidgets) {
        markAsRequired(widget);
      }
      for (InputParameterWidget widget : this.dataSourceChildWidgets) {
        markAsRequired(widget);
      }
      this.messageReceiver.addError("You have to fill out all required input fields:");
      this.messageReceiver.addError(e.getMessage());
    }
  }

  private void markAsRequired(InputParameterWidget widget) {
    for (InputField inputField : widget.getInputWidgets()) {
      if (inputField.isRequired) {
        inputField.addStyleName("required");
      }
    }
  }

  /**
   * Iterates over the child widgets that represent data sources and retrieves their user input.
   *
   * @return The list of {@link InputParameterDataSourceWidget}s of this ParameterTable with their
   * user-set values.
   * @throws de.metanome.frontend.client.helpers.InputValidationException if the child widgets
   *                                                                      cannot validate their
   *                                                                      input
   */
  public List<ConfigurationRequirement> getConfigurationSpecificationDataSourcesWithValues()
      throws InputValidationException, AlgorithmConfigurationException {
    LinkedList<ConfigurationRequirement> parameterList = new LinkedList<>();

    for (InputParameterDataSourceWidget childWidget : this.dataSourceChildWidgets) {
      parameterList.add(childWidget.getUpdatedSpecification());
    }

    return parameterList;
  }

  /**
   * Iterates over the child widgets and retrieves their user input.
   *
   * @return The list of ConfigurationSpecifications of this ParameterTable with their user-set
   * values.
   * @throws de.metanome.frontend.client.helpers.InputValidationException if the child widgets
   *                                                                      cannot validate their
   *                                                                      input
   */
  public List<ConfigurationRequirement> getConfigurationSpecificationsWithValues()
      throws InputValidationException, AlgorithmConfigurationException {
    LinkedList<ConfigurationRequirement> parameterList = new LinkedList<>();

    for (InputParameterWidget childWidget : this.childWidgets) {
      parameterList.add(childWidget.getUpdatedSpecification());
    }

    return parameterList;
  }

  /**
   * Gives access to this ParameterTable's {@link InputParameterWidget} child widget whose
   * underlying {@link de.metanome.algorithm_integration.configuration.ConfigurationRequirement} has
   * the given identifier.
   *
   * @param identifier The identifier of the ConfigurationSpecification of the wanted widget.
   * @return This parameter's child widgets that corresponds to the given identifier, or null if
   * such a child does not exist.
   */
  public InputParameterWidget getInputParameterWidget(String identifier) {
    for (InputParameterWidget w : this.childWidgets) {
      if (w.getSpecification().getIdentifier().equals(identifier)) {
        return w;
      }
    }
    for (InputParameterWidget w : this.dataSourceChildWidgets) {
      if (w.getSpecification().getIdentifier().equals(identifier)) {
        return w;
      }
    }
    return null;
  }

  /**
   * Forwards the update call to the data source widgets.
   */
  public void updateDataSources() {
    for (InputParameterDataSourceWidget w : this.dataSourceChildWidgets) {
      w.update();
    }
  }

  /**
   * The AlgorithmTabs implement algorithm type specific methods, which can be called via the
   * AlgorithmTab's interface.
   *
   * @return the parent AlgorithmTab
   */
  private RunConfigurationPage getAlgorithmTab() {
    return (RunConfigurationPage) this.getParent();
  }
}
