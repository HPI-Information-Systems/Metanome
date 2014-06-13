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

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import de.uni_potsdam.hpi.metanome.frontend.client.TabContent;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;


/**
 * @author Tanja Bergmann
 *
 */
public class ResultsVisualizationPage extends FlowPanel implements TabContent {

	protected TabWrapper errorReceiver;

	public ResultsVisualizationPage() {
		this.setWidth("100%");

		SimplePanel indPanel = new SimplePanel();
		this.add(indPanel);
		SimplePanel uccPanel = new SimplePanel();
		this.add(uccPanel);
		SimplePanel fdPanel = new SimplePanel();
		this.add(fdPanel);

		drawInclusionDependencies(indPanel.getElement());
		drawUniqueColumnCombinations(uccPanel.getElement());
		//drawFunctionalDependencies(fdDiv);
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
