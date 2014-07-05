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

import java.util.Collections;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;
import de.uni_potsdam.hpi.metanome.frontend.client.TabContent;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.services.AlgorithmService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.AlgorithmServiceAsync;
import de.uni_potsdam.hpi.metanome.results_db.Algorithm;

/**
 * UI element that incorporates structure and logic for displaying and adding algorithms.
 * 
 * @author Claudia Exeler
 */
public class AlgorithmsPage extends VerticalPanel implements TabContent {

  protected final AlgorithmServiceAsync finderService;
  protected final BasePage basePage;
  private final FlexTable uccList;
  private final FlexTable fdList;
  private final FlexTable indList;
  private final FlexTable statsList;
  protected TabWrapper errorReceiver;

  public AlgorithmsPage(BasePage parent) {
    this.setWidth("100%");
    this.setSpacing(5);

    this.finderService = GWT.create(AlgorithmService.class);
    this.basePage = parent;

    this.add(new HTML("<h3>Unique Column Combinations</h3>"));
    this.uccList = new FlexTable();
    this.add(this.uccList);
    listAndAddUccAlgorithms();

    this.add(new HTML("<h3>Functional Dependencies</h3>"));
    this.fdList = new FlexTable();
    this.add(this.fdList);
    listFdAlgorithms();

    this.add(new HTML("<h3>Inclusion Dependencies<//h3>"));
    this.indList = new FlexTable();
    this.add(this.indList);
    listIndAlgorithms();

    this.add(new HTML("<h3>Basic Statistics</h3>"));
    this.statsList = new FlexTable();
    this.add(this.statsList);
    listStatsAlgorithms();

    this.add(new HTML("<hr>"));
    this.add(new AlgorithmEditForm(this));
    // Label temporaryAddContent = new Label();
    // temporaryAddContent
    // .setText("To add a new algorithm, put its jar in the designated folder and provide details in this form:");
    // this.add(temporaryAddContent);
  }

  /**
   * Request a list of available UCC algorithms and display them in the uccList
   */
  private void listAndAddUccAlgorithms() {
    finderService.listUniqueColumnCombinationsAlgorithms(getCallback(this.uccList));
  }

  /**
   * Request a list of available FD algorithms and display them in the fdList
   */
  private void listFdAlgorithms() {
    finderService.listFunctionalDependencyAlgorithms(getCallback(this.fdList));
  }

  /**
   * Request a list of available IND algorithms and display them in the indList
   */
  private void listIndAlgorithms() {
    finderService.listInclusionDependencyAlgorithms(getCallback(this.indList));
  }

  /**
   * Request a list of available Basic Statistics algorithms and display them in the statsList
   */
  private void listStatsAlgorithms() {
    finderService.listBasicStatisticsAlgorithms(getCallback(this.statsList));
  }

  /**
   * Constructs a callback that will add all results to the given table
   * 
   * @param list Object that all returned elements will be added to
   * @return the desired callback instance
   */
  protected AsyncCallback<List<Algorithm>> getCallback(final FlexTable list) {
    return new AsyncCallback<List<Algorithm>>() {
      public void onFailure(Throwable caught) {
        errorReceiver.addError(caught.getMessage());
        caught.printStackTrace();
      }

      public void onSuccess(List<Algorithm> result) {
        basePage.addAlgorithmsToRunConfigurations(result);
        addAlgorithmsToList(result, list);
      }
    };
  }

  /**
   * Adds each of the algorithms to the given table, including formatting and buttons.
   * 
   * @param algorithms the algorithms to be displayed
   * @param list the table to which the algoithms will be added
   */
  protected void addAlgorithmsToList(List<Algorithm> algorithms, FlexTable list) {
    int row = list.getRowCount();
    Collections.sort(algorithms);
    for (Algorithm algorithm : algorithms) {
      // Using the HTML title to associate an algorithm with each button.
      Button runButton = new Button("Run");
      runButton.setTitle(algorithm.getName());
      runButton.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
          callRunConfiguration(((Button) event.getSource()).getTitle());
        }
      });

      list.setHTML(row, 0, "<b>" + algorithm.getName() + "</b>");
      list.setText(row, 1, "Author: " + algorithm.getAuthor());
      list.setText(row, 2, "File: " + algorithm.getFileName());
      list.setWidget(row, 3, runButton);
      row++;
    }
  }

  /**
   * Initiates a redirect to the Run Configuration page, prefilled with the given algorithm
   * 
   * @param algorithmName name of the algorithm that will be configured
   */
  protected void callRunConfiguration(String algorithmName) {
    basePage.jumpToRunConfiguration(algorithmName, null);
  }

  /**
   * @param retrieveInputValues
   */
  public void callAddAlgorithm(Algorithm algorithm) {
    // TODO Auto-generated method stub

  }


  /*
   * (non-Javadoc)
   * 
   * @see
   * de.uni_potsdam.hpi.metanome.frontend.client.TabContent#setErrorReceiver(de.uni_potsdam.hpi.
   * metanome.frontend.client.TabWrapper)
   */
  @Override
  public void setErrorReceiver(TabWrapper tab) {
    this.errorReceiver = tab;
  }

}
