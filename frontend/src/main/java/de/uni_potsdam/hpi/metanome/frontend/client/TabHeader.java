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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import de.uni_potsdam.hpi.metanome.frontend.client.results.ResultsPage;

/**
 * Widget to be put into the tab bars of result tabs. Includes the algorithm name as well
 * as a close button.
 *
 * @author Claudia Exeler
 */
public class TabHeader extends HorizontalPanel {

    private final Panel tab;
    private final ResultsPage tabPanel;

    /**
     * Constructor.
     *
     * @param algorithmName the name of the algorithm or whatever should be displayed as tab title
     * @param tabContent    the panel holding the corresponding tab's contents
     * @param resultsPage   the TabLayoutPanel where resultsTab will be added to
     */
    public TabHeader(String algorithmName, Panel tabContent, ResultsPage resultsPage) {
        this.tab = tabContent;
        this.tabPanel = resultsPage;

        this.setSpacing(3);
        this.add(new Label(algorithmName));

        Button button = new Button("x");
        button.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                tabPanel.remove(tab);

            }
        });
        this.add(button);
    }
}
