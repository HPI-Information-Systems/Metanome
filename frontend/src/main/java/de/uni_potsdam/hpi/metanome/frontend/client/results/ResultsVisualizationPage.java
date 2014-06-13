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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;
import de.uni_potsdam.hpi.metanome.frontend.client.TabContent;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;


/**
 * @author Tanja Bergmann
 *
 */
public class ResultsVisualizationPage extends HorizontalPanel implements TabContent {

	protected final BasePage basePage;
	protected TabWrapper errorReceiver;

	public ResultsVisualizationPage(BasePage parent) {
		this.basePage = parent;

		Button b_ind = new Button("Draw Inclusion Dependencies!");
		final Element buttonElement_ind = b_ind.getElement();
		b_ind.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Element div = DOM.createDiv();
				buttonElement_ind.getParentElement().appendChild(div);

				drawInclusionDependencies(div);
			}
		});

		Button b_ucc = new Button("Draw Unique Column Combinations!");
		final Element buttonElement_ucc = b_ucc.getElement();
		b_ucc.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Element div = DOM.createDiv();
				buttonElement_ucc.getParentElement().appendChild(div);

				drawUniqueColumnCombinations(div);
			}
		});

		Button b_fd = new Button("Draw Functional Dependencies!");
		final Element buttonElement_fd = b_fd.getElement();
		b_fd.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Element div = DOM.createDiv();
				buttonElement_fd.getParentElement().appendChild(div);

				//drawFunctionalDependencies(div);
			}
		});

		this.add(b_ind);
		this.add(b_ucc);
		this.add(b_fd);
	}


	/* (non-Javadoc)
	 * @see de.uni_potsdam.hpi.metanome.frontend.client.TabContent#setErrorReceiver(de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper)
	 */
	@Override
	public void setErrorReceiver(TabWrapper tab) {
		this.errorReceiver = tab;
	}

	// call d3 with dom element & data
	private native void drawInclusionDependencies(Element div)/*-{
        $wnd.draw_ind(div);
    }-*/;

	// call d3 with dom element & data
	private native void drawUniqueColumnCombinations(Element div)/*-{
        $wnd.draw_ucc(div);
    }-*/;

	// call d3 with dom element & data
	private native void drawFunctionalDependencies(Element div)/*-{
        $wnd.draw_fd(div);
    }-*/;
}
