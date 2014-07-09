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

package de.uni_potsdam.hpi.metanome.frontend.client.algorithms;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.results_db.Algorithm;

/**
 * @author Claudia Exeler
 * 
 */
public class AlgorithmEditForm extends Grid {

  protected final AlgorithmsPage algorithmsPage;
  protected final TabWrapper errorReceiver;

  protected TextBox fileNameTextBox = new TextBox();
  protected TextBox nameTextBox = new TextBox();
  protected TextBox authorTextBox = new TextBox();
  protected TextBox descriptionTextBox = new TextBox();
  protected CheckBox indCheckBox = new CheckBox("Inclusion Dependencies");
  protected CheckBox fdCheckBox = new CheckBox("Functional Dependencies");
  protected CheckBox uccCheckBox = new CheckBox("Unique Column Combinations");
  protected CheckBox basicStatsCheckBox = new CheckBox("Basic Statistics");


  public AlgorithmEditForm(AlgorithmsPage parent, TabWrapper errorReceiver) {
    super(6, 3);

    this.algorithmsPage = parent;
    this.errorReceiver = errorReceiver;

    this.setWidget(0, 2, new Label(
        "First, make your JAR file available by putting it in the algorithms folder."));
    this.setText(0, 0, "File Name");
    this.setWidget(0, 1, this.fileNameTextBox);

    this.setText(1, 0, "Algorithm Name");
    this.setWidget(1, 1, this.nameTextBox);

    this.setText(2, 0, "Author");
    this.setWidget(2, 1, this.authorTextBox);

    this.setText(3, 0, "Description");
    this.setWidget(3, 1, this.descriptionTextBox);

    this.setText(4, 0, "Features");
    VerticalPanel p = new VerticalPanel();
    p.add(this.indCheckBox);
    p.add(this.fdCheckBox);
    p.add(this.uccCheckBox);
    p.add(this.basicStatsCheckBox);
    this.setWidget(4, 1, p);

    this.setWidget(5, 1, (Widget) new Button("Save", new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        submit();
      }
    }));
  }

  /**
   * 
   */
  protected void submit() {
    try {
      algorithmsPage.callAddAlgorithm(retrieveInputValues());
    } catch (InputValidationException e) {
      errorReceiver.addError(e.getMessage());
    }
  }

  /**
   * @return a new Algorithm instance with attribute values according to user input
   */
  protected Algorithm retrieveInputValues() throws InputValidationException {
    String fileName = this.fileNameTextBox.getValue();
    if (fileName.matches("\\s*")) { // TODO #160 more advanced validation, or replace with dropdown
      throw new InputValidationException("You must enter a filename!");
    }

    Algorithm algorithm = new Algorithm(fileName);
    algorithm.setName(this.nameTextBox.getValue());
    algorithm.setAuthor(this.authorTextBox.getValue());
    algorithm.setDescription(this.descriptionTextBox.getValue());

    algorithm.setInd(this.indCheckBox.getValue());
    algorithm.setFd(this.fdCheckBox.getValue());
    algorithm.setUcc(this.uccCheckBox.getValue());
    algorithm.setBasicStat(this.basicStatsCheckBox.getValue());

    if (!algorithm.isInd() && !algorithm.isFd() && !algorithm.isUcc() && !algorithm.isBasicStat()) {
      throw new InputValidationException(
          "Your algorithm must implement at least one algorithm type.");
    }

    return algorithm;
  }
}
