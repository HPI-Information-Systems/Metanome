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

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * @author Claudia Exeler
 */
public class TabWrapper extends FlowPanel {

  protected VerticalPanel errorPanel;
  protected TabContent contentPanel;
  protected boolean inError = false;

  /**
   *
   */
  public TabWrapper() {
    this.errorPanel = new VerticalPanel();
    this.add(this.errorPanel);
    this.errorPanel.addStyleName("errorMessage");
  }

  public TabWrapper(TabContent panel) {
    this();

    this.contentPanel = panel;
    this.contentPanel.setErrorReceiver(this);
    this.add(this.contentPanel);
  }


  public void addError(String message) {
    Label label = new Label(message);
    this.errorPanel.add(label);
    this.setInError(true);
  }

  public void clearErrors() {
    this.errorPanel.clear();
    this.setInError(false);
  }

  public boolean isInError() {
    return inError;
  }

  public void setInError(boolean inError) {
    this.inError = inError;
  }
}
