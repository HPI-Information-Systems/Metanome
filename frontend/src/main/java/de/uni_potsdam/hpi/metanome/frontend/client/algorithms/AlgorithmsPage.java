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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;
import de.uni_potsdam.hpi.metanome.frontend.client.TabContent;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.services.AlgorithmService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.AlgorithmServiceAsync;
import de.uni_potsdam.hpi.metanome.results_db.Algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * UI element that incorporates structure and logic for displaying and adding algorithms.
 * 
 * @author Claudia Exeler
 */
public class AlgorithmsPage extends VerticalPanel implements TabContent {

  protected final AlgorithmServiceAsync algorithmService;
  protected final BasePage basePage;
  protected final FlexTable uccList;
  protected final FlexTable cuccList;
  protected final FlexTable fdList;
  protected final FlexTable indList;
  protected final FlexTable statsList;
  protected TabWrapper errorReceiver;

  public AlgorithmsPage(BasePage parent) {
    this.setWidth("100%");
    this.setSpacing(5);

    this.algorithmService = GWT.create(AlgorithmService.class);
    this.basePage = parent;

    this.add(new HTML("<h3>Unique Column Combinations</h3>"));
    this.uccList = new FlexTable();
    this.add(this.uccList);
    listAndAddUccAlgorithms();

    this.add(new HTML("<h3>Conditional Unique Column Combinations</h3>"));
    this.cuccList = new FlexTable();
    this.add(this.cuccList);
    listAndAddCuccAlgorithms();

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
    this.add(new HTML("<h3>Add A New Algorithm</h3>"));
    this.add(new AlgorithmEditForm(this, this.errorReceiver));
  }

  /**
   * Request a list of available UCC algorithms and display them in the uccList
   */
  private void listAndAddUccAlgorithms() {
    algorithmService.listUniqueColumnCombinationsAlgorithms(getRetrieveCallback(this.uccList));
  }

  /**
   * Request a list of available CUCC algorithms and display them in the cuccList
   */
  private void listAndAddCuccAlgorithms() {
    algorithmService.listConditionalUniqueColumnCombinationsAlgorithms(
        getRetrieveCallback(this.cuccList));
  }

  /**
   * Request a list of available FD algorithms and display them in the fdList
   */
  private void listFdAlgorithms() {
    algorithmService.listFunctionalDependencyAlgorithms(getRetrieveCallback(this.fdList));
  }

  /**
   * Request a list of available IND algorithms and display them in the indList
   */
  private void listIndAlgorithms() {
    algorithmService.listInclusionDependencyAlgorithms(getRetrieveCallback(this.indList));
  }

  /**
   * Request a list of available Basic Statistics algorithms and display them in the statsList
   */
  private void listStatsAlgorithms() {
    algorithmService.listBasicStatisticsAlgorithms(getRetrieveCallback(this.statsList));
  }

  /**
   * Adds each of the algorithms to the given table, including formatting and buttons.
   * 
   * @param algorithms the algorithms to be displayed
   * @param list the table to which the algoithms will be added
   */
  protected void addAlgorithms(List<Algorithm> algorithms, FlexTable list) {
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
   * Initiates a service call to add the given algorithm to the database.
   * 
   * @param algorithm
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
    basePage.jumpToRunConfiguration(algorithmName, null);
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
        errorReceiver.addError(caught.getMessage());
        caught.printStackTrace();
      }

      public void onSuccess(List<Algorithm> result) {
        basePage.addAlgorithmsToRunConfigurations(result);
        addAlgorithms(result, list);
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
        errorReceiver.addError("Could not add the algorithm: " + caught.getMessage());
      }

      @Override
      public void onSuccess(Void result) {
        ArrayList<Algorithm> list = new ArrayList<Algorithm>();
        list.add(algorithm);
        basePage.addAlgorithmsToRunConfigurations(list);

        if (algorithm.isInd()) {
          addAlgorithms(list, indList);
        }
        if (algorithm.isFd()) {
          addAlgorithms(list, fdList);
        }
        if (algorithm.isUcc()) {
          addAlgorithms(list, uccList);
        }
        if (algorithm.isBasicStat()) {
          addAlgorithms(list, statsList);
        }
      }
    };
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
