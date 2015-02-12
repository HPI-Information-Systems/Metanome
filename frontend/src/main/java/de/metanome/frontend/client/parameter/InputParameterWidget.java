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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;

import de.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.metanome.algorithm_integration.configuration.ConfigurationRequirement;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.input_fields.InputField;

import java.util.ArrayList;
import java.util.List;

public abstract class InputParameterWidget extends FlowPanel implements IsWidget {

  protected Button addButton;
  protected Boolean required;

  /**
   * Creates a widget with the given number of input fields and sets the configuration specification
   * and the tab wrapper.
   *
   * @param config  the configuration specification
   * @param wrapper the tab wrapper
   */
  public InputParameterWidget(ConfigurationRequirement config, TabWrapper wrapper) {
    super();

    this.setMessageReceiver(wrapper);
    this.setSpecification(config);
    this.required = config.isRequired();

    int numberOfSettings = this.getSpecification().getNumberOfSettings();

    if (numberOfSettings == ConfigurationRequirement.ARBITRARY_NUMBER_OF_VALUES) {
      this.setInputWidgets(new ArrayList<InputField>(1));
      this.addInputField(true, this.required, 0);    //one default input field
      createAddOneButton();
    } else {
      this.setInputWidgets(new ArrayList<InputField>(numberOfSettings));
      for (int i = 0; i < numberOfSettings; i++) {
        this.addInputField(false, this.required, i);
      }
    }
  }

  /**
   * Removes the given input field
   *
   * @param w the input field, which should be removed
   * @return true, if input field could be removed, false otherwise
   */
  public boolean removeField(InputField w) {
    this.getInputWidgets().remove(w);
    return super.remove(w);
  }

  /**
   * Adds a button beneath all input fields that, when clicked, adds another input field (checkbox,
   * textbox, or whatever is appropriate for the implementing class.
   */
  protected void createAddOneButton() {
    this.addButton = new Button("Add");
    this.addButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        addInputField(true, required, -1);
      }
    });

    this.add(this.addButton);
  }

  /**
   * Adds an input field to the widget.
   *
   * @param optional specifies, weather a a remove button will be rendered
   * @param index    specifies the index of the corresponding setting
   */
  protected abstract void addInputField(boolean optional, boolean required, int index);

  /**
   * Gets the configuration specification and updates the current configuration specification.
   *
   * @return the updated specification of the input
   * @throws InputValidationException if the specification is invalid
   */
  public abstract ConfigurationRequirement getUpdatedSpecification()
      throws InputValidationException, AlgorithmConfigurationException;

  /**
   * @return true, if the input field represents a data source input, false otherwise
   */
  public boolean isDataSource() {
    return false;
  }

  /**
   * @return a list of all input widgets
   */
  public abstract List<? extends InputField> getInputWidgets();

  /**
   * Sets the list of all input widgets to the given list
   *
   * @param inputWidgetsList list of input widgets
   */
  public abstract void setInputWidgets(List<? extends InputField> inputWidgetsList);

  /**
   * @return the current configuration specification
   */
  public abstract ConfigurationRequirement getSpecification();

  /**
   * Sets the configuration specification
   *
   * @param config the configuration specification, which should be set
   */
  public abstract void setSpecification(ConfigurationRequirement config);

  /**
   * Sets the message receiver
   *
   * @param messageReceiver the tab wrapper, which should be set
   */
  public abstract void setMessageReceiver(TabWrapper messageReceiver);
}
