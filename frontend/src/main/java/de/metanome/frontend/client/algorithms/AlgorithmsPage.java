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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

import de.metanome.backend.results_db.Algorithm;
import de.metanome.frontend.client.BasePage;
import de.metanome.frontend.client.TabContent;
import de.metanome.frontend.client.TabWrapper;
import de.metanome.frontend.client.services.AlgorithmRestService;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * UI element that incorporates structure and logic for displaying and adding algorithms.
 *
 * @author Claudia Exeler
 */
public class AlgorithmsPage extends FlowPanel implements TabContent {

  protected final AlgorithmRestService restService;
  protected final BasePage basePage;
  protected final FlexTable uccList;
  protected final FlexTable cuccList;
  protected final FlexTable fdList;
  protected final FlexTable indList;
  protected final FlexTable odList;
  protected final FlexTable statsList;
  protected TabWrapper messageReceiver;
  protected AlgorithmEditForm editForm;

  public AlgorithmsPage(BasePage parent) {
    this.restService = com.google.gwt.core.client.GWT.create(AlgorithmRestService.class);

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

    this.add(new HTML("<h3>Order Dependencies</h3>"));
    this.odList = new FlexTable();
    this.add(this.odList);
    updateOdAlgorithms();
    
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
    this.restService.listUniqueColumnCombinationsAlgorithms(getRetrieveCallback(this.uccList));
  }

  /**
   * Request a list of available CUCC algorithms and display them in the cuccList
   */
  private void updateCuccAlgorithms() {
    this.restService.listConditionalUniqueColumnCombinationsAlgorithms(
        getRetrieveCallback(this.cuccList));
  }

  /**
   * Request a list of available FD algorithms and display them in the fdList
   */
  private void updateFdAlgorithms() {
    this.restService.listFunctionalDependencyAlgorithms(getRetrieveCallback(this.fdList));
  }

  /**
   * Request a list of available IND algorithms and display them in the indList
   */
  private void updateIndAlgorithms() {
    this.restService.listInclusionDependencyAlgorithms(getRetrieveCallback(this.indList));
  }
  
  /**
   * Request a list of available OD algorithms and display them in the odList
   */
  private void updateOdAlgorithms() {
    this.restService.listOrderDependencyAlgorithms(getRetrieveCallback(this.odList));
  }

  /**
   * Request a list of available Basic Statistics algorithms and display them in the statsList
   */
  private void updateStatsAlgorithms() {
    this.restService.listBasicStatisticsAlgorithms(getRetrieveCallback(this.statsList));
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
      final AlgorithmsPage page = this;
      deleteButton.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          new AlgorithmDeleteDialogBox(page, algorithm).show();
        }
      });

      table.setWidget(row, 0, new HTML("<b>" + algorithm.getName() + "</b>"));
      table.setText(row, 1, "Author: " + algorithm.getAuthor());
      table.setText(row, 2, "File: " + algorithm.getFileName());
      table.setText(row, 3, "Description: " + algorithm.getDescription());
      table.setWidget(row, 4, runButton);
      table.setWidget(row, 5, deleteButton);
      row++;
    }
  }

  protected void deleteAlgorithm(Algorithm algorithm) {
    final boolean cucc = algorithm.isCucc();
    final boolean fd = algorithm.isFd();
    final boolean ind = algorithm.isInd();
    final boolean basicStat = algorithm.isBasicStat();
    final boolean ucc = algorithm.isUcc();
    final boolean od = algorithm.isOd();
    final String algorithmName = algorithm.getName();

    this.restService.deleteAlgorithm(algorithm.getFileName(), new MethodCallback<Void>() {
       @Override
       public void onFailure(Method method, Throwable throwable) {
         messageReceiver.addErrorHTML("Could not delete algorithm: " + throwable.getMessage());
       }

       @Override
       public void onSuccess(Method method, Void aVoid) {
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
         if (od) {
           removeRow(odList, algorithmName);
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
    this.restService.addAlgorithm(algorithm, getAddCallback());
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
  protected MethodCallback<List<Algorithm>> getRetrieveCallback(final FlexTable list) {
    return new MethodCallback<List<Algorithm>>() {
      @Override
      public void onFailure(Method method, Throwable throwable) {
        messageReceiver.addError(throwable.getMessage());
      }

      @Override
      public void onSuccess(Method method, List<Algorithm> algorithms) {
        basePage.addAlgorithmsToRunConfigurations(algorithms);
        addAlgorithmsToTable(algorithms, list);
      }
    };
  }

  /**
   * Constructs a callback that will add the given algorithm to all matching tables
   *
   * @return the desired callback instance
   */
  protected MethodCallback<Algorithm> getAddCallback() {
    return new MethodCallback<Algorithm>() {
      @Override
      public void onFailure(Method method, Throwable throwable) {
        messageReceiver.addErrorHTML("Could not add the algorithm: " + throwable.getMessage());
      }

      @Override
      public void onSuccess(Method method, Algorithm algorithm) {
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
        if (algorithm.isOd()) {
          addAlgorithmsToTable(list, odList);
        }
        if (algorithm.isBasicStat()) {
          addAlgorithmsToTable(list, statsList);
        }

        editForm.updateFileListBox();
        editForm.reset();
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
   * @see de.metanome.frontend.client.TabContent#setMessageReceiver(de.metanome.frontend.client.TabWrapper)
   */
  @Override
  public void setMessageReceiver(TabWrapper tab) {
    this.editForm.messageReceiver = tab;
    this.messageReceiver = tab;
  }

}
