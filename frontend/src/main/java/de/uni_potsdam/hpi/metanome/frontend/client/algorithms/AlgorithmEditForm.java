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

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.BasicStatisticsAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.FunctionalDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.InclusionDependencyAlgorithm;
import de.uni_potsdam.hpi.metanome.algorithm_integration.algorithm_types.UniqueColumnCombinationsAlgorithm;
import de.uni_potsdam.hpi.metanome.results_db.Algorithm;

/**
 * @author Claudia Exeler
 * 
 */
public class AlgorithmEditForm extends FlowPanel {

  private final AlgorithmsPage algorithmsPage;

  protected TextBox fileNameTextBox = new TextBox();
  protected TextBox nameTextBox = new TextBox();
  protected TextBox authorTextBox = new TextBox();
  protected TextBox descriptionTextBox = new TextBox();
  protected CheckBox indCheckBox = new CheckBox("Inclusion Dependencies");
  protected CheckBox fdCheckBox = new CheckBox("Functional Dependencies");
  protected CheckBox uccCheckBox = new CheckBox("Unique Column Combinations");
  protected CheckBox basicStatsCheckBox = new CheckBox("Basic Statistics");

  public AlgorithmEditForm(AlgorithmsPage parent) {
    this.algorithmsPage = parent;

    this.add(new HTML("<h3>Add A New Algorithm</h3>"));
    this.add(new Label(
        "First, make your JAR file available by putting it in the algorithms folder."));
    this.add(createGrid());
    this.add(createSubmitButton());
  }


  private Grid createGrid() {
    Grid grid = new Grid(5, 2);

    grid.setText(0, 0, "File Name");
    grid.setWidget(0, 1, this.fileNameTextBox);

    grid.setText(1, 0, "Algorithm Name");
    grid.setWidget(1, 1, this.nameTextBox);

    grid.setText(2, 0, "Author");
    grid.setWidget(2, 1, this.authorTextBox);

    grid.setText(3, 0, "Description");
    grid.setWidget(3, 1, this.descriptionTextBox);

    grid.setText(4, 0, "Features");
    VerticalPanel p = new VerticalPanel();
    p.add(this.indCheckBox);
    p.add(this.fdCheckBox);
    p.add(this.uccCheckBox);
    p.add(this.basicStatsCheckBox);
    grid.setWidget(4, 1, p);

    return grid;
  }

  /**
   * @return
   */
  private Widget createSubmitButton() {
    return new Button("Save", new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        algorithmsPage.callAddAlgorithm(retrieveInputValues());

      }
    });
  }


  /**
   * @return a new Algorithm instance with attribute values according to user input
   */
  protected Algorithm retrieveInputValues() {
    // TODO input validation

    Set<Class<?>> algorithmInterfaces = new HashSet<Class<?>>();
    if (this.indCheckBox.getValue())
      algorithmInterfaces.add(InclusionDependencyAlgorithm.class);
    if (this.fdCheckBox.getValue())
      algorithmInterfaces.add(FunctionalDependencyAlgorithm.class);
    if (this.uccCheckBox.getValue())
      algorithmInterfaces.add(UniqueColumnCombinationsAlgorithm.class);
    if (this.basicStatsCheckBox.getValue())
      algorithmInterfaces.add(BasicStatisticsAlgorithm.class);

    return new Algorithm(this.fileNameTextBox.getValue(), this.nameTextBox.getValue(),
        this.authorTextBox.getValue(), this.descriptionTextBox.getValue(), algorithmInterfaces);
  }

}
