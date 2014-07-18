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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.uni_potsdam.hpi.metanome.frontend.client.BasePage;
import de.uni_potsdam.hpi.metanome.frontend.client.TabContent;
import de.uni_potsdam.hpi.metanome.frontend.client.TabWrapper;
import de.uni_potsdam.hpi.metanome.frontend.client.helpers.InputValidationException;
import de.uni_potsdam.hpi.metanome.frontend.client.services.DatabaseConnectionService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.DatabaseConnectionServiceAsync;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FileInputService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.FileInputServiceAsync;
import de.uni_potsdam.hpi.metanome.frontend.client.services.TableInputService;
import de.uni_potsdam.hpi.metanome.frontend.client.services.TableInputServiceAsync;
import de.uni_potsdam.hpi.metanome.results_db.EntityStorageException;

public class InputConfigurationPage extends VerticalPanel implements TabContent {

  private TabWrapper errorReceiver;
  private BasePage basePage;

  protected HorizontalPanel content;
  protected DatabaseConnectionField dbField;
  protected FileInputField fileInputField;
  protected TableInputField tableInputField;

  protected Boolean dbFieldSelected;
  protected Boolean tableInputFieldSelected;
  protected Boolean fileInputFieldSelected;

  protected Button saveButton;

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

    Button dbButton = new Button("Database Connection");
    dbButton.addClickHandler(getClickHandlerForDbButton());
    Button fileButton = new Button("File Input");
    fileButton.addClickHandler(getClickHandlerForFileButton());
    Button tableButton = new Button("Table Input");
    tableButton.addClickHandler(getClickHandlerForTableButton());

    this.saveButton = new Button("Save", new ClickHandler() {
      @Override
      public void onClick(ClickEvent clickEvent) {
        saveObject();
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

    this.dbFieldSelected = true;
    this.tableInputFieldSelected = false;
    this.fileInputFieldSelected = false;

    wrapper.add(buttonWrapper);
    wrapper.add(contentWrapper);
    wrapper.add(saveButton);
    this.add(wrapper);
  }

  private ClickHandler getClickHandlerForFileButton() {
    return new ClickHandler() {
      public void onClick(ClickEvent event) {
        content.clear();
        content.add(fileInputField);

        dbFieldSelected = false;
        tableInputFieldSelected = false;
        fileInputFieldSelected = true;
      }
    };
  }

  private ClickHandler getClickHandlerForDbButton() {
    return new ClickHandler() {
      public void onClick(ClickEvent event) {
        content.clear();
        content.add(dbField);

        dbFieldSelected = true;
        tableInputFieldSelected = false;
        fileInputFieldSelected = false;
      }
    };
  }

  private ClickHandler getClickHandlerForTableButton() {
    return new ClickHandler() {
      public void onClick(ClickEvent event) {
        content.clear();
        content.add(tableInputField);

        dbFieldSelected = false;
        tableInputFieldSelected = true;
        fileInputFieldSelected = false;
      }
    };
  }

  protected void saveObject() {
    this.errorReceiver.clearErrors();

    if (dbFieldSelected) {
      DatabaseConnectionField w = (DatabaseConnectionField) content.getWidget(0);
      try {
        DatabaseConnectionServiceAsync databaseConnectionService = GWT.create(DatabaseConnectionService.class);
        databaseConnectionService.storeDatabaseConnection(w.getValue(), new AsyncCallback<Void>() {
          @Override
          public void onFailure(Throwable throwable) {
            errorReceiver.addError("Database Connection could not be stored!");
          }

          @Override
          public void onSuccess(Void aVoid) {
            dbField.reset();
          }
        });
        System.out.println("DB ID " + w.getValue().getId());
      } catch (InputValidationException e) {
        errorReceiver.addError("Database Connection could not be stored: " + e.getMessage());
      }

    } else if (tableInputFieldSelected) {
      TableInputField w = (TableInputField) content.getWidget(0);
      try {
        TableInputServiceAsync tableInputService = GWT.create(TableInputService.class);
        tableInputService.storeTableInput(w.getValue(), new AsyncCallback<Void>() {
          @Override
          public void onFailure(Throwable throwable) {
            errorReceiver.addError("Database Connection could not be stored!");
          }

          @Override
          public void onSuccess(Void aVoid) {
            tableInputField.reset();
          }
        });
        System.out.println("TABLE ID " + w.getValue().getId());
      } catch (InputValidationException | EntityStorageException e) {
        errorReceiver.addError("Table Input could not be stored: " + e.getMessage());
      }

    } else if (fileInputFieldSelected) {
      FileInputField w = (FileInputField) content.getWidget(0);
      try {
        FileInputServiceAsync fileInputService = GWT.create(FileInputService.class);
        fileInputService.storeFileInput(w.getValue(), new AsyncCallback<Void>() {
          @Override
          public void onFailure(Throwable throwable) {
            errorReceiver.addError("Database Connection could not be stored!");
          }

          @Override
          public void onSuccess(Void aVoid) {
            fileInputField.reset();
          }
        });
        System.out.println("FILE ID " + w.getValue().getId());
      } catch (InputValidationException e) {
        errorReceiver.addError("File Input could not be stored: " + e.getMessage());
      }
    }
  }

  @Override
  public void setErrorReceiver(TabWrapper tab) {
    this.errorReceiver = tab;
  }

}
