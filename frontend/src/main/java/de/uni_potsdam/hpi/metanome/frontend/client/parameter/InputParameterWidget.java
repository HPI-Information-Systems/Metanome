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

package de.uni_potsdam.hpi.metanome.frontend.client.parameter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecification;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;

import java.util.ArrayList;
import java.util.List;

public abstract class InputParameterWidget extends VerticalPanel implements IsWidget {

  protected Button addButton;

  public InputParameterWidget(ConfigurationSpecification config) {
    super();

    this.setSpecification(config);

    if (this.getSpecification().getNumberOfValues()
        == ConfigurationSpecification.ARBITRARY_NUMBER_OF_VALUES) {
      this.setInputWidgets(new ArrayList<InputField>(1));
      this.addInputField(true);    //one default input field
      createAddOneButton();
    } else {
      this.setInputWidgets(new ArrayList<InputField>(this.getSpecification().getNumberOfValues()));
      for (int i = 0; i < this.getSpecification().getNumberOfValues(); i++) {
        this.addInputField(false);
      }
    }
  }

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
        addInputField(true);
      }
    });

    this.add(this.addButton);
  }

  protected abstract void addInputField(boolean optional);

  public abstract ConfigurationSpecification getUpdatedSpecification()
      throws InputValidationException;

  public boolean isDataSource() {
    return false;
  }

  public abstract List<? extends InputField> getInputWidgets();

  public abstract void setInputWidgets(List<? extends InputField> inputWidgetsList);

  public abstract ConfigurationSpecification getSpecification();

  public abstract void setSpecification(ConfigurationSpecification config);

}
