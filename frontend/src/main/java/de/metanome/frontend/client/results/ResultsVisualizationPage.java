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

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import de.metanome.frontend.client.TabContent;
import de.metanome.frontend.client.TabWrapper;


public class ResultsVisualizationPage extends FlowPanel implements TabContent {

  protected TabWrapper messageReceiver;

  public ResultsVisualizationPage() {
    Label label = new Label("Here comes some visualization...");
    label.setStyleName("resultTable-caption");
    this.add(label);
  }

  @Override
  public void setMessageReceiver(TabWrapper tab) {
    this.messageReceiver = tab;
  }
}
