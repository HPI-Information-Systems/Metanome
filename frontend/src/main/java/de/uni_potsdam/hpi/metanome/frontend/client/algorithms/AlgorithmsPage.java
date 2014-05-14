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
import com.google.gwt.user.client.ui.*;

import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;
import de.uni_potsdam.hpi.metanome.frontend.client.TabContent;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FinderService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FinderServiceAsync;

public class AlgorithmsPage extends VerticalPanel implements TabContent {

    protected final FinderServiceAsync finderService;
    protected final BasePage basePage;
    protected TabWrapper errorReceiver;
    
    private final FlexTable uccList;
    private final FlexTable fdList;
    private final FlexTable indList;
    private final FlexTable statsList;

    public AlgorithmsPage(BasePage parent) {
        this.setWidth("100%");
        this.setSpacing(5);

        this.finderService = GWT.create(FinderService.class);
        this.basePage = parent;

        this.add(new HTML("<b>Unique Column Combinations</b>"));
        this.uccList = new FlexTable();
        this.add(this.uccList);
        listAndAddUccAlgorithms();

        this.add(new HTML("<hr><b>Functional Dependencies</b>"));
        this.fdList = new FlexTable();
        this.add(this.fdList);
        listFdAlgorithms();

        this.add(new HTML("<hr><b>Inclusion Dependencies</b>"));
        this.indList = new FlexTable();
        this.add(this.indList);
        listIndAlgorithms();

        this.add(new HTML("<hr><b>Basic Statistics</b>"));
        this.statsList = new FlexTable();
        this.add(this.statsList);
        listStatsAlgorithms();

        this.add(new HTML("<hr>"));
        Label temporaryAddContent = new Label();
        temporaryAddContent.setText("To add a new algorithm, put its jar in the designated folder.");
        this.add(temporaryAddContent);
    }

    /**
     * Request a list of available UCC algorithms and display them in the uccList
     */
    private void listAndAddUccAlgorithms() {
        finderService.listUniqueColumnCombinationsAlgorithmFileNames(getCallback(this.uccList));
    }

    /**
     * Request a list of available FD algorithms and display them in the fdList
     */
    private void listFdAlgorithms() {
        finderService.listFunctionalDependencyAlgorithmFileNames(getCallback(this.fdList));
    }

    /**
     * Request a list of available IND algorithms and  display them in the indList
     */
    private void listIndAlgorithms() {
        finderService.listInclusionDependencyAlgorithmFileNames(getCallback(this.indList));
    }

    /**
     * Request a list of available Basic Statistics algorithms and display them in the statsList
     */
    private void listStatsAlgorithms() {
        finderService.listBasicStatisticsAlgorithmFileNames(getCallback(this.statsList));
    }

    /**
     * Constructs a callback that will add all results to the given
     *
     * @param list Object that all returned elements will be added to
     * @return the desired callback instance
     */
    protected AsyncCallback<String[]> getCallback(final FlexTable list) {
        return new AsyncCallback<String[]>() {
            public void onFailure(Throwable caught) {
                errorReceiver.addError(caught.getMessage());
                caught.printStackTrace();
            }

            public void onSuccess(String[] result) {
                addAlgorithmsToList(result, list);
                basePage.addAlgorithmsToRunConfigurations(result);
            }
        };
    }

    protected void addAlgorithmsToList(String[] algorithmNames, FlexTable list) {
        int row = list.getRowCount();
        for (String algorithmName : algorithmNames) {
            //Using the HTML title to associate an algorithm with each button.
            Button runButton = new Button("Run");
            runButton.setTitle(algorithmName);
            runButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    jumpToRunConfiguration(((Button) event.getSource()).getTitle());
                }
            });

            list.setText(row, 0, algorithmName);
            list.setWidget(row, 1, runButton);
            row++;
        }
    }

    protected void jumpToRunConfiguration(String algorithmName) {
        basePage.jumpToRunConfiguration(algorithmName, null);
    }

	/* (non-Javadoc)
	 * @see de.uni_potsdam.hpi.metanome.frontend.client.TabContent#setErrorReceiver(de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper)
	 */
	@Override
	public void setErrorReceiver(TabWrapper tab) {
		this.errorReceiver = tab;
	}
}
