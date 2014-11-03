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

package de.metanome.frontend.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;


/**
 * A wrapper for tabs to show messages at the top of the tab. The messages can be either error
 * messages or info messages.
 */
public class TabWrapper extends FlowPanel {

  protected FlowPanel errorPanel;
  protected FlowPanel infoPanel;
  protected TabContent contentPanel;

  protected boolean inError = false;
  protected boolean inInfo = false;

  public TabWrapper() {
    this.errorPanel = new FlowPanel();
    this.errorPanel.addStyleName("errorMessage");
    this.add(this.errorPanel);

    this.infoPanel = new FlowPanel();
    this.infoPanel.addStyleName("infoMessage");
    this.add(this.infoPanel);
  }

  public TabWrapper(TabContent panel) {
    this();

    this.contentPanel = panel;
    this.contentPanel.setMessageReceiver(this);
    this.add(this.contentPanel);
  }

  /**
   * Displays the given info message.
   *
   * @param message the message, which should be displayed.
   */
  public void addInfo(String message) {
    Label label = new Label(message);
    this.infoPanel.add(label);
    this.setInInfo(true);
  }

  /**
   * Clears all info messages from the tab.
   */
  public void clearInfos() {
    this.infoPanel.clear();
    this.setInInfo(false);
  }

  /**
   * Displays the given error message.
   *
   * @param message the message, which should be displayed.
   */
  public void addError(String message) {
    Label label = new Label(message);
    this.errorPanel.add(label);
    this.setInError(true);
  }

  /**
   * Displays the given error message.
   *
   * @param message the message, which should be displayed.
   */
  public void addErrorHTML(String message) {
    HTML label = new HTML(message);
    this.errorPanel.add(label);
    this.setInError(true);
  }

  /**
   * Clears all error messages from the tab.
   */
  public void clearErrors() {
    this.errorPanel.clear();
    this.setInError(false);
  }

  /**
   * @return true, if a error message is set, false otherwise
   */
  public boolean isInError() {
    return inError;
  }

  /**
   * @param inError specifies if an error message is set or not
   */
  public void setInError(boolean inError) {
    this.inError = inError;
  }

  /**
   * @return true, if a info message is set, false otherwise
   */
  public boolean isInInfo() {
    return inInfo;
  }

  /**
   * @param inInfo specifies if an info message is set or not
   */
  public void setInInfo(boolean inInfo) {
    this.inInfo = inInfo;
  }
}
