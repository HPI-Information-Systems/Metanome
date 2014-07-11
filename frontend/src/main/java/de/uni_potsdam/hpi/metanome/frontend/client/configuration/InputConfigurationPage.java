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

package de.uni_potsdam.hpi.metanome.frontend.client.configuration;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;
import de.uni_potsdam.hpi.metanome.frontend.client.TabContent;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;

public class InputConfigurationPage extends VerticalPanel implements TabContent {

  TabWrapper errorReceiver;
  BasePage basePage;

  HorizontalPanel content;
  DatabaseConnectionField dbField;
  FileInputField fileInputField;
  TableInputField tableInputField;

  /**
   * Constructor.
   */
  public InputConfigurationPage(BasePage basePage) {
    this.setWidth("100%");
    this.basePage = basePage;

    this.dbField = new DatabaseConnectionField();
    this.fileInputField = new FileInputField();
    this.tableInputField = new TableInputField(this.errorReceiver);
    this.content = new HorizontalPanel();

    Button dbButton = new Button("Database Connection", new ClickHandler() {
      public void onClick(ClickEvent event) {
        content.clear();
        content.add(dbField);
      }
    });
    Button fileButton = new Button("File Input", new ClickHandler() {
      public void onClick(ClickEvent event) {
        content.clear();
        content.add(fileInputField);
      }
    });
    Button tableButton = new Button("Table Input", new ClickHandler() {
      public void onClick(ClickEvent event) {
        content.clear();
        content.add(tableInputField);
      }
    });
    Button saveButton = new Button("Save", new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        // TODO Save Input
      }
    });

    VerticalPanel wrapper = new VerticalPanel();
    HorizontalPanel buttonWrapper = new HorizontalPanel();
    SimplePanel contentWrapper = new SimplePanel();

    buttonWrapper.add(dbButton);
    buttonWrapper.add(tableButton);
    buttonWrapper.add(fileButton);

    contentWrapper.add(this.content);
    this.content.add(dbField);

    wrapper.add(buttonWrapper);
    wrapper.add(contentWrapper);
    wrapper.add(saveButton);
    this.add(wrapper);
  }

  @Override
  public void setErrorReceiver(TabWrapper tab) {
    this.errorReceiver = tab;
  }

}
