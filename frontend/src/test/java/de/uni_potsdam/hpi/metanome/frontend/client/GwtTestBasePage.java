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

package de.uni_potsdam.hpi.metanome.frontend.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TabLayoutPanel;

import de.uni_potsdam.hpi.metanome.algorithm_integration.AlgorithmConfigurationException;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSettingCsvFile;
import de.uni_potsdam.hpi.metanome.algorithm_integration.configuration.ConfigurationSpecificationCsvFile;
import de.uni_potsdam.hpi.metanome.frontend.client.BasePage.Tabs;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterCsvFileWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.parameter.InputParameterDataSourceWidget;
import de.uni_potsdam.hpi.metanome.frontend.client.runs.RunConfigurationPage;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FinderService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FinderServiceAsync;
import de.uni_potsdam.hpi.metanome.results_db.Algorithm;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * Tests related to the overall page.
 */
public class GwtTestBasePage extends GWTTestCase {

    /**
     * this must contain an algorithm and a data source that are currently available
     */
    private String algorithmName = "example_ucc_algorithm.jar";
    private String dataSourceName = "inputA.csv";
    LinkedList<Algorithm> algorithms = new LinkedList<Algorithm>();

    private BasePage testPage;

    /**
     * Test BasePage constructor.
     */
    @Test
    public void testNewBasePage() {
        //Execute
        testPage = new BasePage();

        //Check
        assertEquals(5, testPage.getWidgetCount());

        // -- Results page
        assertTrue(testPage.getWidget(Tabs.RESULTS.ordinal()) instanceof TabLayoutPanel);
    }

    @Test
    public void testAddAlgorithmsToRunConfigurations() {
        BasePage page = new BasePage();
        int itemCount = page.runConfigurationsPage.getJarChooser().getListItemCount();
        algorithms.add(new Algorithm("Algorithm 1"));
        algorithms.add(new Algorithm("Algorithm 2"));

        //Execute
        page.addAlgorithmsToRunConfigurations(algorithms);

        //Check
        assertEquals(itemCount + 2, page.runConfigurationsPage.getJarChooser().getListItemCount());
    }

    /**
     * Test control flow from Algorithms to Run configuration
     */
    @Test
    public void testJumpToRunConfigurationFromAlgorithm() {
        // Setup
        final BasePage page = new BasePage();
        algorithms.add(new Algorithm("file/name", "displayed name", "author"));
        final String algorithmName = algorithms.get(0).getName();

        page.addAlgorithmsToRunConfigurations(algorithms);

        AsyncCallback<List<Algorithm>> callback = new AsyncCallback<List<Algorithm>>() {
            @Override
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(List<Algorithm> result) {
                page.addAlgorithmsToRunConfigurations(result);

                //Execute
                page.jumpToRunConfiguration(algorithmName, null);

                //Check
                assertEquals(Tabs.RUN_CONFIGURATION.ordinal(), page.getSelectedIndex());
                assertEquals(algorithmName, ((RunConfigurationPage) page.getWidget(page.getSelectedIndex()))
                        .getCurrentlySelectedAlgorithm());

//				TODO Add testing to ensure the parameter table is shown
//				assertEquals(2, (((AlgorithmTab) page.getWidget(page.getSelectedIndex()))).getWidgetCount()); 
//				assertTrue((((AlgorithmTab) page.getWidget(page.getSelectedIndex()))).getWidget(1) instanceof ParameterTable); 

                finishTest();
            }
        };

        ((FinderServiceAsync) GWT.create(FinderService.class)).listAllAlgorithms(callback);

        delayTestFinish(5000);
    }

    /**
     * Test control flow from Data source to Run configuration
     *
     * @throws AlgorithmConfigurationException
     */
    @Test
    public void testJumpToRunConfigurationFromDataSource() throws AlgorithmConfigurationException {
        final BasePage page = new BasePage();
        final InputParameterDataSourceWidget dataSourceWidget = new InputParameterCsvFileWidget(
                new ConfigurationSpecificationCsvFile("test"));
        ConfigurationSettingCsvFile dataSource = new ConfigurationSettingCsvFile();
        dataSource.setFileName(dataSourceName);
        final ConfigurationSettingCsvFile finalDataSource = dataSource;
//		dataSourceWidget.setDataSource(dataSource);

        AsyncCallback<List<Algorithm>> callback = new AsyncCallback<List<Algorithm>>() {
            @Override
            public void onFailure(Throwable caught) {
                // TODO: Do something with errors.
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(List<Algorithm> result) {
                page.addAlgorithmsToRunConfigurations(result);

                //Execute
                page.jumpToRunConfiguration(null, finalDataSource);

                RunConfigurationPage runConfigPage = (RunConfigurationPage) page.getWidget(page.getSelectedIndex());

                //Check
                assertEquals(Tabs.RUN_CONFIGURATION.ordinal(), page.getSelectedIndex());
                assertEquals(finalDataSource.getValueAsString(), runConfigPage.primaryDataSource.getValueAsString());
                //TODO assert correct filtering

                finishTest();
            }
        };

        ((FinderServiceAsync) GWT.create(FinderService.class)).listAllAlgorithms(callback);

        delayTestFinish(5000);
    }

    @Override
    public String getModuleName() {
        return "de.uni_potsdam.hpi.metanome.frontend.Metanome";
    }

}
