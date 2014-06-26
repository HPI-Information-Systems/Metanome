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

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;
import de.uni_potsdam.hpi.metanome.frontend.client.TabContent;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;

/**
 * @author Claudia Exeler
 */
public class ResultsPage extends VerticalPanel implements TabContent {

    protected final BasePage basePage;
    protected TabWrapper errorReceiver;

    public ResultsPage(BasePage parent) {
        this.basePage = parent;

        this.add(new Label("Here goes a list of previous executions, with some filtering options.     "));
        this.add(new Label("And on the right half we can have the results of the execution selected on the left."));
    }

    /* (non-Javadoc)
     * @see de.uni_potsdam.hpi.metanome.frontend.client.TabContent#setErrorReceiver(de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper)
     */
    @Override
    public void setErrorReceiver(TabWrapper tab) {
        this.errorReceiver = tab;
    }
}
