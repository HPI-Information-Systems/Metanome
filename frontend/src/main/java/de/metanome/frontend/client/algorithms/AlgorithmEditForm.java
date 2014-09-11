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

package de.metanome.frontend.client.algorithms;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.metanome.backend.results_db.Algorithm;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.input_fields.ListBoxInput;
import de.metanome.frontend.client.services.AlgorithmService;
import de.metanome.frontend.client.services.AlgorithmServiceAsync;

import java.util.Arrays;

/**
 * @author Claudia Exeler
 */
public class AlgorithmEditForm extends Grid {

  protected final AlgorithmsPage algorithmsPage;
  protected TabWrapper messageReceiver;

  protected ListBoxInput fileListBox = new ListBoxInput(false);
  protected TextBox nameTextBox = new TextBox();
  protected TextBox authorTextBox = new TextBox();
  protected TextArea descriptionTextArea = new TextArea();


  public AlgorithmEditForm(AlgorithmsPage parent, TabWrapper messageReceiver) {
    super(5, 2);

    this.algorithmsPage = parent;
    this.messageReceiver = messageReceiver;

    this.createFileListBox();
    this.setText(0, 0, "File Name");
    this.setWidget(0, 1, this.fileListBox);

    this.setText(1, 0, "Algorithm Name");
    this.setWidget(1, 1, this.nameTextBox);

    this.setText(2, 0, "Author");
    this.setWidget(2, 1, this.authorTextBox);

    this.descriptionTextArea.setVisibleLines(3);
    this.setText(3, 0, "Description");
    this.setWidget(3, 1, this.descriptionTextArea);

    this.setWidget(4, 1, (Widget) new Button("Save", new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        submit();
      }
    }));
  }

  /**
   * Find all available algorithms files and adds them to the list box.
   */
  private void createFileListBox() {
    AsyncCallback<String[]> callback = new AsyncCallback<String[]>() {
      @Override
      public void onFailure(Throwable throwable) {
        messageReceiver.addError(
            "Could not find any algorithms. Please add your algorithms to the algorithm folder.");
        throwable.printStackTrace();
      }

      @Override
      public void onSuccess(String[] strings) {
        fileListBox.setValues(Arrays.asList(strings));
      }
    };

    AlgorithmServiceAsync service = GWT.create(AlgorithmService.class);
    service.listAvailableAlgorithmFiles(callback);
  }

  /**
   * Add the algorithm to the list of algorithms and store the algorithm in the database.
   */
  protected void submit() {
    try {
      algorithmsPage.callAddAlgorithm(retrieveInputValues());
    } catch (InputValidationException e) {
      messageReceiver.addError(e.getMessage());
    }
  }

  /**
   * @return a new Algorithm instance with attribute values according to user input
   * @throws InputValidationException if the algorithm name is not set
   */
  protected Algorithm retrieveInputValues()
      throws InputValidationException {
    String algorithmFile = this.fileListBox.getSelectedValue();

    Algorithm algorithm = new Algorithm(algorithmFile);
    algorithm.setName(this.nameTextBox.getValue());
    algorithm.setAuthor(this.authorTextBox.getValue());
    algorithm.setDescription(this.descriptionTextArea.getValue());

    if (algorithm.getName().isEmpty()) {
      throw new InputValidationException("Your algorithm should have a name!");
    }

    return algorithm;
  }

}
