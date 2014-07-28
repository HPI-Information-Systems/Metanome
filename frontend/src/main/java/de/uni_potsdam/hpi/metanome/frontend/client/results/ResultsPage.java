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

package de.uni_potsdam.hpi.metanome.frontend.client.results;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.TabLayoutPanel;

import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;
import de.uni_potsdam.hpi.metanome.frontend.client.TabContent;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;

/**
 * Tab that contains widgets displaying execution results, i.e. tables, visualizations etc.
 * Currently, these widgets are organized in another tab panel.
 * 
 * @author Claudia Exeler
 */
public class ResultsPage extends TabLayoutPanel implements TabContent {

  protected final BasePage basePage;
  protected TabWrapper errorReceiver;

  /**
   * Constructs the tab, creating a full height {@link TabLayoutPanel} with 1cm headers.
   * 
   * @param parent the page that this element will be attached to
   */
  public ResultsPage(BasePage parent) {
    super(1, Unit.CM);
    this.basePage = parent;

    this.setHeight("100%");
  }

  /* (non-Javadoc)
   * @see de.uni_potsdam.hpi.metanome.frontend.client.TabContent#setErrorReceiver(de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper)
   */
  @Override
  public void setErrorReceiver(TabWrapper tab) {
    this.errorReceiver = tab;
    tab.addStyleName("fullSize");
  }

  /**
   * Adds the Tabs for results and visualization.
   *
   * @param resultsTab the results tab
   * @param visualizationTab the visualization tab
   */
  public void update(ResultsTablePage resultsTab, ResultsVisualizationPage visualizationTab) {
    this.clear();
    this.add(resultsTab, "Table");
    this.add(visualizationTab, "Visualization");
  }
}
