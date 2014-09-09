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

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

import de.metanome.backend.results_db.Algorithm;
import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;
import de.uni_potsdam.hpi.metanome.frontend.client.TabContent;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.services.AlgorithmService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.AlgorithmServiceAsync;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * UI element that incorporates structure and logic for displaying and adding algorithms.
 *
 * @author Claudia Exeler
 */
public class AlgorithmsPage extends FlowPanel implements TabContent {

  protected final AlgorithmServiceAsync algorithmService;
  protected final BasePage basePage;
  protected final FlexTable uccList;
  protected final FlexTable cuccList;
  protected final FlexTable fdList;
  protected final FlexTable indList;
  protected final FlexTable statsList;
  protected TabWrapper messageReceiver;
  protected AlgorithmEditForm editForm;

  public AlgorithmsPage(BasePage parent) {
    this.algorithmService = GWT.create(AlgorithmService.class);
    this.basePage = parent;

    this.add(new HTML("<h3>Unique Column Combinations</h3>"));
    this.uccList = new FlexTable();
    this.add(this.uccList);
    updateUccAlgorithms();

    this.add(new HTML("<h3>Conditional Unique Column Combinations</h3>"));
    this.cuccList = new FlexTable();
    this.add(this.cuccList);
    updateCuccAlgorithms();

    this.add(new HTML("<h3>Functional Dependencies</h3>"));
    this.fdList = new FlexTable();
    this.add(this.fdList);
    updateFdAlgorithms();

    this.add(new HTML("<h3>Inclusion Dependencies<//h3>"));
    this.indList = new FlexTable();
    this.add(this.indList);
    updateIndAlgorithms();

    this.add(new HTML("<h3>Basic Statistics</h3>"));
    this.statsList = new FlexTable();
    this.add(this.statsList);
    updateStatsAlgorithms();

    this.add(new HTML("<hr>"));
    this.add(new HTML("<h3>Add A New Algorithm</h3>"));
    this.editForm = new AlgorithmEditForm(this, this.messageReceiver);
    this.add(this.editForm);
  }

  /**
   * Request a list of available UCC algorithms and display them in the uccList
   */
  private void updateUccAlgorithms() {
    algorithmService.listUniqueColumnCombinationsAlgorithms(getRetrieveCallback(this.uccList));
  }

  /**
   * Request a list of available CUCC algorithms and display them in the cuccList
   */
  private void updateCuccAlgorithms() {
    algorithmService.listConditionalUniqueColumnCombinationsAlgorithms(
        getRetrieveCallback(this.cuccList));
  }

  /**
   * Request a list of available FD algorithms and display them in the fdList
   */
  private void updateFdAlgorithms() {
    algorithmService.listFunctionalDependencyAlgorithms(getRetrieveCallback(this.fdList));
  }

  /**
   * Request a list of available IND algorithms and display them in the indList
   */
  private void updateIndAlgorithms() {
    algorithmService.listInclusionDependencyAlgorithms(getRetrieveCallback(this.indList));
  }

  /**
   * Request a list of available Basic Statistics algorithms and display them in the statsList
   */
  private void updateStatsAlgorithms() {
    algorithmService.listBasicStatisticsAlgorithms(getRetrieveCallback(this.statsList));
  }

