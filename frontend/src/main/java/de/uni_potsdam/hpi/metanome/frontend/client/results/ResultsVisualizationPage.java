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

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;


/**
 * @author Tanja Bergmann
 */
public class ResultsVisualizationPage extends StackLayoutPanel {

  public ResultsVisualizationPage() {
    super(Style.Unit.EM);
    this.setSize("1800px", "650px");

    SimplePanel indPanel = new ScrollPanel();
    SimplePanel uccPanel = new ScrollPanel();
    SimplePanel fdPanel = new ScrollPanel();

    prepareFDVisualization(fdPanel);
    drawInclusionDependencies(indPanel.getElement());
    drawUniqueColumnCombinations(uccPanel.getElement());

    this.add(indPanel, new HTML("Inclusion Dependencies"), 2);
    this.add(uccPanel, new HTML("Unique Column Combinations"), 2);
    this.add(fdPanel, new HTML("Functional Dependencies"), 2);
  }

  // call d3 with dom element & data
  private native void drawInclusionDependencies(Element div)/*-{
      $wnd.draw_ind(div);
  }-*/;

  // call d3 with dom element & data
  private native void drawUniqueColumnCombinations(Element div)/*-{
      $wnd.draw_ucc(div);
  }-*/;


  private void prepareFDVisualization(SimplePanel fdPanel) {
    HTML svgHtml = new HTML("<div id='svgDiv'/>");
    HTML mainHtml = new HTML("<div id='mainDiv'/>");
    HTML bpgHtml = new HTML("<div id='bpg'/>");
    HTML
        toolTipHtml =
        new HTML(
            "<div id='toolTip' class='tooltip' style='width:250px; height:120px; position:absolute;s' />");
    HTML header1Html = new HTML("<div id='header1' class='header2' />");
    HTML header2Html = new HTML("<div id='header2' class='header3' />");
    HTML headHtml = new HTML("<div id='head' class='header' />");

    DOM.appendChild(fdPanel.getElement(), bpgHtml.getElement());
    DOM.appendChild(bpgHtml.getElement(), mainHtml.getElement());
    DOM.appendChild(mainHtml.getElement(), svgHtml.getElement());
    DOM.appendChild(bpgHtml.getElement(), toolTipHtml.getElement());
    DOM.appendChild(toolTipHtml.getElement(), header1Html.getElement());
    DOM.appendChild(toolTipHtml.getElement(), headHtml.getElement());
    DOM.appendChild(toolTipHtml.getElement(), header2Html.getElement());

    drawFunctionalDependencies(svgHtml.getElement(), mainHtml.getElement(), bpgHtml.getElement(),
                               toolTipHtml.getElement(), header1Html.getElement(),
                               header2Html.getElement(), headHtml.getElement());
  }


  // call d3 with dom element & data
  private native void drawFunctionalDependencies(Element svgDiv, Element mainDiv, Element bpgDiv,
                                                 Element toolTipDiv, Element header1Div,
                                                 Element header2Div, Element headDiv)/*-{
      $wnd.draw_fd(svgDiv, mainDiv, bpgDiv, toolTipDiv, header1Div, header2Div, headDiv, $doc);
  }-*/;
}
