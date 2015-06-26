/*
 * Copyright 2015 by the Metanome project
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

package de.metanome.frontend.client.results;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;

import de.metanome.backend.results_db.Execution;
import de.metanome.backend.results_db.Result;
import de.metanome.backend.results_db.ResultType;
import de.metanome.frontend.client.TabContent;
import de.metanome.frontend.client.TabWrapper;

import java.util.ArrayList;
import java.util.List;


public class ResultsVisualizationPage extends FlowPanel implements TabContent {

  protected TabWrapper messageReceiver;

  public ResultsVisualizationPage() {
    Label label = new Label("There are no visualizations yet.");
    label.setStyleName("resultTable-caption");
    this.add(label);
    this.getElement().setId("visualizationTab");
  }


  /**
   * Adds a table for each result type
   *
   * @param execution the execution
   */
  public void addVisualisations(Execution execution) {
    this.clear();
    Image loadingIcon = new Image("ajax-loader.gif");
    this.add(loadingIcon);

    List<String> types = new ArrayList<>();
    for (Result result : execution.getResults()) {
      types.add(result.getType().getName());
    }
    addVisualizations(types, execution.getId());

    remove(loadingIcon);
  }

  /**
   * Adds a table for each result type
   *
   * @param types the names of the result types
   */
  public void addVisualizations(List<String> types, long executionID) {
    if (types.contains(ResultType.STAT.getName())) {
      Label label = new Label("There are no visualizations for Basic Statistic Results yet.");
      label.setStyleName("resultTable-caption");
      this.add(label);
    }

    if (types.contains(ResultType.UCC.getName())) {
      try {
        showUniqueColumnCombinationVisualization();
      } catch (Exception e) {
        Label label = new Label("There are no visualizations for Unique Column Combination Results yet.");
        label.setStyleName("resultTable-caption");
        this.add(label);
      }
    }

    if (types.contains(ResultType.CUCC.getName())) {
      Label label = new Label("There are no visualizations for Conditional Unique Column Combination Results yet.");
      label.setStyleName("resultTable-caption");
      this.add(label);
    }

    if (types.contains(ResultType.FD.getName())) {
      try {
        showFunctionalDependencyVisualization();
      } catch (Exception e) {
        Label label = new Label("There are no visualizations for Functional Dependency Results yet.");
        label.setStyleName("resultTable-caption");
        this.add(label);
      }
    }

    if (types.contains(ResultType.IND.getName())) {
      Label label = new Label("There are no visualizations for Inclusion Dependency Results yet.");
      label.setStyleName("resultTable-caption");
      this.add(label);
    }

    if (types.contains(ResultType.OD.getName())) {
      Label label = new Label("There are no visualizations for Order Dependency Results yet.");
      label.setStyleName("resultTable-caption");
      this.add(label);
    }
  }

  private void showFunctionalDependencyVisualization() {
    final Frame visualizationFrame = new Frame();
    final ListBox listBox = new ListBox();
    listBox.addItem("Sunburst", "ZoomableSunburst.html");
    listBox.addItem("Circle Packing", "CirclePacking.html");
    listBox.addItem("Prefix Tree", "PrefixTree.html");
    listBox.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent changeEvent) {
        int selectedIndex = listBox.getSelectedIndex();
        String selectedValue = listBox.getValue(selectedIndex);
        if(!visualizationFrame.getUrl().equals(selectedValue)){
          visualizationFrame.setUrl(selectedValue);
        }
      }
    });
    this.add(listBox);

    //set attributes needed to make sunburst appear in iFrame
    visualizationFrame.getElement().setAttribute("id", "visualizationFrame");
    visualizationFrame.setWidth("98%");
    visualizationFrame.setHeight("93%");
    this.add(visualizationFrame);
    visualizationFrame.setUrl("ZoomableSunburst.html");

  }

  private void showUniqueColumnCombinationVisualization() {
    this.getElement().setId("uccVisualizationTab");
    final Frame visualizationFrame = new Frame();
    final ListBox listBox = new ListBox();
    listBox.addItem("uccPlotter", "uccPlotter.html");
    listBox.addItem("uccClusterBubbles", "clusterBubbles.html");
    listBox.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent changeEvent) {
        int selectedIndex = listBox.getSelectedIndex();
        String selectedValue = listBox.getValue(selectedIndex);
        if(!visualizationFrame.getUrl().equals(selectedValue)){
          visualizationFrame.setUrl(selectedValue);
        }
      }
    });
    this.add(listBox);

    //set attributes needed to make sunburst appear in iFrame
    visualizationFrame.getElement().setAttribute("id", "visualizationFrame");
    visualizationFrame.setWidth("98%");
    visualizationFrame.setHeight("93%");
    this.add(visualizationFrame);
    visualizationFrame.setUrl("uccPlotter.html");
  }

  @Override
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
  }
}