  /**
   * Adds each of the algorithms to the given table, including formatting and buttons.
   *
   * @param algorithms the algorithms to be displayed
   * @param table      the table to which the algorithms will be added
   */
  protected void addAlgorithmsToTable(List<Algorithm> algorithms, FlexTable table) {
    int row = table.getRowCount();
    Collections.sort(algorithms);

    for (final Algorithm algorithm : algorithms) {
      // Using the HTML title to associate an algorithm with each button.
      Button runButton = new Button("Run");
      runButton.setTitle(algorithm.getName());
      runButton.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          callRunConfiguration(((Button) event.getSource()).getTitle());
        }
      });

      Button deleteButton = new Button("Delete");
      deleteButton.setTitle(algorithm.getName());
      deleteButton.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          deleteAlgorithm(algorithm);
        }
      });

      table.setWidget(row, 0, new HTML("<b>" + algorithm.getName() + "</b>"));
      table.setText(row, 1, "Author: " + algorithm.getAuthor());
      table.setText(row, 2, "File: " + algorithm.getFileName());
      table.setWidget(row, 3, runButton);
      table.setWidget(row, 4, deleteButton);
      row++;
    }
  }

  protected void deleteAlgorithm(Algorithm algorithm) {
    final boolean cucc = algorithm.isCucc();
    final boolean fd = algorithm.isFd();
    final boolean ind = algorithm.isInd();
    final boolean basicStat = algorithm.isBasicStat();
    final boolean ucc = algorithm.isUcc();
    final String algorithmName = algorithm.getName();

    this.algorithmService.deleteAlgorithm(algorithm, new AsyncCallback<Void>() {
      @Override
      public void onFailure(Throwable throwable) {
        messageReceiver.addError("Could not delete algorithm: " + throwable.getMessage());
      }

      @Override
      public void onSuccess(Void aVoid) {
        basePage.removeAlgorithmFromRunConfigurations(algorithmName);
        if (cucc) {
          removeRow(cuccList, algorithmName);
        }
        if (fd) {
          removeRow(fdList, algorithmName);
        }
        if (ind) {
          removeRow(indList, algorithmName);
        }
        if (basicStat) {
          removeRow(statsList, algorithmName);
        }
        if (ucc) {
          removeRow(uccList, algorithmName);
        }
      }
    });
  }

  /**
   * Initiates a service call to add the given algorithm to the database.
   *
   * @param algorithm algorithm, which should be added to the database
   */
  public void callAddAlgorithm(final Algorithm algorithm) {
    algorithmService.addAlgorithm(algorithm, getAddCallback(algorithm));
  }

  /**
   * Initiates a redirect to the Run Configuration page, prefilled with the given algorithm
   *
   * @param algorithmName name of the algorithm that will be configured
   */
  protected void callRunConfiguration(String algorithmName) {
    basePage.switchToRunConfiguration(algorithmName, null);
  }


  /**
   * Constructs a callback that will add all results to the given table
   *
   * @param list Object that all returned elements will be added to
   * @return the desired callback instance
   */
  protected AsyncCallback<List<Algorithm>> getRetrieveCallback(final FlexTable list) {
    return new AsyncCallback<List<Algorithm>>() {
      public void onFailure(Throwable caught) {
        messageReceiver.addError(caught.getMessage());
        caught.printStackTrace();
      }

      public void onSuccess(List<Algorithm> result) {
        basePage.addAlgorithmsToRunConfigurations(result);
        addAlgorithmsToTable(result, list);
      }
    };
  }

  /**
   * Constructs a callback that will add the given algorithm to all matching tables
   *
   * @param algorithm the algorithm to add to the page
   * @return the desired callback instance
   */
  protected AsyncCallback<Void> getAddCallback(final Algorithm algorithm) {
    return new AsyncCallback<Void>() {

      @Override
      public void onFailure(Throwable caught) {
        messageReceiver.addError("Could not add the algorithm: " + caught.getMessage());
      }

      @Override
      public void onSuccess(Void result) {
        ArrayList<Algorithm> list = new ArrayList<Algorithm>();
        list.add(algorithm);
        basePage.addAlgorithmsToRunConfigurations(list);

        if (algorithm.isInd()) {
          addAlgorithmsToTable(list, indList);
        }
        if (algorithm.isFd()) {
          addAlgorithmsToTable(list, fdList);
        }
        if (algorithm.isUcc()) {
          addAlgorithmsToTable(list, uccList);
        }
        if (algorithm.isBasicStat()) {
          addAlgorithmsToTable(list, statsList);
        }
      }
    };
  }

  /**
   * Removes the row, which contains the given algorithm Name, from the given table
   *
   * @param table         the table
   * @param algorithmName the algorithm name
   */
  protected void removeRow(FlexTable table, String algorithmName) {
    int row = 0;
    while (row < table.getRowCount()) {
      // Check if the file name cell contains the given algorithm name
      if (((HTML) table.getWidget(row, 0)).getText().equals(algorithmName)) {
        table.removeRow(row);
        return;
      }
      row++;
    }
  }

  /**
   * (non-Javadoc)
   *
   * @see de.uni_potsdam.hpi.metanome.frontend.client.TabContent#setMessageReceiver(de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper)
   */
  @Override
  public void setMessageReceiver(TabWrapper tab) {
    this.editForm.messageReceiver = tab;
    this.messageReceiver = tab;
  }

}
