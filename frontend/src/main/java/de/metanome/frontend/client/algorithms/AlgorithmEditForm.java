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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import de.metanome.backend.results_db.Algorithm;
import de.metanome.backend.results_db.AlgorithmObj;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.helpers.InputValidationException;
import de.metanome.frontend.client.input_fields.ListBoxInput;
import de.metanome.frontend.client.services.AlgorithmRestService;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.List;

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

  private List<String> algorithmsInDatabase;
  private List<String> algorithmsOnStorage;

  public AlgorithmEditForm(AlgorithmsPage parent, TabWrapper messageReceiver) {
    super(5, 3);

    this.algorithmsPage = parent;
    this.messageReceiver = messageReceiver;

    Button refreshButton = new Button("Refresh");
    refreshButton.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        updateFileListBox();
      }
    });

    this.updateFileListBox();
    this.setText(0, 0, "File Name");
    this.setWidget(0, 1, this.fileListBox);
    this.setWidget(0, 2, refreshButton);

    this.setText(1, 0, "Algorithm Name");
    this.setWidget(1, 1, this.nameTextBox);
    this.setText(1, 2, "(Should be unique)");

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
  public void updateFileListBox() {
    this.fileListBox.clear();
    this.fileListBox.addValue("--");
    this.fileListBox.disableFirstEntry();

    this.algorithmsInDatabase = new ArrayList<>();
    this.algorithmsOnStorage = new ArrayList<>();

    // Add available CSV files
    MethodCallback<List<String>> storageCallback = getStorageCallback();
    MethodCallback<List<AlgorithmObj>> databaseCallback = getDatabaseCallback();


    AlgorithmRestService restService = com.google.gwt.core.client.GWT.create(AlgorithmRestService.class);
    restService.listAvailableAlgorithmFiles(storageCallback);
    restService.listAllAlgorithms(databaseCallback);
  }

  /**
   * Gets all algorithms in the database.
   * The list box should only contain those algorithms, which are not yet stored in the database.
   * @return the callback
   */
  private MethodCallback<List<AlgorithmObj>> getDatabaseCallback() {
    return new MethodCallback<List<AlgorithmObj>>() {
      @Override
      public void onFailure(Method method, Throwable caught) {
        System.out.println(caught.getMessage());
      }

      @Override
      public void onSuccess(Method method, List<AlgorithmObj> result) {
        List<String> algorithmNames = new ArrayList<>();
        List<Algorithm> l = convert(result);

        for (Algorithm algorithm : l)
          algorithmsInDatabase.add(algorithm.getFileName());

        for (String name : algorithmsOnStorage) {
          if (!algorithmsInDatabase.contains(name)) {
            algorithmNames.add(name);
          }
        }

        fileListBox.setValues(algorithmNames);
      }
    };
  }

  /**
   * Gets all algorithms on storage.
   * The list box should only contain those algorithms, which are not yet stored in the database.
   * @return the callback
   */
  private MethodCallback<List<String>> getStorageCallback() {
    return new MethodCallback<List<String>>() {
      @Override
      public void onFailure(Method method, Throwable throwable) {
        messageReceiver.addError(
            "Could not find any algorithms. Please add your algorithms to the algorithm folder.");
        throwable.printStackTrace();
      }

      @Override
      public void onSuccess(Method method, List<String> result) {
        List<String> algorithmNames = new ArrayList<>();

        if (result.size() == 0) {
          messageReceiver
              .addError("Could not find any algorithms. Please add your algorithms to the algorithm folder.");
          return;
        }

        for (String name : result) {
          if (algorithmsInDatabase.size() > 0 && !algorithmsInDatabase.contains(name)) {
            algorithmNames.add(name);
          }

          algorithmsOnStorage.add(name);
        }

        fileListBox.setValues(algorithmNames);
      }
    };
  }

  /**
   * Add the algorithm to the list of algorithms and store the algorithm in the database.
   */
  protected void submit() {
    messageReceiver.clearErrors();
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

  /**
   * Reset all input fields.
   */
  protected void reset() {
    this.fileListBox.reset();
    this.nameTextBox.setText("");
    this.authorTextBox.setText("");
    this.descriptionTextArea.setText("");
  }

  protected List<Algorithm> convert(List<AlgorithmObj> l) {
    List<Algorithm> algorithms = new ArrayList<>();
    for (AlgorithmObj a : l) {
      Algorithm algorithm = new Algorithm(a.getFileName());
      algorithm.setFd(a.isFd());
      algorithm.setInd(a.isInd());
      algorithm.setUcc(a.isUcc());
      algorithm.setCucc(a.isCucc());
      algorithm.setOd(a.isOd());
      algorithm.setBasicStat(a.isBasicStat());
      algorithm.setDatabaseConnection(a.isDatabaseConnection());
      algorithm.setFileInput(a.isFileInput());
      algorithm.setRelationalInput(a.isRelationalInput());
      algorithm.setTableInput(a.isTableInput());
      algorithm.setAuthor(a.getAuthor());
      algorithm.setName(a.getName());
      algorithm.setDescription(a.getDescription());
      algorithms.add(algorithm);
    }
    return algorithms;
  }
}
